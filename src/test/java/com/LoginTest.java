package com;

import org.testng.annotations.Test;

import pages.LoginPage;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.AfterClass;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.testng.Assert;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private WebDriverWait wait;
    private int failedAttempts = 0;

    @BeforeClass
    public void setUp() {
        driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1529, 993));
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void loginExitoso() {
        driver.get("https://demoqa.com/login");

        loginPage.setUsername("pedrito123");
        loginPage.setPassword("Pedrito123!");
        loginPage.login();

        wait.until(ExpectedConditions.urlToBe("https://demoqa.com/profile"));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "https://demoqa.com/profile", "La URL después del login no es la esperada");
        System.out.println("✅ Login exitoso confirmado. URL actual: " + currentUrl);

        // Logout
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Log out']"))).click();
        System.out.println("🔄 Logout realizado para reiniciar estado.");
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() throws IOException {
        FileInputStream fis = new FileInputStream("src/test/resources/datos/LoginData.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        int rowCount = sheet.getPhysicalNumberOfRows();
        int colCount = sheet.getRow(0).getLastCellNum();

        Object[][] data = new Object[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = sheet.getRow(i).getCell(j).toString();
            }
        }

        workbook.close();
        fis.close();
        return data;
    }

    @Test(dataProvider = "loginData")
    public void loginFallido(String username, String password) {
        if (failedAttempts >= 3) {
            System.out.println("Simulación: usuario bloqueado por múltiples intentos fallidos.");
            return;
        }

        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.login();

        String mensajeError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("name"))).getText();

        System.out.println("📩 Mensaje mostrado: " + mensajeError);
        Assert.assertTrue(mensajeError.contains("Invalid username or password!"),
                "No se encontró el mensaje esperado.");

        failedAttempts++;
        if (failedAttempts == 3) {
            System.out.println("3 intentos fallidos detectados. Simulando bloqueo.");
        }
    }
}
