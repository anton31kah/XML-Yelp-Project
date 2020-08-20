#!/bin/bash

# cd to script directory so all paths are relative to the script and not to the directory from where the script was called
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd "$DIR"

cd ..

rm -f spark-data/app.properties
cp ../src/main/resources/app-aws.properties spark-data/app.properties
