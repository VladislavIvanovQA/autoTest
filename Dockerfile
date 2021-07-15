FROM maven:3.6-jdk-11 as builder
ENV APP_HOME=/workDir/
COPY . $APP_HOME
WORKDIR $APP_HOME
RUN mvn clean
RUN mvn package

FROM openjdk:11.0.11-jdk
WORKDIR app
COPY --from=builder /workDir/target ./target
RUN chmod 777 ./target/autoTest-1.0-fat-tests.jar
CMD ["java","-jar","/app/target/autoTest-1.0-fat-tests.jar", "classes/testng.xml"]
