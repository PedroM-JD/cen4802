
# Study Session Tracker

CEN 4802 - Final Project

The Study Session Tracker is a Java web application that allows users to record study sessions and keep track of their total study time. Throughout the semester, the application was expanded to include automated testing, CI/CD, Docker, Kubernetes, logging, systems monitoring, software profiling, and Infrastructure as Code.

## Features

- Add study sessions with a course name and number of minutes
- View recorded study sessions
- Calculate total study time and number of sessions
- Delete individual study sessions
- Validate study session information
- Record application activity using logging
- Automatically test application functionality and performance

## Technologies

- Java 21
- Spring Boot
- Thymeleaf
- Maven
- JUnit
- Git and GitHub
- GitHub Actions
- Docker
- Kubernetes
- SLF4J and Logback
- VisualVM
- Terraform
- Apache JMeter
- Python

## Running the Application

Open the project in IntelliJ IDEA and run Cen4802Application.

The application can also be started from the Terminal:

```bash
./mvnw spring-boot:run
```

Open the application at http://localhost:8080.

## Unit Testing

Run the following command from the project directory:

```bash
./mvnw clean verify
```

The project contains six automated tests covering application startup, session creation, deletion, calculations, and input validation.

All six tests passed during the final project verification.

## Continuous Integration

The GitHub Actions workflow is located in `.github/workflows/maven.yml`.

The workflow performs the following steps:

1. Checks out the repository.
2. Sets up Java 21.
3. Builds the application and runs unit tests.
4. Uploads the packaged JAR file.
5. Builds the Docker image.
6. Starts the application container.
7. Runs the JMeter performance test.
8. Checks the performance results.
9. Uploads the performance report.
10. Stops and removes the container.
11. Saves and uploads the Docker image if the workflow succeeds.

JMeter is cached between workflow runs to reduce the time spent downloading it.

## Advanced Enhancement - Automated Performance Testing

For the final project, I added automated performance testing to the existing CI workflow using Apache JMeter and Python.

The idea was to check application performance before allowing the workflow to finish successfully and publish the Docker image.

The JMeter test plan is located in `performance/load-test.jmx`.

### Test Configuration

- Concurrent users: 20
- Ramp-up period: 5 seconds
- Iterations per user: 25
- Total HTTP requests: 500
- Request method: GET
- Expected HTTP status: 200
- Maximum response time: 500 ms

### Running the Performance Test Locally

Make sure the application is running on port 8080 and JMeter 5.6.3 and Python 3 are installed.

From the project directory, run:

```bash
mkdir -p target/performance
jmeter -n -t performance/load-test.jmx -l target/performance/results.jtl -e -o target/performance/report
```

The results are saved to `target/performance/results.jtl`.

The HTML report can be opened from `target/performance/report/index.html`.

Remove or rename the previous results file and report directory before repeating the test.

### Performance Quality Gate

The Python script is located in `performance/check_results.py`.

Run the script using:

```bash
python3 performance/check_results.py target/performance/results.jtl
```

The script checks that exactly 500 requests were completed, that none failed, and that every response was within the 500 ms limit.

If any of these requirements are not met, the script returns an exit code of 1, causing the CI workflow to fail.

### Controlled Failure and Recovery

To verify that the quality gate worked, I temporarily lowered the response time limit in the Python script to 1 ms.

The workflow failed as expected, and the Docker image was not published as an artifact. The performance report was still available for review.

I then restored the normal 500 ms threshold and ran the workflow again.

The workflow passed with the following results:

| Metric | Result |
| --- | --- |
| Total requests | 500 |
| Failed requests | 0 |
| Average response time | 3.16 ms |
| Maximum response time | 32 ms |
| Response time limit | 500 ms |

This demonstrated that the pipeline could detect a performance requirement failure and recover after the threshold was restored.

### Build Artifacts

A successful GitHub Actions workflow produces the following artifacts:

- study-session-tracker
- study-session-tracker-docker
- performance-test-report

These contain the packaged application, Docker image, and performance test results.

## Docker

Build the application and Docker image:

```bash
./mvnw clean verify
docker build -t study-session-tracker:latest .
```

Run the container:

```bash
docker run -d --name study-tracker -p 8080:8080 study-session-tracker:latest
```

Open http://localhost:8080.

Check the running container and application logs:

```bash
docker ps
docker logs study-tracker
```

To stop and remove the container:

```bash
docker stop study-tracker
docker rm study-tracker
```

### Running the CI Docker Image

The Docker image can also be downloaded from a successful GitHub Actions workflow.

After extracting the downloaded ZIP file, load the TAR file:

```bash
docker load -i study-session-tracker.tar
```

The CI image uses the AMD64 architecture. On my ARM64 MacBook, I ran the image using:

```bash
docker run -d --platform linux/amd64 --name cen4802-final -p 8080:8080 study-session-tracker:latest
```

I verified that session creation, deletion, calculations, and logging worked using the image generated by GitHub Actions.

## Kubernetes

The Kubernetes configuration files are located in the `k8s` directory.

- deployment.yaml
- service.yaml

Before deploying, build a Docker image that matches the architecture of the local Kubernetes node.

For my ARM64 MacBook, I used:

```bash
./mvnw clean verify
docker build --platform linux/arm64 -t study-session-tracker:latest .
```

Make sure Kubernetes is enabled in Docker Desktop.

Create the deployment and service:

```bash
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

Check the deployment:

```bash
kubectl get deployments,services,pods
```

Access the application using port forwarding:

```bash
kubectl port-forward service/study-session-tracker-service 8080:8080
```

Open http://localhost:8080.

Stop port forwarding with Control+C before continuing.

### Scaling

Increase the number of replicas:

```bash
kubectl scale deployment study-session-tracker --replicas=3
kubectl rollout status deployment/study-session-tracker
kubectl get deployments,pods
```

Reduce the deployment back to one replica:

```bash
kubectl scale deployment study-session-tracker --replicas=1
kubectl rollout status deployment/study-session-tracker
kubectl get deployments,pods
```

During testing, all three replicas started successfully, and the deployment was later reduced to one replica without restarting the original pod.

### Kubernetes Logs and Cleanup

View application logs:

```bash
kubectl logs deployment/study-session-tracker
```

Remove the application resources:

```bash
kubectl delete -f k8s/service.yaml
kubectl delete -f k8s/deployment.yaml
```

## Application Logging

The application uses SLF4J and Logback to record application activity.

Logging settings are stored in `src/main/resources/application.properties`.

The following logging levels are used:

- INFO: Normal application activity
- DEBUG: Additional details about calculations
- WARN: Invalid study session information
- ERROR: Invalid session deletion attempts

Application logs are saved to `logs/study-session.log`.

View recent log entries:

```bash
tail -n 25 logs/study-session.log
```

Find warnings and errors:

```bash
grep -E "WARN|ERROR" logs/study-session.log
```

Find recorded study sessions:

```bash
grep "Study session added" logs/study-session.log
```

I verified console logging in Docker and Kubernetes, along with local file logging and log rotation.

## Systems Monitoring

Docker stats and VisualVM were used to monitor application resource usage.

To monitor a running Docker container:

```bash
docker stats study-tracker
```

The original monitoring workload is available in `scripts/monitor-workload.sh`.

For the final project, I also used VisualVM 2.2.2 with the standalone application and generated a workload using JMeter.

I compared CPU usage, heap memory, and thread counts before and after the workload.

| Metric | Before workload | After workload |
| --- | --- | --- |
| CPU usage | 0.1% | 0.0% |
| Used heap memory | 25.63 MiB | 30.66 MiB |
| Live threads | 29 | 29 |

All 500 requests completed successfully. CPU usage returned to idle levels, and the number of live threads remained unchanged.

## Software Profiling

VisualVM was used to examine CPU activity and memory consumption.

I connected VisualVM to the running Java process and used the CPU and memory samplers while generating application activity with JMeter.

The CPU sampling results showed that the examined HTTP worker thread spent most of its time waiting for tasks during the sampling period.

Memory sampling showed an increase in byte arrays, object arrays, and strings during testing.

After performing garbage collection, used heap memory decreased to approximately 18.53 MiB.

### Performance Results During Profiling

| Metric | CPU sampling | Memory sampling |
| --- | --- | --- |
| Requests | 500 | 500 |
| Failed requests | 0 | 0 |
| Maximum response time | 21 ms | 104 ms |

Both workloads remained below the 500 ms response time limit.

The original profiling workload is also available in `profiling-workload.sh`.

These were short tests, so additional testing would be needed to evaluate long-term memory usage.

## Infrastructure as Code

Terraform was used to manage the Docker infrastructure for the application.

The configuration is located in `terraform/main.tf`.

Terraform manages two resources:

- Docker network: cen4802-terraform-network
- Docker container: cen4802-terraform-app

The container uses the `study-session-tracker:latest` Docker image and maps external port 8082 to internal port 8080.

### Creating the Infrastructure

Build the application and Docker image from the project directory:

```bash
./mvnw clean verify
docker build -t study-session-tracker:latest .
```

Make sure Docker Desktop is running and Terraform is installed.

Navigate to the Terraform directory:

```bash
cd terraform
```

Initialize and validate the configuration:

```bash
terraform init
terraform validate
```

Preview and create the resources:

```bash
terraform plan
terraform apply
```

Type yes when prompted for confirmation.

Open http://localhost:8082.

### Verifying the Infrastructure

```bash
terraform state list
docker ps
docker network ls
terraform plan
```

During final project testing, Terraform created both resources successfully.

Running terraform plan again showed that the infrastructure matched the configuration and no changes were necessary.

### Removing the Infrastructure

```bash
terraform plan -destroy
terraform destroy
```

Type yes when prompted for confirmation.

Both resources were successfully removed, and the Terraform state was empty afterward.

### Terraform Configuration Note

The Docker provider uses the local Docker Desktop socket path configured in `main.tf`.

This path may need to be updated when running the project on another computer.

## Current Limitations and Future Improvements

Study sessions are currently stored in an ArrayList. This means that the data is lost when the application stops and is not shared between Kubernetes replicas.

One improvement would be to use a database so that study sessions can be saved permanently and accessed by multiple application instances.

The current performance test focuses on GET requests to the home page. Future tests could also include adding and deleting sessions under different workloads.

## Author

Pedro Salcedo
