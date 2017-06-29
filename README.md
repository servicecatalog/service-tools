# service-tools

The service tools are a small framework for microservices based on the commander and event sourcing patterns. It uses the Jersey REST framework for its frontend and the Kafka Streams client for its backend. The intention is to provide the basic non-business functionality for each java-based microservice within OSCM.

## Overview

The tools are composed of several projects:

- `oscm-common-build` - Implementation of the tools within multiple modules.
- `oscm-archetype` - Maven archetype for getting started quickly with a new microservice.
- `oscm-dev` - Collection of configurations, scripts and style guides for developers. 

## Documentation

Architecture and implementation guides as well as a description of the configuration can be found [here](./oscm-common-build).

## Getting started

If you want to develop a new microservice based on these tools, just clone or download this repository, execute the following instructions. You will require Java 8 and maven for the installation, also docker if you want to run the application it in a container. Note that the sample application can run without any additional work.

1. Install maven archetype
  ```
  mvn install -f ./oscm-archetype/pom.xm
  ```

2. Install tools
  ```
  mvn install -f ./oscm-common-build/pom.xm
  ```

3. Create new project structure (replace `sample` with application name)
  ```
  mvn archetype:generate \
  	-DarchetypeCatalog=local \
  	-DarchetypeGroupId=org.oscm.archetype \
  	-DarchetypeArtifactId=oscm-archetype \
  	-DarchetypeVersion=1.0.0 \
  	-DgroupId=org.oscm.sample \
  	-DartifactId=sample \
  	-Dversion=0.0.1-SNAPSHOT \
  	-Dpackage=org.oscm.sample \
  	-DserviceToolVersion=1.0.0 \
  ```

  4. Build project
  ```
  mvn install -f ./oscm-sample-build/pom.xm
  ```

  5. Build docker image
  ```
  docker build -t oscm-sample ./oscm-sample-build
  ```

6. Adapt the config file oscm-sample-build/config.properties and start your docker image (assuming you have a running Kafka instance).

  ```
  docker run -it --env-file ./oscm-sample-build/config.properties --name oscm-sample -p 8080:8080 oscm-sample
  ```

## Application parameters

```
-c [local|env] {properites <location>}
	Determines the source, the type and the location of the configuration for this application.
-l stdout <level>
	Determines the destination and the log level for the application logger.

Examples:
-c env
	Filters all environment variable for configuration keys and loads them into the application.
	Usually used in container environments.
-c local properties ./config.properties
	Loads the properties file and loads all found configuration keys into the application.
-l stdout ALL
	Logs all log messages to the console.
```

## License

The service tools are released under the [Apache 2.0 license](./LICENSE).