FROM openjdk:13-jdk-alpine

RUN apk add git && \
    git clone https://github.com/COVID-Report/wirvsvirus-backend-quarkus.git && \
    cd wirvsvirus-backend-quarkus && \
    ./gradlew clean build -Dquarkus.package.uber-jar=true -x test && \
    mkdir /appdir && \
    mv build/*-runner.jar  /appdir/backend-runner.jar && \
    rm -rf wirvsvirus-backend-quarkus && \
    rm -rf ~/.gradle && \
    apk del git

ENTRYPOINT java -jar /appdir/backend-runner.jar
