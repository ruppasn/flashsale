### Flash Sale API

-----------------------
#### Problem statement
Design a robust & concurrent java application with Flash Sale Functionality and expose API (UI not required).

The following conditions holding true in your solution :

* Don’t oversell or undersell

* Assume Login & Registration to the e-commerce system is not required. 
  Registration to the flash sale API needs to be implemented.

* Only registered users allowed to make a purchase.

##### Assumptions
* Customer details cannot be edited.
* Customer has only one address.
* Products are predefined hence no api required to manage products.
* Customer must register before starting the flash sale and can order once in flash sale.
* Database is designed specific to flash sale, excluded the e-comers site details.

##### Tech Stack and Features
* Spring boot
* Gradle
* JPA
* Swagger UI support
* Global Response and Exception format
* Redis cache
* C3P0 connection pool
* PostgreSQL
* Versioned Flash sale system API

##### How to run the application:
* Start Redis and Postgres (here Docker containers are used for simplicity).

```shell script

# Run redis
docker run -d -p 6379:6379 --name redis1 redis

# Run Postgresql
docker run -d -p 5432:5432 --name my-postgres -e POSTGRES_PASSWORD=mysecretpassword postgres

## Access postgres cli in container
docker exec -it my-postgres bash
psql -h localhost -p 5432 -U postgres -W

```

Go to project root and run below command.

To start application:
```shell script
./gradlew bootRun
```

##### Application flow and API:
* Customer registers for flash sale 
    - ```/v1/customers``` - post
* Admin starts flash sale 
    - ```/v1/flashsale/start``` - get
* Registered customer(s) explore products and product descriptions
    - ```/v1/products``` - get
    - ```/v1/products/{productId}``` - get
* Registered customer(s) buy products 
    - ```/v1/orders``` - post
* Admin ends flash sale
    - ```/v1/flashsale/stop``` - get

Once application is started, access the API on Swagger UI using ```http://localhost:8080/swagger-ui.html```

##### Development tips:
* Created a spring boot application using spring initializer

* Added support for Swagger UI and global error and exception handlers.
* Wrote database models and make ddl config to ```create```
 to auto generate database tables. then change the confic to ```update```,
* Implemented customer registration
* Implemented start flash sale api
* All app constants will be found in /src/main/resources/application.yml
* Configure Redis to lock the inventory and restrict customer to order once.
* Implement test cases





