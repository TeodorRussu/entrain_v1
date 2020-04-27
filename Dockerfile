FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD /target/english-training-1.jar .
ENTRYPOINT ["java","-jar","/english-training-1.jar"]