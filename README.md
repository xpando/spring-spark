# Getting Started

## Building and running

Run standalone:
```bash
./gradlew clean build && java -jar build/libs/sparkpi-0.0.1-SNAPSHOT.jar 10
```

Run with spark submit:
```bash
./gradlew clean build && spark-submit --master 'local[*]' --class org.springframework.boot.loader.JarLauncher build/libs/sparkpi-0.0.1-SNAPSHOT.jar 10
```

## Spring Cloud Data Flow

Run a Spring Cloud Data Flow stack via docker-compose:
```
cd docker/spring-cloud-data-flow
export DATAFLOW_VERSION=2.2.1.RELEASE
export SKIPPER_VERSION=2.1.2.RELEASE
docker-compose up

# to cleanup
docker-compose rm -f
```

Now hit: http://localhost:9393/dashboard for the UI

To access the shell run:
```bash
docker exec -it dataflow-server java -jar shell.jar
```

Spark-client task seems to be out of date. 

Register spark client task app
```
app register --type task --name spark-client --uri maven://org.springframework.cloud.task.app:spark-client-task:1.3.0.RELEASE
```

Create a spark task instance and launch it
```
task create spark1 --definition "spark-client --spark.app-name=sparkpi --spark.app-class=org.springframework.boot.loader.JarLauncher --spark.app-jar=file:/home/david/Projects/java/dataflow/sparkpi/build/libs/sparkpi-0.0.1-SNAPSHOT.jar --spark.app-args=10"
task launch spark1
```

Creating spark client task through the UI
```
spark-client --spark.app-jar=file:/opt/sparkpi-0.0.1-SNAPSHOT.jar --spark.app-class=org.springframework.boot.loader.JarLauncher --spark.app-args=10 --spark.app-name=sparkpi
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/gradle-plugin/reference/html/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
