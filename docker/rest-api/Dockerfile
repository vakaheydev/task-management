FROM openjdk:21

RUN mkdir /app

WORKDIR /app

COPY ../../rest-api/target/rest-api-spring-boot.jar /app/app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]