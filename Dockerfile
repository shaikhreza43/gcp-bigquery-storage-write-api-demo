FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/spring-boot-gcp-bigquery-stream-api-demo-0.0.1-SNAPSHOT.war
COPY ${JAR_FILE} spring-boot-gcp-bigquery-stream-api-demo.war
ENTRYPOINT ["java","-jar","/spring-boot-gcp-bigquery-stream-api-demo.war"]