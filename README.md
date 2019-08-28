# money-transfer-api
Restful API to transfer money between two accounts

## Stack

- Java 11
- JAX-RS API
- H2 in memory database
- Hibernate
- JUnit
- Lombok
- Mapstruct
- Maven

## How to run

- Generate resources running: mvn clean install
- Run the fat jar: java -jar target/money-transfer-api-0.0.1-SNAPSHOT-jar-with-dependencies.jar

## Available endpoints

### Create a client
POST : http://localhost:8080/clients

Request Payload:
{
    "name": "John",
    "document": "DOC10231",
    "initialDeposit" : "10.55"
}

Response:
{
    "id": 1,
    "name": "John",
    "account": {
        "balance": 10.55
    }
}

### Retrieve a client
GET : http://localhost:8080/clients/{clientId}

Response:
{
    "id": 1,
    "name": "John",
    "document": "DOC10231",
    "account": {
        "id": 2,
        "balance": 10.55
    }
}

### Transfer money
POST : http://localhost:8080/clients/{clientId}/accounts/transactions

Request Payload:
{
	"amount" : 5,
	"toClientId" : 3
}

Response:
{
    "newBalance": "5.55"
}
