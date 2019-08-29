# money-transfer-api
Restful API to transfer money between two accounts

## Stack

- Java 11
- JAX-RS API
- Jetty
- H2 in memory database
- Hibernate
- JUnit
- Mockito
- Lombok
- Mapstruct
- Maven

## How to run

- Generate resources running: 
```sh
mvn clean install
```
- Run the fat jar:
```sh
java -jar target/money-transfer-api-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

## Available endpoints

### Create a client
POST : http://localhost:8080/clients

Request Payload:

```sh
{
    "name": "John",
    "document": "DOC10231",
    "initialDeposit" : "10.55"
}
```

Response:

```sh
{
    "id": 1,
    "name": "John",
    "account": {
        "balance": 10.55
    }
}
```

### Retrieve a client
GET : http://localhost:8080/clients/{clientId}

Response:

```sh
{
    "id": 1,
    "name": "John",
    "document": "DOC10231",
    "account": {
        "id": 2,
        "balance": 10.55
    }
}
```

### Transfer money
POST : http://localhost:8080/clients/{clientId}/accounts/transactions

Request Payload:

```sh
{
	"amount" : 5,
	"toClientId" : 3
}
```

Response:

```sh
{
    "newBalance": "5.55"
}
```
