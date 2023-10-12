FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
COPY target/spring-boot-library-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]