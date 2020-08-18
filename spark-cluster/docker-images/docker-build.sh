#!/bin/bash

# cd to script directory so all paths are relative to the script and not to the directory from where the script was called
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd "$DIR"

set -e

docker build -t anton31kah/spark-base:3.0.0 ./spark-base
docker build -t anton31kah/spark-master:3.0.0 ./spark-master
docker build -t anton31kah/spark-worker:3.0.0 ./spark-worker
docker build -t anton31kah/spark-submit:3.0.0 ./spark-submit
