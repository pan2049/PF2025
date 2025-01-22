package tw.pan.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {

	@Bean(name = "localDataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource.local")
	DataSourceProperties localDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "localDataSource")
	DataSource localDataSource(@Qualifier("localDataSourceProperties") DataSourceProperties localDataSourceProperties) {
		return localDataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean(name = "localJdbcTemplate")
	JdbcTemplate localJdbcTemplate(@Qualifier("localDataSource") DataSource localDataSource) {
		return new JdbcTemplate(localDataSource);
	}
}
