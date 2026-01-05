FROM eclipse-temurin:21-jre

RUN mkdir /app

WORKDIR /app

COPY target/notifier-spring-boot.jar /app/app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]