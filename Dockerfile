FROM eclipse-temurin:21-jdk-jammy

RUN apt-get update && \
    apt-get install -y ca-certificates curl iputils-ping && \
    update-ca-certificates -f \

WORKDIR /app
COPY ./target/acady-backend-0.0.1-SNAPSHOT.jar acady.jar
ENV MONGODB_DRIVER_VERSION 4.11.1

EXPOSE 8080
ENTRYPOINT ["java", "-Djdk.tls.client.protcols=TLSv1.2", "-jar", "acady.jar"]