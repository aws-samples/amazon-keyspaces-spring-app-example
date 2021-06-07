# Amazon Keyspaces (for Apache Cassandra) and the Spring Application.

Explore Amazon Keyspaces (for Apache Cassandra) with SpringBoot.
## Description 
This sample is a practical intro to working Amazon Keyspaces application with SpringBoot.

### Prerequisites
````
    JDK 1.7.0u71 or better
    Maven 3.3.9 or better
    Spring-boot-starter-data-cassandra 2.5 or better
````

##Step-by-Step instruction to run the Sping Application using the SpringBoot and DataStax Java Driver for Apache Cassandra. 
Download the certificate using the following command and save it to the path_to_file/ directory.
```curl https://certs.secureserver.net/repository/sf-class2-root.crt -O```

Convert the certificate to a trustStore file
```openssl x509 -outform der -in sf-class2-root.crt -out temp_file.der```
```keytool -import -alias cassandra -keystore cassandra_truststore.jks -file temp_file.der```

SSL/TLS - initialize the SSLEngineFactory by adding a section in the application.conf file with a single 
line that specifies the class with class = DefaultSslEngineFactory. Provide the path to 
the trustStore file and the password that you created previously.
``` advanced.ssl-engine-factory {
        class = DefaultSslEngineFactory
        truststore-path = "./src/main/resources/cassandra_truststore.jks"
        truststore-password = "my_password"
      }
```

Create the authentication provider with the PlainTextAuthProvider class. ServiceUserName and ServicePassword should match 
the user name and password you obtained when you generated the service-specific credentials by following the steps in 
Generate Service-Specific Credentials 
(https://docs.aws.amazon.com/keyspaces/latest/devguide/programmatic.credentials.html#programmatic.credentials.ssc)
Set user name and password via environmental variables, for example, 
``` 
    export AWS_MCS_SPRING_APP_USERNAME=your_user_name 
    export AWS_MCS_SPRING_APP_PASSWORD=your_password 
```
Let's create one keyspace and one table

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

If you want to build everything at once, from the top directory run

```
    ./mvnw install
```

Enjoy! Feedback and PR's welcome!

## License

This library is licensed under the MIT-0 License. See the LICENSE file.
  