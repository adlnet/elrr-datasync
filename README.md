# elrrdatasync API
ELRR services which aid in the streaming (Kafka streams) of data from the staging database to the ELRR database.

Setup elrrdatasync first.

# Dependencies
- Java JDK 17
- git
- Maven 3
- Docker Desktop
- DBeaver
- Postman

# Create Docker Containers
- Start Docker Desktop
- Open terminal
- cd /elrrdatasync/dev-resources/docker-compose/docker-compose.yml
- docker compose up
- Check for new containers in Docker Desktop
   
# Create and populate PostgreSQL staging schema
- Start Docker Desktop
- Open DBeaver
- Run Create_sync_db.sql
- Run sync_db_insert.sql
- Run Create_service_db.sql
- Run service_db_insert.sql

# Run elrrdatasynch
- Run elrrexternalservices first
- Update application-local.properties to match docker-compose.yml
- Start Docker Desktop
- Open terminal
- git switch <dev feature branch>
- mvn clean
- mvn spring-boot:run -D"spring-boot.run.profiles"=local -e (Windows)
- mvn spring-boot:run -D spring-boot.run.profiles=local -e  (Linux)
- Ctrl+C to end --> Terminate batch job = Y

# Run Postman to populate lrs-db
- Start Docker Desktop
- Open Postman
- POST http://localhost:8083/xapi/statements
- Headers
   - &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; key = X-Experience-API-Version
   - &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; value = 1.0.3
- Body raw, JSON
- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; [
  {"actor":{"name":"Sophia Lewis","mbox":"mailto:sophia.lewis@us.navy.mil"},"verb":{"id":"https://adlnet.gov/expapi/verbs/achieved","display":{"en-us":"Achieved"}},"object":{"id":"https://w3id.org/xapi/credential/GIAC%20Security%20Essentials%20Certification%20%28GSEC%29","objectType":"Activity","definition":{"name":{"en-us":"GIAC Security Essentials Certification (GSEC)"},"type":"https://yetanalytics.com/deloitte-edlm/demo-profile/certificate"}},"stored":"2024-09-20T21:37:23.835000000Z","authority":{"account":{"homePage":"http://example.org","name":"0192115b-03d0-849f-8a65-f217ffbe2207"},"objectType":"Agent"},"id":"d9f1328b-bcc2-4b9c-b954-03cb88a240c8","timestamp":"2024-09-20T21:37:23.835000000Z","version":"1.0.0"} ]