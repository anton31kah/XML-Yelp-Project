#!/bin/bash

# cd to script directory so all paths are relative to the script and not to the directory from where the script was called
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd "$DIR"

SPARK_APPLICATION_JAR_LOCATION="/opt/spark-apps/xmlyelpproject_2.12-0.1.jar"
SPARK_APPLICATION_MAIN_CLASS="preprocessing.jobs.TestAws"

docker run --network xml-yelp-spark-cluster_default \
  -v "`pwd`/../spark-apps:/opt/spark-apps" \
  --env SPARK_APPLICATION_JAR_LOCATION=$SPARK_APPLICATION_JAR_LOCATION \
  --env SPARK_APPLICATION_MAIN_CLASS=$SPARK_APPLICATION_MAIN_CLASS \
  anton31kah/spark-submit:3.0.0