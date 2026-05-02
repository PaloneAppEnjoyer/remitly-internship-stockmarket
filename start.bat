@echo off

if "%~1"=="" (
    echo Error: No port specified.
    echo Usage: start.bat ^<PORT^>
    exit /b 1
)

set APP_PORT=%1

echo Starting on port %APP_PORT%...
docker-compose up --build -d

echo app is available at http://localhost:%APP_PORT%