# springboot-mqtt-example
An example of integrating MQTT messaging into Spring Boot and Oracle database

## Requirements

Install **maven** and **gradle** to compile the projects, and **docker** to create containers for the needed apps.

- maven
- gradle
- docker

## Configuration

1. Create a database user, and inside the next table.
```
CREATE TABLE SENSOR (SENSOR_ID VARCHAR2(50), TEMP NUMBER, CLIENT_ID VARCHAR2(50), MOD_TIMESTAMP TIMESTAMP);
```

2. Edit the file ![application-localdocker.properties](mqtt-service/src/main/resources/application-localdocker.properties) with the proper values for the Oracle database and for the MQTT server connection. 
```
# Local Docker Properties
mqtt.hostname=host.docker.internal
mqtt.port=1883
mqtt.username=username
mqtt.password=password
mqtt.topic=example.sensor.temp

spring.datasource.url=jdbc:oracle:thin:@192.168.1.8:1521/ORCL
spring.datasource.username=mqtt
spring.datasource.password=oracle
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
spring.jpa.show-sql=true
spring.jpa.database=oracle
```

## Build

1. Go to https://www.oracle.com/es/database/technologies/appdev/jdbc-downloads.html and download the jdbc driver for your database.
2. Install the .jar into the local maven repository.
```
# For ojdbc8.jar would be like
mvn install:install-file -Dfile=ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
```
3. Run the following command to build the example:

```
./gradlew clean buildImage
```
    
This command builds the example as a set of Docker images.

## Test
Follow the steps below to run the example:

1. Run the following command to start the example:

```
docker-compose up
```

The Docker Compose script starts the following containers:

* 1 - Eclipse Mosquitto MQTT Broker
* 1 - [mqtt-service](mqtt-service) to collect temperature statistics.
* 2 - [temp-sensor](temp-sensor) to generate temperature data.
        
