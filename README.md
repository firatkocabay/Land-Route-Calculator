# Land Route Calculator

## Project Description
This service created for simple calculation of country land routes. Routing endpoint returns when if it can find a possible route from origin to destination country.

Given county data link is: https://raw.githubusercontent.com/mledoze/countries/master/countries.json

## How to Install and Run the Project
### Requirements
JDK 8

Docker

Maven 3

### Installation
You should run this command on project home directory:

docker build -t springio/gs-spring-boot-docker .

This command builds an image and tags it as 'springio/gs-spring-boot-docker'

#### Alternative Installation
You can run this command on project home directory:

mvn clean install

### Run Project
You should run this command:

docker run -p 8080:8080 springio/gs-spring-boot-docker

#### Alternative Run Project
You can run this command:

java -jar LandRouteCalculator-0.0.1-SNAPSHOT.jar

### Test
You can test with this curl command:

curl -L -X GET 'http://localhost:8080/routing/CZE/ITA'

expected response is:

{ "route": [ "CZE", "AUT", "ITA" ] }
