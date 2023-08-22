package com.supercoding.commerce03.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;

//readOnly 속성을 구별하여 key를 반환
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() { // (1)

        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        String dataSourceKey = isReadOnly ? "slave" : "master";

        return dataSourceKey; //(2)
    }
}
