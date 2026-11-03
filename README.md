
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

## Assignment 8: Systems Monitoring

For this assignment, I used Docker stats to monitor the Study Session Tracker while it was running.

### Running the application

Build the project:

    ./mvnw clean verify

Build the Docker image:

    docker build -t cen4802-monitoring:latest .

Run the container:

    docker run -d --name cen4802-monitoring -p 8080:8080 cen4802-monitoring:latest

Open the application at http://localhost:8080/

### Monitoring

To check CPU and memory usage:

    docker stats cen4802-monitoring

I recorded the baseline measurements while the application was idle.

To generate additional activity, run this in another Terminal window:

    ./scripts/monitor-workload.sh

The script sends 10,000 requests to the application.

I compared CPU and memory usage before and during the workload.

To investigate the container after the workload:

    docker inspect cen4802-monitoring
    docker stats --no-stream cen4802-monitoring
    docker ps

The measurements and screenshots are included in the Assignment 8 PDF documentation.



## Assignment 9 - Software Profiling

For this assignment, I used VisualVM 2.2.2 to check how the Study Session Tracker uses memory.

I started the application with `./mvnw spring-boot:run` and connected VisualVM to the running Java process.

### Workload

I created `profiling-workload.sh` to send 1,000 study sessions to the application.

Run the script with `./profiling-workload.sh`.

### Results

VisualVM showed 1,002 StudySession objects using 24,048 bytes after the workload.

After performing garbage collection, the object count stayed at 1,002. This is consistent with the application keeping the sessions in an ArrayList.

### Possible Improvement

One improvement would be to limit the number of sessions stored in memory or move older sessions into a database.

I would repeat the same workload and compare memory usage and retained objects to see if the change helped.
