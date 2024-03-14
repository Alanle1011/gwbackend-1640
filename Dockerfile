FROM openjdk:17
EXPOSE 5001 5002

ARG GWCOLLECTING_VERSION="0.0.1-SNAPSHOT"
ARG GWCOLLECTING="Backend-1640"
ARG GWCOLLECTING_WORKING_DIR="/gw/${GWCOLLECTING}"
ARG GWCOLLECTING_LOG_DIR="/gw/${GWCOLLECTING}/log"
ARG GWCOLLECTING_JAR_FILE="target/${GWCOLLECTING}-${GWCOLLECTING_VERSION}.jar"

WORKDIR ${GWCOLLECTING_WORKING_DIR}
VOLUME ${GWCOLLECTING_LOG_DIR}

ADD ${GWCOLLECTING_JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
