#! /bin/bash

echo "Running docker entry command..."

export APP_NAME="mywebapp-0.0.1-SNAPSHOT.jar"
export APP_HOME="/usr/local/mywebapp"

echo "java -jar $APP_HOME/$APP_NAME"
eval "java -jar $APP_HOME/$APP_NAME"
