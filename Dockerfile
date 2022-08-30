FROM openjdk:11
LABEL maintainer="epam.illia.ua"
ADD target/payments-spring-0.0.1-SNAPSHOT.jar payements-docker-demo.jar
ENTRYPOINT ["java", "-jar", "payements-docker-demo.jar"]