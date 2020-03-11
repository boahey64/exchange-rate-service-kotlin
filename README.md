# exchange-rate-service

## REST API

### current exchange rate
    /api/exchange-rate/{date}/{baseCurrency}/{targetCurrency}
    example: http://localhost:8080/api/exchange-rate/2019-12-31/EUR/CHF
    
    descending trend example: http://localhost:8080/api/exchange-rate/2020-01-12/EUR/PHP

### current exchange rate with parameters
    /api/exchange-rate/{date}/{baseCurrency}/{targetCurrency}?customerId={user}&randomQueryDate={true|false}
    example: http://localhost:8080/api/exchange-rate/2020-01-12/EUR/PHP?customerId=user2&randomQueryDate=true

### historical data from local database
    daily: /api/exchange-rate/history/local/daily/{yyyy}/{MM}/{dd}
    example: http://localhost:8080/api/exchange-rate/history/local/daily/2019/12/31
    monthly: /api/exchange-rate/history/local/monthly/{yyyy}/{MM}
    example: http://localhost:8080/api/exchange-rate/history/local/monthly/2020/01

### usage info from local database
    Needs user string in request body
    example: fakeUser
    
    daily: /api/exchange-rate/usage/monthly/{yyyy}/{MM}
    example: http://localhost:8080/api/exchange-rate/usage/monthly/2019/12
    monthly: /api/exchange-rate/history/local/yearly/{yyyy}
    example: http://localhost:8080/api/exchange-rate/usage/yearly/2019

### historical data from data source
    daily: /api/exchange-rate/history/daily/{yyyy}/{MM}/{dd}
    example: http://localhost:8080/api/exchange-rate/history/daily/2019/12/31
    monthly: /api/exchange-rate/history/monthly/{yyyy}/{MM}
    example: http://localhost:8080/api/exchange-rate/history/monthly/2020/01    
    
## data source
### documentation
    https://exchangeratesapi.io/
### current exchange rates   
    GET https://api.exchangeratesapi.io/latest HTTP/1.1
    
    {
      "base": "EUR",
      "date": "2018-04-08",
      "rates": {
        "CAD": 1.565,
        "CHF": 1.1798,
        "GBP": 0.87295,
        "SEK": 10.2983,
        "EUR": 1.092,
        "USD": 1.2234,
        ...
      }
    }
### historical data
    GET https://api.exchangeratesapi.io/history?start_at=2018-01-01&end_at=2018-09-01 HTTP/1.1
    
## build and run

### build
    ./mvnw clean verify    

### run
    ./mvnw spring-boot:run    
    
## database
### connection string
    jdbc:h2:mem:testdb
    
### h2 console (user: sa, password: )
    http://localhost:8080/h2-console    
    
## frontend development
### bundle
    yarn bundle
### watch
    yarn watch    
### start wiremock
    yarn wiremock    

            
## ToDO's
* RestTemplate vs. WebClient (/) 
`According to the Java Doc the RestTemplate will be deprecated. Spring team advise to use the WebClient if possible`
* refactor and refine validation (/)
* add validation to history calls (/)
* dissolve mixed Junit4 and Junit5 `wrong mockito version was the problem. set version to 2.28.2` (/)
* tests for exception handling and controller advice ? (/) 
`Testing controller advice and exception into ExchangeRateControllerIT`
* test DateUtil
* Mocking in ExchangeRateControllerIT doesn't work
* test jpa classes ? (/)
`with annotation @DataJpaTest setup an embedded database
and prepare database with a save staement into the test method or use 
@DatabaseSetup("createExchangeRate.xml") or Flyway or Liquibase to setup database data.`
* use wiremock in ExchangeRatesApiClientIT (/)
* make ./mvnw executable (/)
* cleanup sout (/)   
* run tests in ExchangeRatesApiClientIT when you start from console (/)
* extract ValidationService from ExchangeRateService (/)
* ExchangeRateServiceTest optimizing data objects (/)
