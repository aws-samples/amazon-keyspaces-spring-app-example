package com.amazon.demo;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.EntityWriteResult;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

import java.util.UUID;

import static org.springframework.data.cassandra.core.query.Criteria.where;

@SpringBootApplication
@Component
public class KeyspacesDemoApplication {

	/*
	CREATE TABLE "keyspace_name"."company"(
	"companyid" text,
	"companyname" text,
	"duns" text,
	PRIMARY KEY("companyid"))
WITH CUSTOM_PROPERTIES = {
	'capacity_mode':{'throughput_mode':'PAY_PER_REQUEST'},
	'point_in_time_recovery':{'status':'enabled'}}
	 */

	private static final Logger LOGGER = LoggerFactory.getLogger(KeyspacesDemoApplication.class);

	private static Company addCompany(String companyName, String duns) {
		return new Company(UUID.randomUUID().toString(), companyName, duns);
	}

	@Autowired
	@Qualifier("keyspaceOneTemplateBeanId")
	private CassandraOperations cassandraOperations;

	public static void main(String[] args) throws NoSuchAlgorithmException {

		SpringApplication.run(KeyspacesDemoApplication.class, args);
		// use Java-based bean metadata to register an instance of a com.datastax.oss.driver.api.core.CqlSession
		CqlSession cqlSession = new AppConfig().session();

		// You can also configure additional options such as TTL, consistency level, and lightweight transactions when using InsertOptions and UpdateOptions
		InsertOptions insertOptions = org.springframework.data.cassandra.core.InsertOptions.builder().
				consistencyLevel(ConsistencyLevel.LOCAL_QUORUM).
				build();

		// The CqlTemplate can be used within a DAO implementation through direct instantiation with a SessionFactory reference or be configured in
		// the Spring container and given to DAOs as a bean reference. CqlTemplate is a foundational building block for CassandraTemplate
		CassandraOperations template = new CassandraTemplate(cqlSession);

		// Let's insert a new Company
		EntityWriteResult<Company> company = template.insert(addCompany("Amazon Inc.", "15-048-3782"), insertOptions);
		// Let's select tne newly inserted company from the Amazon Keyspaces table
		// Place your companyId into the query
		// SELECT * FROM keyspace_name.company WHERE companyid = "44cdb6ae-2bd4-4008-a0b2-60f6b8972f20"
		Company result = template.selectOne(
				Query.query(where("companyId").is("44cdb6ae-2bd4-4008-a0b2-60f6b8972f20")), Company.class);
		LOGGER.info(result.toString());

		cqlSession.close();
		System.exit(0);
	}

}

