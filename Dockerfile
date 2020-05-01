FROM openjdk:8
EXPOSE 9090
ADD target/spring-batch-demo.jar spring-batch-demo.jar
ENTRYPOINT ["java","-jar","/spring-batch-demo.jar"]