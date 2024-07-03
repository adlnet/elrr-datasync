# elrrdatasync

ELRR services which aid in the streaming (Kafka streams) of data from the staging database to the ELRR database.

1. [Requirements](#requirements)
2. [Building ELRR Datasync](#build-the-application)
3. [Deploy on Docker](#deploy-on-docker)
4. [Running the app locally](#run-app-locally)
5. [ELRR Data Sync API](docs/api/api.md)


## Requirements 

For building and running the elrrdatasync you need:
- JDK 1.8
- Maven 3



## Building the application 

To build the application run the following command

```shell
mvn clean install -Dmaven.test.skip=false
```


## Deploy on Docker 

The easiest way to deploy the sample application to Docker is to follow below steps:
1. Extract the jar file: ```mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)```
2. Build the docker image: ```docker build --build-arg JAR_FILE="./target/elrrdatasync-0.0.1-SNAPSHOT.jar" --file Dockerfile -t <docker_hub>/test:elrrdatasync-dck-img .```
3. Run the docker image: ```docker run -p Port:Port -t <docker_hub>/test:elrrdatasync-dck-img```



## Run app locally

There are several ways to run a Spring Boot application on your local machine.

1. Execute the main method in the com.deloitte.elrr.datasync.DatasyncApplication class from your IDE
2. Using the Spring Boot Maven plugin:  ```mvn spring-boot:run```
3. Optional step ```docker push <docker_hub>/test:elrrdatasync-dck-img```




