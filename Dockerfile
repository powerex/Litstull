FROM openjdk:17-alpine
COPY target/litsfull-*.jar listfull.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","listfull.jar"]