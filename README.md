# L.I.T.S.T.U.L.L.

**L**ife **I**s **T**oo **S**hort **T**o **U**se **L**ong **L**inks

This is a simple example service for getting short URLs.

- [x] Spring Boot 2.7.3
- [x] Spring Data
- [x] Spring MVC
- [x] MongoDB 5.0.3
- [x] Docker 3.8
- [x] Redis 6.2.7
- [x] Unit Test

**System requirements**
- JDK 17
- Maven
- Docker

## API documentation


## Build and run application
The file [docker-compose.yaml](docker-compose.yml) contains all the necessary settings to configure the environment.
After starting application the rest api application will be available at localhost:8080/listfull/v1

### Access
Access localhost:8080/litstull/v1/cut?originUrl=
use for get short link

Access localhost:8080/litstull/v1/restore?shortLink=    use for restore full origin link


# Part 2 - What If

What if this service needed to scale to 10,000 URL generation requests per second? How about 100,000 URL resolve requests per second?

Perhaps we can set up **replication**, for example, so that the database contains copies on multiple servers, one of which is used for writing and the rest for reading. Reader copies must synchronize their content with updates. Then we will get a situation where the number of requests to the database is now distributed over several machines. (This will work if we read more often than we write.

We can also use message queues. If we need to get a new link, we do not immediately redirect it to the server and do not record it in the database. Instead, we send a message to a queue (such as Kafka) that we want to get the short link. This message will be issued to one of several servers (consumers) for processing and storage in the database. We can increase the number of such servers.