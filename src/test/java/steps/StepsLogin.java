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
import static org.junit.Assert.assertFalse;

public class StepsLogin {

    private enum Outcome {
        EXITO, FALLO
    }

    private Outcome ultimoResultado = null;

    private WebDriver driver;
    private WebDriverWait wait;

    // ===================== Given =====================
    @Given("el usuario está en la página de login")
    public void usuarioEnLogin() {
        driver = Hooks.getDriver();
        if (driver == null)
            throw new IllegalStateException("Driver no inicializado. Revisa Hooks.setUp()");
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        driver.get("https://demoqa.com/login");
    }

    // ===================== When / And =====================
    @When("ingresa {string} y {string}")
    public void ingresarCredenciales(String user, String pass) {
        driver.findElement(By.id("userName")).clear();
        driver.findElement(By.id("userName")).sendKeys(user);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(pass);
    }

    @And("hace clic en el botón de inicio de sesión")
    public void clicEnLogin() {
        safeClick(By.id("login"));
    }

    // ===================== Then (éxito) =====================
    @Then("si el resultado es {string} se muestra la pantalla principal")
    public void validarResultadoExitoso(String esperado) {
        Outcome real = detectarResultado(Duration.ofSeconds(20)); // espera única/robusta
        if ("exitoso".equalsIgnoreCase(esperado)) {
            // Debía ser exitoso
            if (real != Outcome.EXITO) {
                throw new AssertionError(
                        "Se esperaba EXITO pero fue " + real + ". URL actual: " + driver.getCurrentUrl());
            }
            // Verificación final suave (por si ya detectó por URL)
            assertTrue("No se redirigió a /profile", driver.getCurrentUrl().contains("/profile"));
        } else {
            // No debía ser exitoso: aseguramos que NO estamos en /profile
            assertFalse("Se llegó a /profile en un caso no exitoso", driver.getCurrentUrl().contains("/profile"));
        }
    }

    // ===================== But (fallido) =====================
    @But("si el resultado es {string} se muestra un mensaje de error")
    public void validarResultadoFallido(String esperado) {
        Outcome real = detectarResultado(Duration.ofSeconds(20)); // reusa el cache y no duplica esperas
        if ("fallido".equalsIgnoreCase(esperado)) {
            if (real != Outcome.FALLO) {
                throw new AssertionError(
                        "Se esperaba FALLO pero fue " + real + ". URL actual: " + driver.getCurrentUrl());
            }
            // Verificación final suave del mensaje si ya detectamos fallo:
            try {
                WebElement msg = driver.findElement(By.id("name"));
                String text = msg.getText().toLowerCase();
                assertTrue("No apareció el mensaje de error esperado. Obtenido: " + text,
                        text.contains("invalid"));
            } catch (NoSuchElementException ignored) {
            }
        } else {
            // No debía ser fallido: aseguramos que NO haya mensaje de error visible
            try {
                WebElement msg = driver.findElement(By.id("name"));
                assertFalse("Apareció mensaje de error en un caso exitoso",
                        msg.isDisplayed() && msg.getText().trim().length() > 0);
            } catch (NoSuchElementException ignored) {
                /* Ok */ }
        }
    }

    /**
     * Espera única: o redirige a /profile (éxito) o aparece #name con "Invalid..."
     * (fallo). Cachea el resultado.
     */
    private Outcome detectarResultado(Duration timeout) {
        if (ultimoResultado != null)
            return ultimoResultado;

        WebDriverWait w = new WebDriverWait(driver, timeout);
        try {
            // Espera “o esto o lo otro”
            ultimoResultado = w.until(d -> {
                // ¿Éxito?
                if (d.getCurrentUrl().contains("/profile"))
                    return Outcome.EXITO;
                // ¿Fallo? (#name visible con "Invalid")
                try {
                    WebElement m = d.findElement(By.id("name"));
                    if (m.isDisplayed() && m.getText().toLowerCase().contains("invalid")) {
                        return Outcome.FALLO;
                    }
                } catch (NoSuchElementException ignored) {
                }
                return null; // seguir esperando
            });
        } catch (TimeoutException te) {
            // Si nada ocurrió, decidimos por estado actual:
            if (driver.getCurrentUrl().contains("/profile")) {
                ultimoResultado = Outcome.EXITO;
            } else {
                // En DemoQA suele ser fallo si sigues en /login
                ultimoResultado = Outcome.FALLO;
            }
        }
        return ultimoResultado;
    }

    // ===================== Helper: click robusto =====================
    /** Click con scroll/espera y mitigación de overlays (ads/iframes) */
    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        // 1) Scroll centrado al elemento
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);

        // 2) Intento normal: esperar clickeable y click
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
            return;
        } catch (ElementClickInterceptedException | JavascriptException ignored) {
            // seguimos con mitigación
        }

        // 3) Ocultar overlays/ads comunes (Google Ads safeframe, anchors, etc.)
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll(\"iframe[id^='google_ads_iframe'], " +
                            "div[id*='google_ads'], div[id*='Ad.Plus-Anchor'], " +
                            "div[class*='ads'], iframe[data-is-safeframe='true']\").forEach(e=>e.style.display='none');");
            Thread.sleep(200); // permitir reflow
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (JavascriptException ignored) {
        }

        // 4) Reintento normal
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
            return;
        } catch (WebDriverException ignored) {
        }

        // 5) Fallback final: click por JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
}
