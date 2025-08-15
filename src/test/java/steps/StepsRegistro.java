package steps;

import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.But;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class StepsRegistro {

    private WebDriver driver;
    private WebDriverWait wait;

    // ===================== Given =====================
    @Given("el usuario está en la página de registro")
    public void usuarioEnRegistro() {
        driver = Hooks.getDriver();
        if (driver == null)
            throw new IllegalStateException("Driver no inicializado. Revisa Hooks.setUp()");
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));

        // Ir directo a la Practice Form
        driver.get("https://demoqa.com/automation-practice-form");

        // Mitigar anuncios/overlays comunes
        ocultarAds();
    }

    // ===================== When / And =====================
    @When("ingresa {string}, {string}, {string}, {string}, {string}")
    public void ingresarDatos(String firstName, String lastName, String email, String gender, String mobile) {
        // Nombre / Apellido
        clearAndType(By.id("firstName"), firstName);
        clearAndType(By.id("lastName"), lastName);

        // Email
        clearAndType(By.id("userEmail"), email);

        // Género
        if (gender != null && !gender.isBlank()) {
            By genderLabel = By.xpath("//label[normalize-space(text())='" + gender + "']");
            safeClick(genderLabel);
        }

        // Móvil
        clearAndType(By.id("userNumber"), mobile);

    }

    @And("hace clic en el botón de registro")
    public void clicEnRegistro() {
        safeClick(By.id("submit"));
    }

    // ===================== Then (éxito) =====================
    @Then("se muestra un modal de registro exitoso")
    public void validarModalExitoso() {
        // Esperar a que el modal esté visible y tenga el título esperado
        boolean ok = wait.withTimeout(Duration.ofSeconds(10))
                .until(ExpectedConditions.textToBePresentInElementLocated(
                        By.className("modal-title"), "Thanks for submitting the form"));

        assertTrue("El modal no contiene el texto esperado.", ok);

        // Cerrar modal
        try {
            safeClick(By.id("closeLargeModal"));
        } catch (Exception ignored) {
        }
    }

    // ===================== But + Then (inválido) =====================
    @But("no completa todos los campos requeridos")
    public void noCompletaCamposRequeridos() {
        // Este step describe la condición; no requiere acción
    }

    @Then("se marcan en rojo los campos requeridos incompletos")
    public void validarCamposRequeridosEnRojo() {
        // Verificar que exista al menos un campo requerido inválido después de enviar
        Long invalidCount = (Long) ((JavascriptExecutor) driver).executeScript(
                "return Array.from(document.querySelectorAll('input[required],textarea[required]'))" +
                        ".filter(el => !el.checkValidity()).length;");

        assertTrue("No se detectaron campos requeridos inválidos (en rojo).", invalidCount != null && invalidCount > 0);
    }

    // ===================== Helpers reutilizables =====================

    private void clearAndType(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        if (value != null && !value.isBlank()) {
            el.sendKeys(value);
        }
    }

    /** Oculta overlays/ads comunes para evitar click interceptado */
    private void ocultarAds() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll(\"iframe[id^='google_ads_iframe'], " +
                            "div[id*='google_ads'], div[id*='Ad.Plus-Anchor'], " +
                            "div[class*='ads'], iframe[data-is-safeframe='true']\").forEach(e=>e.style.display='none');");
        } catch (JavascriptException ignored) {
        }
    }

    /** Click robusto con scroll/espera y fallback JS */
    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        // Scroll centrado
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);

        // Intento normal
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
            return;
        } catch (ElementClickInterceptedException | JavascriptException ignored) {
            // Mitigar overlays y reintentar
            ocultarAds();
        }

        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
            return;
        } catch (WebDriverException ignored) {
        }

        // Fallback final
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
}
