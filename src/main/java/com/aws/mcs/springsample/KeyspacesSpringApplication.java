package com.aws.mcs.springsample;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.EntityWriteResult;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.core.query.Query;

import java.security.NoSuchAlgorithmException;

import java.util.UUID;

import static org.springframework.data.cassandra.core.query.Criteria.where;

@SpringBootApplication
public class KeyspacesSpringApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(KeyspacesSpringApplication.class);

	private static Company addCompany(String companyName, String uniqueBusinessIdentifier) {
		return new Company(UUID.randomUUID().toString(), companyName, uniqueBusinessIdentifier);
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {

		SpringApplication.run(KeyspacesSpringApplication.class, args);
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
		// Select the first record from the table with no where clause
		Company resultOne = template.selectOne(Query.empty().limit(1), Company.class);
		// Select the second record based on the previous query with where clause
		Company resultSecond = template.selectOne(
				Query.query(where("companyId").is(resultOne.getCompanyId())), Company.class);
		LOGGER.info(resultSecond.toString());

		cqlSession.close();
		System.exit(0);
	}

}

