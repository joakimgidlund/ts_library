Feature: Searching for books
    As a user I want to be able to search for available books so I know what I can loan

    Scenario: Getting to the search page
        Given the user is on the start page
        When the user navigates to the book search
        Then they can see the search form

    Scenario: Searching for an existing book should give it as a result
        Given the user is on the search page
        When the user searches for a book
        Then they can see the book in results

    Scenario: Searching for a book that does not exist should not give results
        Given the user is on the search page
        When the user searches for a nonexisting book
        Then they can see no results