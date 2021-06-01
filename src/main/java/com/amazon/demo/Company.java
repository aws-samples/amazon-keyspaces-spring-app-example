package com.amazon.demo;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Company {

    @PrimaryKey private final String companyId;

    private final String companyName;
    private final String duns;


    public Company(String companyId, String companyName, String duns) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.duns = duns;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDuns() {
        return duns;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", duns='" + duns + '\'' +
                '}';
    }
}
