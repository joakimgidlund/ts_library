package se.yrgo.integrations.pos;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import se.yrgo.integrations.Utils;

public class SearchPage {
    final WebDriver driver;

    public SearchPage(WebDriver driver) {
        this.driver = driver;

        if(driver.findElements(By.cssSelector("form")).size() == 0) {
            throw new IllegalStateException("Not on search page");
        }
    }

    public Boolean isOnSearchPage() {
        WebElement form = Utils.find(driver, By.cssSelector("form"));
        return form.isDisplayed();
    }

    public void searchForBookByTitle(String book) {
        WebElement titleInput = Utils.find(driver, By.cssSelector("input.input:nth-child(1)"));
        titleInput.sendKeys(book);
        WebElement searchButton = Utils.find(driver, By.cssSelector("input.btn"));
        searchButton.click();
    }

    public Boolean findSearchResults(String book) {
        WebElement resultTable = Utils.find(driver, By.tagName("tbody"));
        List<WebElement> results = resultTable.findElements(By.tagName("tr"));
        for(var result : results) {
            if(result.findElement(By.cssSelector("td.text-ellipsis:nth-child(1)")).getText().equals(book)) {
                return true;
            }
        }
        return false;
    }

    public Boolean noResults() {
        return Utils.find(driver, By.cssSelector(".errors > div:nth-child(1)")).isDisplayed();
    }
}
