package se.yrgo.integrations.pos;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import se.yrgo.integrations.Utils;

public class LoanPage {
    private WebDriver driver;

    public LoanPage(WebDriver driver) {
        this.driver = driver;

        if (!driver.findElement(By.cssSelector("section.flex:nth-child(5) > form:nth-child(1) > h2:nth-child(1)"))
                .getText().equals("Lend out a book")) {
            throw new IllegalStateException("Not on loan page");
        }
    }

    public void loanBook(String user, String book) {
        var bookIdField = Utils.find(driver,
                By.cssSelector("section.flex:nth-child(5) > form:nth-child(1) > input:nth-child(2)"));
        bookIdField.sendKeys(book);
        var userIdField = Utils.find(driver,
                By.cssSelector("section.flex:nth-child(5) > form:nth-child(1) > input:nth-child(3)"));
        userIdField.sendKeys(user);
        var lendButton = Utils.find(driver,
                By.cssSelector("section.flex:nth-child(5) > form:nth-child(1) > input:nth-child(4)"));
        lendButton.click();
    }
}
