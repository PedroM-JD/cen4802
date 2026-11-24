import csv
import sys

# Performance requirements
expected_requests = 500
max_response_time = 1

with open(sys.argv[1], newline="") as file:
    results = list(csv.DictReader(file))

total = len(results)
failed = 0
response_times = []

for result in results:
    elapsed = int(result["elapsed"])
    response_times.append(elapsed)

    if result["success"].lower() != "true" or elapsed > max_response_time:
        failed += 1

average = sum(response_times) / total if total else 0
maximum = max(response_times) if total else 0

print("Performance Test Results")
print("------------------------")
print(f"Total requests: {total}")
print(f"Failed requests: {failed}")
print(f"Average response time: {average:.2f} ms")
print(f"Maximum response time: {maximum} ms")
print(f"Response time limit: {max_response_time} ms")

# Fail the pipeline if requirements are not met
if total != expected_requests or failed > 0:
    print("PERFORMANCE TEST FAILED")
    sys.exit(1)

print("PERFORMANCE TEST PASSED")
sys.exit(0)