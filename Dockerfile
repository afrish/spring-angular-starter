FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

COPY .git* .git
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY angular.json .
COPY tsconfig.json .
COPY package.json .
COPY yarn.lock .

RUN --mount=type=cache,target=/root/.m2 --mount=type=cache,target=/app/node_modules ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-jammy
VOLUME /tmp

ARG DEPENDENCY=/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
         -cp app:app/lib/* \
         com.sample.starter.StarterApplication;
