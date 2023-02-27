# DKB URL Shortener

## Requirements
* JDK 17. You can use [SDK Man](https://sdkman.io/) to install it.
* [Docker](https://docs.docker.com/get-docker/)
* [Docker Compose](https://docs.docker.com/compose)

## Setup
* Run `docker-compose up` to start the local database.
* Run `./gradlew flywayMigrate` to initialize the database schema.
* Run `./gradlew bootRun` to launch the application.
* Run `./gradlew test` to run the tests.


### Classes:

1. `UrlController`: Containing APIs for shortening & resolving url. I have not added expiration time for the MVP, although this could be added in the request and could be used later. I could have also used open api to generate the API spec & classes but considering that is out of scope for now.
2. `UrlService`: Implement Shortening & Resolving Url. It also uses the classes like UrlValidator(validated URL), IdentifierGenerator(generates Identifier), CacheManager(for caching), UrlRepository(Database repository). As Later classes injected are interfaces, their implementations can be changed in future without much changes in Service class.
3. `UrlRepository`: Contains Database functions
4. `RedisConfiguration`: Reads the data for config from application.properties to generate RedisTemplate. Beans are created based on the profile used.
5. `DatabaseConfiguration`: Reads the data for from application.properties config to generate Database Client. Beans are created based on the profile. As mentioned in the comments in the file we don't need to have the Profiles as we use secrets.
6. `secrets`: All the configs are added to the secrets in encrypted form, which will be exposed as ENV variable based on the environment we are running in, further these ENV variables are read in the application.properties file.
7. `docker-compose`: to spawn db, redis.
8. Tests:
   a. `UrlControllerTests` - contains tests for the controller
   b. `UrlServiceTests` - contains tests for the service
   c. `UrlValidatorTests` & `IdentifierGeneratorTests` - contains test for respective classes
9. Also, added a regression tests to be run against the real apis using cucumber. Test provided in url.feature file.
10. Added swagger-ui dependency to expose the APIs to clients

### What is missing or assumed:
1. Provision for having Read Write/Transaction Manager but for MVP only one database-client/transaction manager is used.
2. Test containers for integration tests. Although I have made sure to implement the integration/regression that runs against the real service/data)
3. Monitoring using tools like Datadog/Grafana/Kibana
4. Optimistic locking using version in the Entity, rather used the unique constraints which helped with concurrent updates.
5. application-{profile}.properties reading ENV variables from the secrets, assuming secrets are read and ENV variable are exposed.
6. Overall Docker image for the application.

To Run use `./gradlew bootRun --args='--spring.profiles.active={profile}'` or `./gradlew clean build` and `java -jar build/libs/dkb-url-shortener-0.0.1.jar`

