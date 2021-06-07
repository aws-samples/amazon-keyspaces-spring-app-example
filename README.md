# Amazon Keyspaces (for Apache Cassandra) and the Spring Application

Explore Amazon Keyspaces (for Apache Cassandra) with SpringBoot.
## Description 
This sample is a practical intro to working Amazon Keyspaces application with SpringBoot.

### Prerequisites
````
    JDK 1.7.0u71 or better
    Maven 3.3.9 or better
    Spring-boot-starter-data-cassandra 2.5 or better
````

### Step-by-Step instruction to run the Sping Application using the SpringBoot and DataStax Java Driver for Apache Cassandra 
Before you begin please download the Starfield certificate and convert it to a trustStore file by following steps: 
https://docs.aws.amazon.com/keyspaces/latest/devguide/using_java_driver.html

Provide the path to the trustStore file and the password that you created previously:

``` ...
     advanced.ssl-engine-factory {
        class = DefaultSslEngineFactory
        truststore-path = "./src/main/resources/cassandra_truststore.jks"
        truststore-password = "my_trustStore_password"
      }
    ...
```

Create the authentication provider with the PlainTextAuthProvider class. ServiceUserName and ServicePassword should match 
the user name and password you obtained when you generated the service-specific credentials by following the steps in 
Generate Service-Specific Credentials 
(https://docs.aws.amazon.com/keyspaces/latest/devguide/programmatic.credentials.html#programmatic.credentials.ssc)
Set the service user name and service password via environmental variables, for example, 
``` 
    export AWS_MCS_SPRING_APP_USERNAME=ServiceUserName 
    export AWS_MCS_SPRING_APP_PASSWORD=ServicePassword 
```

Connect to Amazon Keyspaces by using the AWS Management Console (https://console.aws.amazon.com/keyspaces/home) or 
CQLSH (https://docs.aws.amazon.com/keyspaces/latest/devguide/getting-started.ddl.html).
The following example is using cqlsh to connect to Amazon Keyspaces:  
```
    cqlsh host 9142 -u ServiceUserName -p ServicePassword --ssl
```

Let's create one keyspace and one table:
````
    CREATE KEYSPACE keyspace_name WITH replication = {'class': 'com.amazonaws.cassandra.DefaultReplication'};
````

````
    CREATE TABLE "keyspace_name"."company"(
        "companyid" text,
        "companyname" text,
        "uniquebusinessidentifier" text,
        PRIMARY KEY("companyid"))
    WITH CUSTOM_PROPERTIES = {
        'capacity_mode':{'throughput_mode':'PAY_PER_REQUEST'},
        'point_in_time_recovery':{'status':'disabled'}}
````

You might want to run a unit test bootstraps Spring context before executing the unit test.
The unit test creates a keyspace "keyspace_name", and a table "table_name" if it does not exist.
After that it inserts a row in the table and queries it back to compare with a predefined value"00-00-0000".

To run the unit test: 

```
    ./mvwn test
```

Below shown a successful result:

```
2021-06-07 18:14:32.649  INFO 8526 --- [main] c.a.m.s.KeyspacesSpringApplicationTests  : Returned value:00-00-0000
2021-06-07 18:14:32.650  INFO 8526 --- [main] c.a.m.s.KeyspacesSpringApplicationTests  : CompanyId:2e74626a-f2dd-4791-88ae-f6d478728830
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 14.738 s - in com.aws.mcs.springsample.KeyspacesSpringApplicationTests
[INFO] Results: 
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  19.671 s
[INFO] Finished at: 2021-06-07T18:14:35-04:00
[INFO] ------------------------------------------------------------------------
```   

If you want to build everything at once, from the top directory run
```
    ./mvnw install
```

Enjoy! Feedback and PR's welcome!

### License

This library is licensed under the MIT-0 License. See the LICENSE file.
  