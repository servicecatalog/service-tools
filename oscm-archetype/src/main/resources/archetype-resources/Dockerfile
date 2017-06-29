#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
FROM openjdk:8u131-jdk-alpine

COPY oscm-${artifactId}-main/target/oscm-${artifactId}.jar /opt/jar
COPY oscm-${artifactId}-main/src/test/resources/keystore.jks /opt/keystore

ENTRYPOINT ["java", "-jar", "/opt/jar/oscm-${artifactId}.jar"]
CMD ["-c", "env", "-l", "stdout", "ALL"]