# KafkaLib-Inventory-System Application


**Overview**

The KafkaLib Inventory System Application is a microservices-based project designed to manage an inventory system using Apache Kafka and Spring Boot. The project includes two main microservices: a Kafka Producer microservice and a Kafka Consumer microservice. The Producer microservice collects book information via a scanning API and sends it to a Kafka topic, while the Consumer microservice consumes this data from the Kafka topic and persists it into a database.


**Features**

Microservices Architecture: Separate Producer and Consumer microservices. <br>
Kafka Integration: Utilizes Kafka for messaging between microservices. <br>
Spring Boot: Simplifies the development of microservices. <br>
Docker: Runs a Kafka cluster with multiple brokers and Zookeeper. <br>
Testing: Includes unit and integration tests using JUnit5. <br>
Error Handling and Recovery: Implements robust error handling, retry, and recovery mechanisms. <br>
Security: Secures Kafka communications using SSL. <br>
Project Structure <br>
Producer Microservice: <br>
Book scanning API to gather book data. <br>
Kafka producer to send book data to Kafka topic. <br>
Consumer Microservice: <br>
Kafka consumer to read data from Kafka topic. <br>
Persists book data into a database. <br>


**Prerequisites**

Java 8+ <br>
Maven <br>
Docker <br>
Apache Kafka <br>
Spring Boot <br>


**Setup Instructions**

Clone the Repository: 

git clone https://github.com/your-username/kafkalib-inventory-system.git <br>
cd kafkalib-inventory-system

Start Kafka Cluster: <br>
Ensure Docker is installed and running. <br>
Navigate to the Docker directory and run the Docker Compose file: <br>
cd kafka-cluster <br>
docker-compose up <br>

Build and Run Microservices: <br>
Navigate to the Producer microservice directory and run: <br>
cd library-events-producer <br>
mvn clean install <br>
mvn spring-boot:run <br>

Navigate to the Consumer microservice directory and run:
cd library-events-consumer <br>
mvn clean install <br>
mvn spring-boot:run <br>


**Usage**

Producer Microservice: <br>
Use the book scanning API to post book information. <br>

Consumer Microservice: <br>
The consumer will automatically consume messages from the Kafka topic and persist them into the database.<br>


**Testing**

Unit Tests:

Run unit tests for the Producer microservice: <br>
cd library-events-producer <br>
mvn test <br>

Run unit tests for the Consumer microservice: <br>
cd library-events-consumer <br>
mvn test


Integration Tests: <br>
Integration tests use Embedded Kafka to simulate Kafka interactions.

Run integration tests for the Producer microservice: <br>
cd library-events-producer <br>
mvn verify <br>

Run integration tests for the Consumer microservice: <br>
cd clibrary-events-consumer <br>
mvn verify <br>


**Error Handling, Retry, and Recovery**

The application implements robust error handling and retry mechanisms to ensure reliable message delivery and processing. Detailed configurations can be found in the respective microservice configuration files.


**Security**

Kafka communications are secured using SSL. Ensure that the necessary SSL certificates are configured properly in the Docker Compose file and the Spring Boot application properties.


**Contributors**

Kushaal Addagatla - Initial work - kushaaladdagatla22@gmail.com

**Acknowledgments**

Dilip Sundar Raj - Instructor of the Udemy course "Apache Kafka for Developers using Spring Boot"
