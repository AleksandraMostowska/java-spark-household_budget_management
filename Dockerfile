FROM openjdk:20
EXPOSE 8080
WORKDIR /web
ADD target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]