Feature: Get Customer with assets info

  @GetCustomerWithAssets
  Scenario: As a customer, I should be able to get all the assets with my info if they exists
    Given Customer externalId 14
    When Fetch Customer With Assets for externalId
    Then Assets size 2
    And Customer name is 'Han Solo'

  @GetCustomerWithAssets
  Scenario: As a customer, I should be able to get all the assets with my info if they doesn't exists
    Given Customer externalId 2
    When Fetch Customer With Assets for externalId
    Then Assets size 0
    And Customer name is 'C-3PO'

