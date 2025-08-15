package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import utils.DriverFactory;
import io.cucumber.java.After;

public class Hooks {
    @Before
    public void setUp() {
        System.out.println("[HOOK] Abriendo navegador...");
        WebDriverManager.chromedriver().setup();

        DriverFactory.driver = new ChromeDriver();
        DriverFactory.driver.manage().window().maximize();
    }

    @After
    public void tearDown(Scenario scenario) {
        System.out.println("[HOOK] Cerrando navegador...");

        if (DriverFactory.driver != null) {
            // Captura si falló
            if (scenario.isFailed()) {
                try {
                    TakesScreenshot ts = (TakesScreenshot) DriverFactory.driver;
                    byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "Captura en fallo");
                    System.out.println("[HOOK] Captura de pantalla adjuntada al reporte.");
                } catch (Exception e) {
                    System.err.println("[HOOK] Error al capturar pantalla: " + e.getMessage());
                }
            }

            DriverFactory.driver.quit();
        }
    }

    public static WebDriver getDriver() {
        return DriverFactory.driver;
    }

}