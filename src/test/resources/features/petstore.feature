Feature: Petstore basic flows

  Scenario: Register, login and add to cart
    Given I open the petstore homepage
    When I register a new user
    And I sign in with the registered credentials
    Then I add a pet to the cart
