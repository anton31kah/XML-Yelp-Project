#!/bin/bash

docker run --name spark-master --network host -v "/root/spark/XML-Yelp-Project/spark-cluster/spark-apps:/opt/spark-apps" -v "/root/spark/XML-Yelp-Project/spark-clus ter/spark-data:/opt/spark-data" -e SPARK_LOCAL_IP='192.168.1.102' -d anton31kah/spark-master:3.0.0

sleep 5

docker run --name spark-worker-1 -v "/root/spark/XML-Yelp-Project/spark-cluster/spark-apps:/opt/spark-apps" -v "/root/spark/XML-Yelp-Project/spark-clus ter/spark-data:/opt/spark-data" -e SPARK_LOCAL_IP='192.168.1.102' -e SPARK_WORKER_CORES='1' -e SPARK_WORKER_MEMORY='1G' -e SPARK_DRIVER_MEMORY='128m' -e SPARK_EXECUTOR_MEMORY='256m' -e SPARK_EXECUTOR_MEMORY='512m' -e SPARK_WORKER_WEBUI_PORT='8081' --network host -it anton31kah/spark-worker:3.0.0

docker run --name spark-worker-2 -v "/root/spark/XML-Yelp-Project/spark-cluster/spark-apps:/opt/spark-apps" -v "/root/spark/XML-Yelp-Project/spark-clus ter/spark-data:/opt/spark-data" -e SPARK_LOCAL_IP='192.168.1.102' -e SPARK_WORKER_CORES='1' -e SPARK_WORKER_MEMORY='1G' -e SPARK_DRIVER_MEMORY='128m' -e SPARK_EXECUTOR_MEMORY='256m' -e SPARK_EXECUTOR_MEMORY='512m' -e SPARK_WORKER_WEBUI_PORT='8082' --network host -it anton31kah/spark-worker:3.0.0

docker run --name spark-worker-3 -v "/root/spark/XML-Yelp-Project/spark-cluster/spark-apps:/opt/spark-apps" -v "/root/spark/XML-Yelp-Project/spark-clus ter/spark-data:/opt/spark-data" -e SPARK_LOCAL_IP='192.168.1.102' -e SPARK_WORKER_CORES='1' -e SPARK_WORKER_MEMORY='1G' -e SPARK_DRIVER_MEMORY='128m' -e SPARK_EXECUTOR_MEMORY='256m' -e SPARK_EXECUTOR_MEMORY='512m' -e SPARK_WORKER_WEBUI_PORT='8083' --network host -it anton31kah/spark-worker:3.0.0
