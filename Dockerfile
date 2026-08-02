FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace

# Opcional: Copia primero solo lo necesario para resolver dependencias
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY . .

RUN chmod +x ./gradlew

# Pasamos -Dorg.gradle.jvmargs para evitar que Gradle consuma toda la RAM en el build
RUN ./gradlew :app:bootJar --no-daemon -x test -Dorg.gradle.jvmargs="-Xmx512m"

# ==========================================================
# Etapa 2: Runtime (Usamos Alpine para reducir el peso de la imagen)
# ==========================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# En Alpine usamos 'curl' para descargar New Relic (más liviano que apt)
RUN apk add --no-cache curl unzip \
    && curl -sSL -o newrelic.zip https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip \
    && unzip -q newrelic.zip -d /app \
    && rm newrelic.zip \
    && apk del curl unzip

COPY --from=build /workspace/app/build/libs/*.jar app.jar

ENV NEW_RELIC_LICENSE_KEY=""
ENV NEW_RELIC_APP_NAME="our-place-backend"
ENV NEW_RELIC_LOG_LEVEL="info"

EXPOSE 8080

# CRÍTICO:
# -Xmx256m: Le dice a Java que la memoria Heap MÁXIMA sea de 256MB.
# -XX:+UseG1GC o SerialGC para bajo consumo.
ENTRYPOINT ["sh", "-c", "java -Xmx256m -XX:+UseSerialGC -javaagent:/app/newrelic/newrelic.jar -jar /app/app.jar"]