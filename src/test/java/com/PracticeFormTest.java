// Registro exitoso 

package com;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import static org.testng.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PracticeFormTest {

    WebDriver driver;
    JavascriptExecutor js;

    @BeforeClass
    public void setUp() {
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        driver.manage().window().maximize();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @DataProvider(name = "formData")
    public Object[][] getLoginData() throws IOException {
        FileInputStream fis = new FileInputStream("src/test/resources/datos/PracticeFormData.xlsx");
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int rowCount = sheet.getPhysicalNumberOfRows();
        int colCount = sheet.getRow(0).getLastCellNum();

        Object[][] data = new Object[rowCount - 1][colCount];
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                Cell cell = sheet.getRow(i).getCell(j);
                data[i - 1][j] = formatter.formatCellValue(cell); // convierte a texto visible
            }
        }

        workbook.close();
        fis.close();
        return data;
    }

    @Test(dataProvider = "formData")
    public void llenarFormulario(String firstName, String lastName, String email, String gender, String mobile) {
        driver.get("https://demoqa.com");
        driver.findElement(By.xpath("//h5[text()='Forms']")).click();
        driver.findElement(By.xpath("//span[text()='Practice Form']")).click();

        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys(firstName);

        driver.findElement(By.cssSelector("input#lastName")).clear();
        driver.findElement(By.cssSelector("input#lastName")).sendKeys(lastName);

        driver.findElement(By.xpath("//input[@id='userEmail']")).clear();
        driver.findElement(By.xpath("//input[@id='userEmail']")).sendKeys(email);

        driver.findElement(By.xpath("//label[text()='" + gender + "']")).click();

        driver.findElement(By.cssSelector("input#userNumber")).clear();
        driver.findElement(By.cssSelector("input#userNumber")).sendKeys(mobile);

        // Seleccionar 1 o 2 hobbies aleatorios
        List<String> hobbies = Arrays.asList("Sports", "Reading", "Music");
        Collections.shuffle(hobbies);
        int count = new Random().nextInt(2) + 1; // 1 o 2 hobbies

        for (int i = 0; i < count; i++) {
            String hobby = hobbies.get(i);
            driver.findElement(By.xpath("//label[text()='" + hobby + "']")).click();
            System.out.println("✅ Hobby seleccionado: " + hobby);
        }

        WebElement submitBtn = driver.findElement(By.id("submit"));
        js.executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        submitBtn.click();

        // Esperar a que el modal esté visible y contenga el texto esperado
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean isTextPresent = wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.className("modal-title"), "Thanks for submitting the form"));

        assertTrue(isTextPresent, "❌ El modal no contiene el texto esperado.");
        System.out.println("✅ El modal de confirmación se mostró correctamente.");

        // Captura de pantalla cuando aparece el modal
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File("screenshots/modal_confirmacion.png");
            FileUtils.copyFile(source, destination);
            System.out.println("📸 Captura del modal guardada con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ Error al guardar la captura del modal.");
        }

        // Cerrar modal
        driver.findElement(By.id("closeLargeModal")).click();
    }
}