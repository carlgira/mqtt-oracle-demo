FROM adoptopenjdk/openjdk11
ARG JAR_FILE
VOLUME /tmp
RUN mkdir -p /opt/mqtt-service
COPY build/libs/${JAR_FILE} /opt/mqtt-service/app.jar
COPY wallet_atptest /opt/mqtt-service/wallet_atptest
ENTRYPOINT ["java", "-Doracle.net.tns_admin=/opt/mqtt-service/wallet_atptest", "-Djava.security.egd=file:/dev/./urandom","-jar","/opt/mqtt-service/app.jar"]
