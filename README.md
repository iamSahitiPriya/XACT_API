## Pre-Requisite

-  Java 11

## Preffered IDE

-  IntelliJ 
-  Please add Lombok plugin & Enable Annotation processing.


## How To Build,Run

-  Build : ./gradlew clean build
-  Build : ./gradlew clean run

## Verify server response

-  Hit open endpoint : [Health](http://localhost:8000/health)
-  Secure endpoint works only when you pass Access Token in header : [SecuredEndpoint] (http://localhost:8000/v1/assessments/123)







## Micronaut 3.3.4 Documentation

- [User Guide](https://docs.micronaut.io/3.3.4/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.3.4/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.3.4/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
## Handler

[AWS Lambda Handler](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)

Handler: AssessmentHandler


## Feature aws-lambda documentation

- [Micronaut AWS Lambda Function documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)


