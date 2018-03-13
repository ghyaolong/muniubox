package com.taoding.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 配置数据源
 * @author vincent
 *
 */
@Configuration
@Component
public class DruidDataSoruceFactory {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setUrl(dataSourceConfig.getUrl());
        dataSource.setUsername(dataSourceConfig.getUsername());
        dataSource.setPassword(dataSourceConfig.getPassword());

        dataSource.setInitialSize(dataSourceConfig.getInitialSize());
        dataSource.setMinIdle(dataSourceConfig.getMinIdle());
        dataSource.setMaxActive(dataSourceConfig.getMaxActive());
        dataSource.setMaxWait(dataSourceConfig.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(dataSourceConfig.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(dataSourceConfig.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(dataSourceConfig.getValidationQuery());
        dataSource.setTestWhileIdle(dataSourceConfig.isTestWhileIdle());
        dataSource.setTestOnBorrow(dataSourceConfig.isTestOnBorrow());
        dataSource.setTestOnReturn(dataSourceConfig.isTestOnReturn());
        dataSource.setPoolPreparedStatements(dataSourceConfig.isPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(dataSourceConfig.getMaxPoolPreparedStatementPerConnectionSize());

        try {
            dataSource.setFilters(dataSourceConfig.getFilters());
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }

        dataSource.setConnectionProperties(dataSourceConfig.getConnectionProperties());

        return dataSource;
    }
}
