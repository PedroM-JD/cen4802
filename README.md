
# Study Session Tracker

A simple Java web application for recording study sessions and tracking the total number of minutes studied.

## Features

- Add a study session with a course name and number of minutes
- Display all recorded study sessions
- Calculate total study time and number of sessions
- Delete individual study sessions
- Record application activity using logging

## Technologies

- Java 21
- Spring Boot
- Thymeleaf
- Maven
- SLF4J and Logback

## Running the Application

1. Open the project in IntelliJ IDEA.
2. Run `Cen4802Application`, or use `./mvnw spring-boot:run` in Terminal.
3. Open http://localhost:8080 in a browser.

## Running Tests

Run the following command:

```bash
./mvnw clean test
```

## Application Logging

The application uses SLF4J with Logback to record activity.

Logs are saved to `logs/study-session.log` in the project directory. File logging was selected so entries can be reviewed after the application stops. The logs stay on the local computer until deleted or rotated.

The application uses these logging levels:

- INFO: Normal application activity
- DEBUG: Additional details about calculations
- WARN: Invalid study session information
- ERROR: Invalid session deletion attempts

Logging settings are stored in `src/main/resources/application.properties`.

## Viewing Logs

View recent log entries:

```bash
tail -n 25 logs/study-session.log
```

Count log entries:

```bash
wc -l logs/study-session.log
```

Find warnings and errors:

```bash
grep -E "WARN|ERROR" logs/study-session.log
```

Find specific application events:

```bash
grep "Study session added" logs/study-session.log
```

## Changing Logging Levels

In `application.properties`, change:

```properties
logging.level.com.pedro.cen4802=INFO
```

to:

```properties
logging.level.com.pedro.cen4802=DEBUG
```

Restart the application to apply the change. DEBUG records additional information about application operations.

## Author

Pedro Salcedo
