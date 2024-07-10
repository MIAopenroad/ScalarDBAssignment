#!/bin/bash
CONTAINER_NAME=$1
LOG_STRING=$2

while true; do
  if docker logs "$CONTAINER_NAME" 2>&1 | grep -q "$LOG_STRING"; then
    echo "Found log string: $LOG_STRING"
    break
  fi
  sleep 1
done
