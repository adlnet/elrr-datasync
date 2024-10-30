# elrrdatasync
ELRR services which aid in the streaming (Kafka streams) of data from the staging database to the ELRR database.

There are database and kafka dependencies, but there's a [repo with a docker-compose](https://github.com/US-ELRR/elrrdatasync/) that resolves them locally.

# Dependencies
- [Java JDK 1.8](https://www.oracle.com/java/technologies/downloads/)
- [git](https://git-scm.com/downloads)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/products/docker-desktop/)
- [PostgreSQL](https://www.postgresql.org/download/)

# Tools
- SQL client or Terminal
- [Postman](https://www.postman.com/downloads/)
- [Eclipse](https://www.eclipse.org/downloads/packages/) or other IDE

# Create Docker Containers
- Start Docker Desktop
- docker compose up
- Check for new containers in Docker Desktop
   
# Create and populate PostgreSQL staging schema
- Start Docker Desktop
- Open DBeaver
- Run Create_sync_db.sql
- Run sync_db_insert.sql
- Run Create_service_db.sql
- Run service_db_insert.sql

# Build the application
- mvn clean install -Dmaven.test.skip=false

# Deploying the application on Docker 
The easiest way to deploy the sample application to Docker is to follow below steps:
- mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
- docker build --build-arg JAR_FILE="./target/elrrdatasync-0.0.1-SNAPSHOT.jar" --file Dockerfile -t <docker_hub>/test:elrrdatasync-dck-img .
- docker run -p Port:Port -t <docker_hub>/test:elrrdatasync-dck-img

# Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the com.deloitte.elrr.datasync.DatasyncApplication class from your IDE

# Alternatively you can use the Spring Boot Maven plugin: 
- [Run elrrexternalservices first](https://github.com/US-ELRR/elrrexternalservices)
- mvn clean
- mvn spring-boot:run -D"spring-boot.run.profiles"=local -e (Windows)
- mvn spring-boot:run -D spring-boot.run.profiles=local -e  (Linux)
- Ctrl+C to end --> Terminate batch job = Y

# Optional step 
- docker push <docker_hub>/test:elrrdatasync-dck-img
