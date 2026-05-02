#!/bin/bash

if [ -z "$1" ]; then
  echo "Error: No port specified."
  echo "Usage: ./start.sh <PORT>"
  exit 1
fi

export APP_PORT=$1
docker-compose up --build -d
echo "app is available at http://localhost:$APP_PORT"