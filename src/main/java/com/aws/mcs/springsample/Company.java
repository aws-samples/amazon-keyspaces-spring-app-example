package com.aws.mcs.springsample;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Company {

    @PrimaryKey private final String companyId;

    private final String companyName;
    private final String uniqueBusinessIdentifier;


    public Company(String companyId, String companyName, String uniqueBusinessIdentifier) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.uniqueBusinessIdentifier = uniqueBusinessIdentifier;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getUniqueBusinessIdentifier() {
        return uniqueBusinessIdentifier;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", business identifier='" + uniqueBusinessIdentifier + '\'' +
                '}';
    }
}
