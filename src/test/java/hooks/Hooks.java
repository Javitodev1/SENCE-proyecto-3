package hooks;

import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;

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
    public void tearDown() {
        System.out.println("[HOOK] Cerrando navegador...");
        if (DriverFactory.driver != null) {
            DriverFactory.driver.quit();
        }
    }
     public static WebDriver getDriver() {
        return DriverFactory.driver;
    }

}