FROM openjdk:21
COPY /target/user-auth-0.0.1-SNAPSHOT.jar /app/user-auth.jar
WORKDIR /app
EXPOSE 8082
CMD ["java", "-jar", "user-auth.jar"]
