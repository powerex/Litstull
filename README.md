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

#Access
Access localhost:8080/listfull/v1/cut?originUrl=
use for get short link

Access localhost:8080/listfull/v1/restore?shortLink=    use for restore full origin link