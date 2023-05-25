package com.cebi.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cebi.entity.ApplicationLabel;
import com.cebi.entity.AuditHistory;
import com.cebi.entity.Banks;
import com.cebi.entity.BranchInformation;
import com.cebi.entity.BranchInformationId;
import com.cebi.entity.Ccdp;
import com.cebi.entity.Ccdp010;
import com.cebi.entity.PreDefineReport;
import com.cebi.entity.QueryData;
import com.cebi.entity.ReportQueueData;
import com.cebi.entity.RequiredField;
import com.cebi.entity.TellerMaster;
import com.cebi.entity.ViewInfo;
import com.cebi.utility.AES;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class DbConfiguration {

	@Autowired
	private Environment env;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	/*---------- session factory for static reports --------------*/

	@Bean(name = "staticReportbasicDataSource")
	public DataSource mySqlStaticReportDataSource() throws Exception {
		BasicDataSource dataSource = new BasicDataSource();
		String appName = env.getProperty("application.static");
		dataSource.setDriverClassName(env.getProperty(appName + ".driverClassName"));
		dataSource.setUrl(env.getProperty(appName + ".databaseurl"));
		dataSource.setUsername(env.getProperty(appName + ".username"));
		dataSource.setPassword(AES.decrypt(env.getProperty(appName + ".password")));
		return dataSource;
	}

	@Bean(name = "staticReportSessionFactory")
	public LocalSessionFactoryBean getStaticReportSessionFactory(@Qualifier("staticReportbasicDataSource") DataSource dataSource) {
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(dataSource);
		localSessionFactoryBean.setAnnotatedClasses(BranchInformation.class, BranchInformationId.class, Ccdp.class, Ccdp010.class);
		localSessionFactoryBean.setHibernateProperties(getHibernateProperties());
		return localSessionFactoryBean;
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate getJdbcTemplate(@Qualifier("staticReportbasicDataSource") DataSource source) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(source);
		return jdbcTemplate;
	}

	/* ---------end of session factory for static report ------------- */

	/* --------- session factory for prod ------------- */
	@Bean(name = "basicDataSource")
	public DataSource mySqlDataSource() throws Exception {
		BasicDataSource dataSource = new BasicDataSource();
		String appName = env.getProperty("application.name");
		dataSource.setDriverClassName(env.getProperty(appName + ".driverClassName"));
		dataSource.setUrl(env.getProperty(appName + ".databaseurl"));
		dataSource.setUsername(env.getProperty(appName + ".username"));
		dataSource.setPassword(AES.decrypt(env.getProperty(appName + ".password")));
		return dataSource;
	}

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean getSessionFactory(@Qualifier("basicDataSource") DataSource dataSource) {
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(dataSource);
		localSessionFactoryBean.setAnnotatedClasses(PreDefineReport.class,ReportQueueData.class, QueryData.class, ViewInfo.class, ApplicationLabel.class, TellerMaster.class, RequiredField.class, AuditHistory.class, QueryData.class, Banks.class);
		localSessionFactoryBean.setHibernateProperties(getHibernateProperties());
		return localSessionFactoryBean;
	}

	/* ---------end of session factory for prod report ------------- */
	protected Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.dialect", env.getProperty("Banc-Edge.dialect"));
		properties.put("hibernate.c3p0.min_size", 1);
		properties.put("hibernate.c3p0.max_size", 300);
		properties.put("hibernate.c3p0.timeout", 50);
		properties.put("hibernate.c3p0.max_statements", 10);
		properties.put("hibernate.c3p0.hibernate.c3p0.idle_test_period", 30);
		return properties;
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}

}
