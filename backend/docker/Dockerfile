FROM openjdk:8-jdk-alpine
WORKDIR /usr/app
ARG JAR_FILE=./app/*.jar
COPY ${JAR_FILE} ./app.jar
ENTRYPOINT ["java","-jar","app.jar"]