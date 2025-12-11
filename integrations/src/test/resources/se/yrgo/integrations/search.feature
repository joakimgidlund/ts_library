Feature: Searching for books
    As a user I want to be able to search for available books so I know what I can loan

    Scenario: Getting to the search page
        Given the user is on the start page
        When the user navigates to the book search
        Then they can see the search form