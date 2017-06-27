#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
FROM ubuntu

ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-amd64

RUN apt-get update && apt-get -y install openjdk-8-jre openjdk-8-jdk maven && mkdir /opt/jar

COPY oscm-${artifactId}-main/target/oscm-${artifactId}.jar /opt/jar

ENTRYPOINT ["java", "-jar", "/opt/jar/oscm-${artifactId}.jar"]
