#!/bin/bash

# Sends requests to the Study Session Tracker
# Run docker stats in another terminal to monitor the container

URL="http://localhost:8080/"
REQUESTS=10000

echo "Starting workload: $REQUESTS requests"

for ((i=1; i<=REQUESTS; i++)); do
    curl -s -o /dev/null "$URL"
done

echo "Workload completed: $REQUESTS requests"
