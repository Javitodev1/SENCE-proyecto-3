package com;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Leccion1test {

    @Test
    @Parameters("browser")
    public void TitleTest(String browser) {
        WebDriver driver = null;

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            driver = new FirefoxDriver(options);
        }

        try {
            driver.get("https://demoqa.com/login");

            String Titulo = driver.findElement(By.tagName("h1")).getText();
            Assert.assertTrue(Titulo.contains("Login"), "El título no contiene 'Login' en " + browser);

        } catch (Exception e) {
            System.out.println("❌ Error en " + browser + ": " + e.getMessage());
            fail("Falló el flujo en " + browser);
        } finally {
            driver.quit();
        }
    }
}
