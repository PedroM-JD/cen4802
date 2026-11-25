
#!/bin/bash

# Send test sessions to the application
for i in {1..1000}
do
    curl -s -o /dev/null -X POST http://localhost:8080/sessions \
        -d "courseName=ProfilingTest$i" \
        -d "minutes=30"

    if (( i % 100 == 0 )); then
        echo "Added $i sessions"
    fi
done

echo "Workload finished"
