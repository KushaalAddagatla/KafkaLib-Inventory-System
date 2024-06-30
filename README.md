# KafkaLib-Inventory-System Application


#Overview
The KafkaLib Inventory System Application is a microservices-based project designed to manage an inventory system using Apache Kafka and Spring Boot. The project includes two main microservices: a Kafka Producer microservice and a Kafka Consumer microservice. The Producer microservice collects book information via a scanning API and sends it to a Kafka topic, while the Consumer microservice consumes this data from the Kafka topic and persists it into a database.

#Features
Microservices Architecture: Separate Producer and Consumer microservices.
Kafka Integration: Utilizes Kafka for messaging between microservices.
Spring Boot: Simplifies the development of microservices.
Docker: Runs a Kafka cluster with multiple brokers and Zookeeper.
Testing: Includes unit and integration tests using JUnit5.
Error Handling and Recovery: Implements robust error handling, retry, and recovery mechanisms.
Security: Secures Kafka communications using SSL.
Project Structure
Producer Microservice:
Book scanning API to gather book data.
Kafka producer to send book data to Kafka topic.
Consumer Microservice:
Kafka consumer to read data from Kafka topic.
Persists book data into a database.

#Prerequisites
Java 8+
Maven
Docker
Apache Kafka
Spring Boot
Setup Instructions
Clone the Repository:

#bash
Copy code
git clone https://github.com/your-username/kafkalib-inventory-system.git
cd kafkalib-inventory-system

Start Kafka Cluster:
Ensure Docker is installed and running.
Navigate to the Docker directory and run the Docker Compose file:
cd kafka-cluster
docker-compose up

Build and Run Microservices:
Navigate to the Producer microservice directory and run:
cd library-events-producer
mvn clean install
mvn spring-boot:run

Navigate to the Consumer microservice directory and run:
cd library-events-consumer
mvn clean install
mvn spring-boot:run

#Usage
Producer Microservice:
Use the book scanning API to post book information. 

Consumer Microservice:
The consumer will automatically consume messages from the Kafka topic and persist them into the database.

#Testing
Unit Tests:

Run unit tests for the Producer microservice:
cd library-events-producer
mvn test

Run unit tests for the Consumer microservice:
cd library-events-consumer
mvn test

Integration Tests:
Integration tests use Embedded Kafka to simulate Kafka interactions.

Run integration tests for the Producer microservice:
cd library-events-producer
mvn verify

Run integration tests for the Consumer microservice:
cd clibrary-events-consumer
mvn verify

#Error Handling, Retry, and Recovery
The application implements robust error handling and retry mechanisms to ensure reliable message delivery and processing. Detailed configurations can be found in the respective microservice configuration files.

#Security
Kafka communications are secured using SSL. Ensure that the necessary SSL certificates are configured properly in the Docker Compose file and the Spring Boot application properties.

#Contributors
Kushaal Addagatla - Initial work - kushaaladdagatla22@gmail.com

#Acknowledgments
Dilip Sundar Raj - Instructor of the Udemy course "Apache Kafka for Developers using Spring Boot"
