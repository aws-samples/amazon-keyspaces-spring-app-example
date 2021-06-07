// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.aws.mcs.springsample;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

@SpringBootTest
public class KeyspacesSpringApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyspacesSpringApplicationTests.class);

    @Autowired
    private CqlSession cqlSession;
        @Test
        void contextLoads() throws InterruptedException {

                UUID uuid = UUID.randomUUID();

                SimpleStatement stmtCreateKeyspaces = SchemaBuilder.createKeyspace("keyspace_name").ifNotExists()
                        .withSimpleStrategy(1)
                        .build();

                SimpleStatement stmtCreateTable = SchemaBuilder.createTable("keyspace_name","keyspace_table").ifNotExists()
                        .withPartitionKey("companyid", DataTypes.UUID)
                        .withColumn("companyname", DataTypes.TEXT)
                        .withColumn("uniquebusinessidentifier", DataTypes.TEXT)
                        .build();

                SimpleStatement stmtInsert = insertInto("keyspace_name","keyspace_table")
                        .value("companyid", raw(uuid.toString()))
                        .value("companyname", literal("AWS"))
                        .value("uniquebusinessidentifier", literal("00-00-0000")).build();

                SimpleStatement stmtQuery = QueryBuilder.selectFrom("keyspace_name","keyspace_table")
                        .column("uniquebusinessidentifier")
                        .whereColumn("companyid").isEqualTo(raw(uuid.toString()))
                        .build();

                cqlSession.execute(stmtCreateKeyspaces);
                Thread.sleep(5000);
                cqlSession.execute(stmtCreateTable);
                Thread.sleep(5000);
                cqlSession.execute(stmtInsert);
                Row row = cqlSession.execute(stmtQuery).one();
                assert "00-00-0000".equals(row.getString(0));
                LOGGER.info("Returned value:" + row.getString(0));
                LOGGER.info("CompanyId:" + uuid);

        }
    }

