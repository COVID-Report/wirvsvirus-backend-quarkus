FROM openjdk:13-jdk-alpine as builder

COPY .  /wirvsvirus-backend-quarkus/

RUN cd wirvsvirus-backend-quarkus && \
    ./gradlew clean build -Dquarkus.package.uber-jar=true -x test && \
    cp build/*-runner.jar build/backend-runner.jar


# TARGET IMAGE
FROM openjdk:13-jdk-alpine

RUN adduser --no-create-home --disabled-password javarunner
COPY --chown=javarunner --from=builder /wirvsvirus-backend-quarkus/build/backend-runner.jar /

USER javarunner
ENTRYPOINT java -jar backend-runner.jar
