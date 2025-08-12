package pages;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegisterPage {
    private WebDriver driver;
    private JavascriptExecutor js;

    private By newuser = By.id("newUser");
    private By firstname = By.id("firstname");
    private By lastname = By.id("lastname");
    private By username = By.id("userName");
    private By password = By.id("password");
    private By register = By.id("register");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }

    private void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void createNewUser() {
        WebElement newUserButton = driver.findElement(newuser);
        scrollIntoView(newUserButton);
        newUserButton.click();
    }

    public void setFirstname(String name) {
        driver.findElement(firstname).sendKeys(name);
    }

    public void setLastname(String last) {
        driver.findElement(lastname).sendKeys(last);
    }

    public void setUsername(String user) {
        driver.findElement(username).sendKeys(user);
    }

    public void setPassword(String pass) {
        driver.findElement(password).sendKeys(pass);
    }

    public void register() {
        WebElement registerButton = driver.findElement(register);
        scrollIntoView(registerButton);
        registerButton.click();
    }

    public void saveScreenshot() {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("screenshots/registro_error.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
