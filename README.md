# Usage
## If docker images NOT built
- Launch an Ubuntu terminal in the `project root dir`:

```shell script
# cd to project root dir
cd spark-cluster/docker-images
bash docker-build.sh
```

## If docker images built (or after you built them in the previous step)

## Launch Spark Cluster
- Launch a PowerShell terminal in the `project root dir`:

```shell script
# cd to project root dir
cd spark-cluster/docker-compose/xml-yelp-spark-cluster
docker-compose up --scale spark-worker=2
```

## Open Spark UI

- Launch Spark Master UI & Workers UIs:
    - Master: http://localhost:9090/
    - Workers: (2 of those should work)
        - http://localhost:8081/
        - http://localhost:8082/
        - http://localhost:8083/
        - http://localhost:8084/

## Build Project
- Launch a PowerShell terminal (you can use the Intellij one) in the `project root dir`:

```shell script
# cd to project root dir
sbt package
```

## Submit the Application
- Launch an Ubuntu terminal in the `project root dir`:

```shell script
# cd to project root dir
cd spark-cluster/spark-submit
bash fetch-jar.sh
bash fetch-properties.sh
bash test-aws.sh
```

## Check the App Running in Spark UI
- Refresh the workers' UIs a couple of times until the Application appears running.
- Use the STDOUT & STDERR links to open the corresponding logs in both workers.
- Notes:
    - Refresh views frequently using F5.
        - Logs as well should be refreshed using F5 instead of the useless buttons on screen like `Load More` & `Load New`.
    - When opening Applications' STDOUT & STDERR logs, replace the hostname & port with the worker url.
        - Example:
            - Replace `b232227f43e4:8081` in the following url with the `localhost:808X` with the worker url.
            - http://b232227f43e4:8081/logPage?appId=app-20200727201028-0000&executorId=0&logType=stdout
            - Notice however that the port in `b232227f43e4:8081` will always be `8081` but you should replace it with the worker's port.

# Spark Cluster Explanation
## The Big Picture
- When we submit our application, spark need to assign a driver for it.
- The spark master node will look for a worker to be the driver for the application.
- The driver's job is to:
    - read the code
    - convert it into tasks
    - and pass to the executors to execute the tasks
    - the tasks can be anything like map or filter or any other tasks.
- To execute the tasks, the driver asks the master node for any workers that can be executors, by checking their resources.
- The master node will look at all the available workers and connect the driver worker to the worker nodes which become executors.
- The driver starts sending them tasks for them to execute, and report back to the driver node.
- This explanation is extremely simplified, for a more detailed explanation check [this great answer](https://stackoverflow.com/a/32628057/6877477) here.
- So in our case, when using N workers (N >= 2).
- One worker would be a driver, the others will be executors.

## Observation
- We tested our application which:
    - reads from a file.
    - does some very simple processing.
    - writes to a different file.
    - writes to a single file using the hadoop `copyMerge` function to merge the `part` files.
    - prints to the stdout to report all the time.
- We launched 2 worker nodes.
- We submitted our application.
- Worker 2 was assigned to be the driver.
- Worker 1 was assigned to be the executor.
- Both have stdout & stderr logs.
- Worker 2 had the messages we printed (using `println`).
    - That means the driver has the print messages.
    - This makes sense because the driver is the one who reads the code.
- Worker 1 had the written files.
    - Actually both workers had the folders created but in Worker 2 (the driver) they were empty.
    - Worker 1 had the part files which contained actual written text.
    - This means the executor had the written files.
    - Which makes sense, because the task of writing to files was the executor's job.
        - If it doesn't make sense why the driver only had printed messages while the executor had files then imagine the following case.
        - You have 10 executors instead of 1, imagine all of them writing to a single driver node. Now imagine them writing each one to their own executor node.
- The joined file which uses hadoop's `copyMerge` function to combine the part files into a single file, failed in this case.
    - Since that code of merging the files is not a task, it is executed on the driver.
    - That means that file can be found on the driver's node.
    - But... it's empty.
    - Why? well it merges files which aren't on the driver's node, the files are on the executors' nodes.
- Another thing you can find in the Spark UI is the following:
	- In the driver worker node, the application is shown as an application, a whole thing, which when completes, shows the FINISHED status.
	- In the executor worker node, the application is shown as jobs (or tasks), which when complete, show the KILLED state.

## Redis Cluster
We have 3 t3.small instances and on each instance we run 2 redis nodes (1 master and 1 slave)
The master is running on ports 6379 and 16379 and the slave is running on ports 6479 and 16479 on all instances

- Starting the redis nodes
    - The shell scripts that start both the master and the slave are located at the home directory of the root user. 
    - **All of the redis nodes need to be started in order to create the cluster**
    - The shell script that start the cluster is located on XMLProject-RedisMaster EC2 instance. After you run the script you will get prompted to check if everything is fine with the cluster (ip addresses and etc) type **yes**.
    - In order to check if everything is working properly go into /root/redis-5.0.5/src of the XMLProject-RedisMaster EC2 instance and type **redis-cli -c** in order to get in the redis node (if you want to get into the other node type **redis-cli -c -p 6479**) and type **cluster nodes** to check if everything is connected and active. The other command that gives more information about the cluster is **cluster info**.
- Ressetting the redis cluster
    - You have to connect to every redis node type this **redis-cli -c -p 6479** or **redis-cli -c -p 6379**. After you got into the node you need to ented this command in order to reset the cluster **cluster reset hard**
    - Change the script or other configurations if needed and run the **create_cluster.sh** shell script to start the cluster again
