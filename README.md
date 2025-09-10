
Transaction Dispatcher Backend

Overview:
This Spring Boot application simulates a backend system that dispatches transactions to configured acquirers based on a percentage load distribution. It supports registering acquirers with assigned percentage shares and routes incoming transactions to the acquirer most behind in their assigned load share.

Technologies Used:
Java 17
Spring Boot 4.x
Spring Data JPA
MySQL
Hibernate ORM 7.x
Maven
Lombok

Project structure & Key classes:
src/main/java/com/example/transactiondispatcher/
|-- TransactionDispatcherApplication.java   (Spring Boot main class)
|-- entity/
|   |-- Acquirer.java
|   |-- Transaction.java
|-- repository/
|   |-- AcquirerRepository.java
|   |-- TransactionRepository.java
|-- service/
|   |-- TransactionService.java
|-- controller/
    |-- TransactionController.java


Features:
Register acquirers with specific percentage load share (e.g., A=10%, B=30%, C=60%)
Automatically assign new transactions to acquirers proportionally to their load percentage
Store transaction details and acquirer assignment in MySQL

Provide REST APIs to:
Create a transaction (auto-assigns acquirer)
Retrieve all transactions grouped by acquirer

Getting Started
Prerequisites:
JDK 17 or higher installed
Maven installed
MySQL database server running
IDE such as IntelliJ IDEA or Eclipse (optional)

Setup Instructions:
Clone the repository
bash:
git clone https://github.com/Surya221101/TransactionDispatcher.git
cd TransactionDispatcher

Configure MySQL
Create a database named transaction_db.
sql:
CREATE DATABASE transaction_db;
Update src/main/resources/application.properties with your database credentials.

Build and run the application
bash:
mvn clean install
mvn spring-boot:run
The application will start on port 8080.

API Endpoints:
Create Transaction
URL: /api/transactions

Method: POST
Request Body:
json
{
  "userId": 1001,
  "amount": 1500
}

Response:
json
{
  "transactionId": 11,
  "assignedAcquirer": "B",
  "status": "SUCCESS"
}
List Transactions Grouped By Acquirer
URL: /api/transactions/grouped

Method: GET
Response:
json
{
  "A": [
    { "id": 1, "userId": 1001, "amount": 500 }
  ],
  "B": [
    { "id": 2, "userId": 1002, "amount": 1500 }
  ],
  "C": [
    { "id": 3, "userId": 1003, "amount": 2000 }
  ]
}
Notes
Ensure acquirers are inserted into the database before creating transactions, e.g.:

sql:
INSERT INTO acquirers (name, percent_share) VALUES ('A', 10), ('B', 30), ('C', 60);
The routing logic balances transaction assignments to match the configured percentage shares.

Troubleshooting:
If Hibernate fails to start, check the dialect setting for MySQL in application.properties.

Use compatible versions of Java, Spring Boot, and dependencies as defined in pom.xml.



