#!/bin/bash

# cd to script directory so all paths are relative to the script and not to the directory from where the script was called
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd "$DIR"

cd ..

rm -f spark-apps/xmlyelpproject_2.12-0.1.jar
cp ../target/scala-2.12/xmlyelpproject_2.12-0.1.jar spark-apps/
