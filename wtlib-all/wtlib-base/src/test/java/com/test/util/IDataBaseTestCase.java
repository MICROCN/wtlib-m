package com.test.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wtlib.base.start.InterfaceUrlInti;


public class IDataBaseTestCase extends DatabaseTestCase {

	private static ApplicationContext context;
	protected Properties props = new Properties();

	public IDataBaseTestCase() {
		super();
		InterfaceUrlInti.init();
		context = new ClassPathXmlApplicationContext(new String[] {
				"classpath:test-spring-mybatis.xml",
				"classpath:test-spring.xml", "classpath:test-spring-aop.xml" });

	}

	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		Connection connection = dataSource.getConnection();
		return new DatabaseConnection(connection);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSet(new FileInputStream("bin/dataSet.xml"));
	}

	@Override
	protected DatabaseOperation getSetUpOperation() throws Exception {
		return DatabaseOperation.REFRESH;
	}

	@Override
	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.NONE;
	}

}
