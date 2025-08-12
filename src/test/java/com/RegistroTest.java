package com;

import org.testng.annotations.Test;

import pages.RegisterPage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;

public class RegistroTest {
    private WebDriver driver;
    private RegisterPage registerPage;

    @BeforeMethod
    public void setUp() {
        driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1529, 945));
        registerPage = new RegisterPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void registroFallidoPorCaptcha() {
        driver.get("https://demoqa.com/login");

        registerPage.createNewUser();
        registerPage.setFirstname("Pedro");
        registerPage.setLastname("Pascal");
        registerPage.setUsername("pedrito12"); // corregido: antes se usaba firstname por error
        registerPage.setPassword("Pedrito123!");

        // ⚠️ No se puede automatizar el reCAPTCHA, así que el registro fallará
        registerPage.register();

        // Esperar brevemente por si aparece mensaje de error
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Capturar screenshot del posible error
        registerPage.saveScreenshot();
        System.out.println("📸 Screenshot guardado tras intento de registro fallido.");
    }
}
