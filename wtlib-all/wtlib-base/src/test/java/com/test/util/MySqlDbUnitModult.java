package com.test.util;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.unitils.dbunit.DbUnitModule;
import org.unitils.dbunit.util.DbUnitDatabaseConnection;

/**
 * ClassName: MySqlDbUnitModult
 * 
 * @Description: 自定义mysql 的 dbunit 模块
 * @author zongzi
 * @date 2017年2月11日 上午10:00:36
 */
public class MySqlDbUnitModult extends DbUnitModule {

	public DbUnitDatabaseConnection getDbUnitDatabaseConnection(
			final String schemaName) {
		DbUnitDatabaseConnection result = dbUnitDatabaseConnections
				.get(schemaName);
		if (null != result) {
			return result;
		}

		result = super.getDbUnitDatabaseConnection(schemaName);
		result.getConfig().setProperty(
				DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
				new MySqlDataTypeFactory());
		result.getConfig().setProperty(
				DatabaseConfig.PROPERTY_METADATA_HANDLER,
				new MySqlMetadataHandler());
		return result;
	}

}
