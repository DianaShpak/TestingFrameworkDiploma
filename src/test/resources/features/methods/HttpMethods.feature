Feature: Testing different HTTP methods

  Scenario: Launch log
    Given Test get
    Then Test patch
    Then Test delete
    Then Test post
    Then Test put
    Then Test put negative
    Then Test post negative