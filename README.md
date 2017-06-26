# service-tools

The service tools are a small framework for microservices based on the commander pattern and event sourcing. It uses the Jersey REST framework for its frontend and the Kafka streams client for its backend. The intention is to provide the basic non-business functionality for each java-based microservice within OSCM.

## Overview

The tools are composed of several projects:

- `oscm-common-build` - Implementation of the tools within multiple modules.
- `oscm-archetype` - Maven archetype for getting started quickly with a new microservice.
- `oscm-dev` - Collection of configurations, scripts and style guides for developers. 

## Documentation

Architecture and implementation guides can be found [here](./oscm-common-build/README.md).

## Getting started

If you want to develop a new microservice based on these tools, just clone or download this repository. You will require Java 8 and maven for the installation, also docker if you to run it in a container.

1. Install maven archetype

```
mvn install archetype:update-local-catalog -f ./oscm-archetype/pom.xm
mvn archetype:crawl
```

2. Install tools

```
mvn install -f ./oscm-common-build/pom.xm
```

3. Create new project structure (replace `sample` with service name)

```
mvn archetype:generate 
	-DarchetypeCatalog=local 
	-DarchetypeGroupId=org.oscm.archetype
	-DarchetypeArtifactId=oscm-archetype
	-DarchetypeVersion=1.0.0
	-DgroupId=org.oscm.sample
	-DartifactId=sample
	-Dversion=0.0.1-SNAPSHOT
	-Dpackage=org.oscm.sample
	-DserviceToolVersion=1.0.0
```

4. Build project (replace `sample` with service name)

```
mvn install -f ./oscm-sample-build/pom.xm
```

5. Build docker image (replace `sample` with service name)

```
docker build -t oscm-sample ./oscm-sample-build
```

## License

The service tools are released under the [Apache 2.0 license](./LICENSE).