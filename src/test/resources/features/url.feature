Feature: URL Shortener tests, to be run as a regression test

  @ResolveShortUrl
  Scenario: As a customer, I should be able to get original url from a short url
    Given Short url is 'http://www.dkb.com/bcH0MIm'
    When Resolve URL is called
    Then Original URL is 'http://www.google.com'

