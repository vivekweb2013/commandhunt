# CommandHunt Backend Module
This is the backend module of commandhunt application.


### Build the backend module

The following command is used to build this backend module.
```
mvn clean install -e
```
This will build the project. It also runs the tests in the build process.

Alternately, you can also build the project & skip the test execution with below command.
```
mvn clean install -DskipTests
```


### Running the backend module locally

**Note:** Before starting the backend module, export OAuth env variables(as defined in application.yml file) for using OAuth login.

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `CommandHuntApplication` class from your IDE.

Alternatively you can also use the below maven command that start the module using spring-boot plugin:

```
mvn spring-boot:run
```