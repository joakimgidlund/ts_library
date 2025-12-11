package se.yrgo.integrations.pos;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import se.yrgo.integrations.Utils;

public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;

        if(!driver.findElement(By.cssSelector("input.btn")).isDisplayed()) {
            throw new IllegalStateException();
        }
    }

    public void loginAsAdmin() {
        var usernameField = Utils.find(driver, By.cssSelector("input.input:nth-child(1)"));
        usernameField.sendKeys("test");
        var passwordField = Utils.find(driver, By.cssSelector("input.input:nth-child(1)"));
        passwordField.sendKeys("yrgoP4ssword");
        var loginButton = Utils.find(driver, By.cssSelector("input.btn"));
        loginButton.click();
    }
}
