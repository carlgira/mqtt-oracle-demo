# mqtt-oracle-demo
An example of integrating MQTT messaging into Spring Boot and Oracle database

## Requirements

Install **maven** and **gradle** to compile the projects, and **docker** to create containers for the needed apps.

- maven
- gradle
- docker

**  *For commands to install on centos-7 see file config-req.sh*

## Configuration

1. Create a database user, and inside the next table.
```
CREATE TABLE SENSOR (SENSOR_ID VARCHAR2(50), TEMP NUMBER, CLIENT_ID VARCHAR2(50), MOD_TIMESTAMP TIMESTAMP);
```

2. Configure the wallet of the database (in case you are using a wallet), copy the folder to the mqtt-service and configure the Dockerfile according.
**If you are going to use a direct connection erase this config from Dockerfile (in the example I use the wallet "wallet_atptest")**
```
COPY wallet_atptest /opt/mqtt-service/wallet_atptest
ENTRYPOINT ["java", "-Doracle.net.tns_admin=/opt/mqtt-service/wallet_atptest"
```

2. Edit the file ![application-localdocker.properties](mqtt-service/src/main/resources/application-localdocker.properties) with the proper values for the Oracle database and for the MQTT server connection.
```
# Local Docker Properties
mqtt.hostname=host.docker.internal
mqtt.port=1883
mqtt.username=username
mqtt.password=password
mqtt.topic=example.sensor.temp

# Oracle 
#spring.datasource.url=jdbc:oracle:thin:@atptest_high
spring.datasource.url=jdbc:oracle:thin:@192.168.1.8:1521/ORCL
spring.datasource.username=mqtt
spring.datasource.password=oracle
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
spring.jpa.show-sql=true
spring.jpa.database=oracle
```

## Build

1. Go to https://www.oracle.com/es/database/technologies/appdev/jdbc-downloads.html and download the jdbc driver for your database.
2. Install all the .jar into the local maven repository.
```
# For driver 19.3 would be like

mvn install:install-file -Dfile=ons.jar -DgroupId=com.oracle.jdbc -DartifactId=ons -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=orai18n.jar -DgroupId=com.oracle.jdbc -DartifactId=orai18n -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=osdt_core.jar -DgroupId=com.oracle.jdbc -DartifactId=osdt_core -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=ucp.jar -DgroupId=com.oracle.jdbc -DartifactId=ucp -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=xmlparserv2.jar -DgroupId=com.oracle.jdbc -DartifactId=xmlparserv2 -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=ojdbc8.jar -DgroupId=com.oracle.jdbc -DartifactId=ojdbc8 -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=oraclepki.jar -DgroupId=com.oracle.jdbc -DartifactId=oraclepki -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=osdt_cert.jar -DgroupId=com.oracle.jdbc -DartifactId=osdt_cert -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=simplefan.jar -DgroupId=com.oracle.jdbc -DartifactId=simplefan -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile=xdb.jar -DgroupId=com.oracle.jdbc -DartifactId=xdb -Dversion=19.3 -Dpackaging=jar -DgeneratePom=true
```

3. Run the following command to build the example:

```
./gradlew clean buildImage
```
    
This command builds the example as a set of Docker images.

## Run
Follow the steps below to run the example:

1. Run the following command to start the example:

```
docker-compose up
```

The Docker Compose script starts the following containers:

* 1 - Eclipse Mosquitto MQTT Broker
* 1 - [mqtt-service](mqtt-service) to collect temperature statistics.
* 2 - [temp-sensor](temp-sensor) to generate temperature data.
        
## Test

Every 5 seconds there is a request from the temp sensor to the mqtt service, but there is also a service that generates requests.


The endpoint of the service is "/generate" and has two query parameters:
* duration (milliseconds)
* rps (request per second)

The next request sends 10 request per second, during 4 seconds.
```
curl 'http://localhost:8080/generate?duration=4000&rps=10'
```