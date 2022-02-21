# currency-account-service

[![Build](https://github.com/wookieJ/account-service-api/actions/workflows/ci.yml/badge.svg?branch=master&event=push)](https://github.com/wookieJ/account-service-api/actions/workflows/ci.yml)

[https://currency-account-service.herokuapp.com/api/v1/accounts/1](https://currency-account-service.herokuapp.com/api/v1/accounts/1)

Example service with in-memory users accounts with PLN balance. API endpoint returns account by user's id with
USD balance converted by external exchange rate service. Authentication and authorization is handle on another layer.

## PLN to USD Conversion

Service uses [NBP API](https://api.nbp.pl) to get exchange rate. To ensure more stability and efficiency, 
following features are implemented:
 * Retries when NBP return 5xx status code.
 * Cache on getting exchange rate, because NBP refresh data every new day.

## Swagger
Check API docs in swagger:
[https://currency-account-service.herokuapp.com/swagger](https://currency-account-service.herokuapp.com/swagger)

Account response fields are described here (schema section):
[https://currency-account-service.herokuapp.com/swagger#account-controller/getAccount](https://currency-account-service.herokuapp.com/swagger#account-controller/getAccount)

## To do
Things to implement in the future:
 * Circuit-breaker pattern (instead of retries)
