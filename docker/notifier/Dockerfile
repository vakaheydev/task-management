FROM openjdk:21

RUN mkdir /app

WORKDIR /app

COPY ../../notifier/target/notifier-spring-boot.jar /app/app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]