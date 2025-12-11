package se.yrgo.integrations.pos;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import se.yrgo.integrations.Utils;

public class StartPage {
    private WebDriver driver;

    public StartPage(WebDriver driver) {
        this.driver = driver;

        if (!"The Library".equals(driver.getTitle())) {
            throw new IllegalStateException("Not on start page.");
        }
    }

    public SearchPage navigateToSearchPage() {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.btn:nth-child(3)")));
        Utils.find(driver, By.linkText("FIND A BOOK")).click();
        return new SearchPage(driver);
    }
}
