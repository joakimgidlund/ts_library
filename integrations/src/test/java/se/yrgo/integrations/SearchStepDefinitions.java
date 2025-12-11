package se.yrgo.integrations;

import io.cucumber.java.en.*;
import se.yrgo.integrations.pos.SearchPage;
import se.yrgo.integrations.pos.StartPage;

import static org.junit.jupiter.api.Assertions.fail;

public class SearchStepDefinitions {

    private StartPage startPage;
    private SearchPage searchPage;

    @Given("the user is on the start page")
    public void the_user_is_on_the_start_page() {
        startPage = Utils.openStartPage(GeneralStepDefinitions.getDriver());
    }

    @When("the user navigates to the book search")
    public void the_user_navigates_to_the_book_search() {
        searchPage = startPage.navigateToSearchPage();
    }

    @Then("they can see the search form")
    public void they_can_see_the_search_form() {
        if (searchPage.isOnSearchPage())
            return;

        fail("Form not found on search page.");
    }

    @Given("the user is on the search page")
    public void the_user_is_on_the_search_page() {
        searchPage = Utils.openSearchPage(GeneralStepDefinitions.getDriver());
    }

    @When("the user searches for a book")
    public void the_user_searches_for_a_book() {
        searchPage.searchForBookByTitle("The world atlas of wine");
    }

    @Then("they can see the book in results")
    public void they_can_see_the_book_in_results() {
        var result = searchPage.findSearchResults("The world atlas of wine");

        if (result)
            return;

        fail("Book not found in results.");
    }

    @When("the user searches for a nonexisting book")
    public void the_user_searches_for_a_nonexisting_book() {
        searchPage.searchForBookByTitle("asaasgagkjaja");
    }

    @Then("they can see no results")
    public void they_can_see_no_results() {
        var result = searchPage.noResults();

        if (result)
            return;

        fail("Books found in results.");
    }
}
