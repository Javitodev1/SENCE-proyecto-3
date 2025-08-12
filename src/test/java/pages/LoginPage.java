package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;

    By username = By.id("userName");
    By password = By.id("password");
    By login = By.id("login");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setUsername(String user) {
        driver.findElement(username).sendKeys(user);
    }

    public void setPassword(String pass) {
        driver.findElement(password).sendKeys(pass);
    }

    public void login() {
        driver.findElement(login).click();
    }
}
