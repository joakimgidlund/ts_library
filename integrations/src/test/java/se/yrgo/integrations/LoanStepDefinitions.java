package se.yrgo.integrations;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import se.yrgo.integrations.pos.LoanPage;
import se.yrgo.integrations.pos.LoginPage;

public class LoanStepDefinitions {
    private LoginPage loginPage;
    private LoanPage loanPage;

    @Given("the administrator test is logged in")
    public void the_administrator_test_is_logged_in() {
        loginPage = Utils.openLoginPage(GeneralStepDefinitions.getDriver());
        loginPage.loginAsAdmin();
    }

    @Given("is on the loan page")
    public void is_on_the_loan_page() {
        loanPage = Utils.openLoanPageAsAdmin(GeneralStepDefinitions.getDriver());
    }

    @When("the administrator lends the book 1 to user 1")
    public void the_administrator_lends_the_book_1_to_user_1() {
        loanPage.loanBook("2", "5");
    }

    @Then("the book should be noted as loaned to the user")
    public void the_book_should_be_noted_as_loaned_to_the_user() throws IOException, InterruptedException {
        getLoanedBooksFromBackend();
    }

    private void getLoanedBooksFromBackend() throws IOException, InterruptedException {
        // we need a http client that can handle cookies since our
        // login information is handled using cookies
        CookieManager cookieHandler = new CookieManager();
        HttpClient client = HttpClient.newBuilder().cookieHandler(cookieHandler).build();
        // login data as a json string
        String loginData = "{\"username\":\"test2\",\"password\":\"yrgoP4ssword\"}";
        HttpRequest loginReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8888/api/login"))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginData)).build();
        HttpResponse<String> loginResp = client.send(loginReq,
                HttpResponse.BodyHandlers.ofString());
        if (loginResp.statusCode() != 200) {
            throw new IllegalStateException("Could not log in");
        }
        HttpRequest lendReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8888/api/user/loans"))
                .build();
        HttpResponse<String> bookResp = client.send(lendReq,
                HttpResponse.BodyHandlers.ofString());

        System.out.println(bookResp.body());
        // here bookResp.body() is a JSON string of all the books the user has on loan
    }
}
