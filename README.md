if images not built

```shell script
# cd to project root dir
cd spark-cluster/docker-images
bash docker-build.sh
```

to start spark cluster

```shell script
# cd to project root dir
cd spark-cluster/docker-compose/xml-yelp-spark-cluster
docker-compose up --scale spark-worker=2
```

open urls
- http://localhost:9090/

and try those
- http://localhost:8081/
- http://localhost:8082/
- http://localhost:8083/
- http://localhost:8084/

```shell script
# cd to project root dir
sbt package
```

```shell script
# cd to project root dir
cd spark-cluster/spark-submit
bash fetch-jar.sh
bash test-aws.sh
```
