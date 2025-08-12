package com;

import java.time.Duration;
import org.testng.annotations.Test;
import org.testng.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.DataProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Leccion7test {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][] {
                { "Weasta", "Nacho0109#" },
                { "pedrito123", "Pedrito123!" },
                { "invalidUser", "wrongPassword" }
        };
    }

    @Test(dataProvider = "loginData")
    public void DataProviderTest(String username, String password) {

        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver();

        driver.manage().window().maximize();

        driver.get("https://demoqa.com/login");

        driver.findElement(By.id("userName")).click();
        driver.findElement(By.id("userName")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ReactTable")));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("profile"),
                "El login ha fallado o no se ha redirigido a la página de perfil.");

        driver.quit();

    }

}
