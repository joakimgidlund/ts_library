Feature: Loaning books
    As a user I want to lend books and see their status

    Scenario: Lending out a book
        Given the administrator test is logged in
        And is on the loan page
        When the administrator lends the book 1 to user 1
        Then the book should be noted as loaned to the user
