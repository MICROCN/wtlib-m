package com.test.component;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import net.bytebuddy.asm.Advice.This;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.dbunit.dataset.AbstractTable;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.RowOutOfBoundsException;
import org.dbunit.dataset.excel.XlsDataSet;
import org.springframework.util.CollectionUtils;
import org.unitils.core.UnitilsException;
import org.unitils.dbunit.util.MultiSchemaDataSet;

import com.wtlib.common.utils.ExeclUtil;
import com.wtlib.common.utils.WTStringUtils;


/**
 * ClassName: MultiSchemaXlsDataSetReader 读取xls文件
 * 
 * @author zongzi
 * @date 2017年2月4日 下午2:17:36
 */
public class MultiSchemaXlsDataSetReader {

	private static final Log logger = LogFactory.getLog(This.class);

	private String defaultSchemaName;

	public MultiSchemaXlsDataSetReader(String defaultSchemaName) {
		this.defaultSchemaName = defaultSchemaName;
	}

	public MultiSchemaDataSet readDataSetXls(File... dataSetFiles) {
		try {
			Map<String, List<ITable>> tableMap = getTables(dataSetFiles);
			MultiSchemaDataSet dataSets = new MultiSchemaDataSet();
			for (Entry<String, List<ITable>> entry : tableMap.entrySet()) {
				List<ITable> tables = entry.getValue();
				try {
					DefaultDataSet ds = new DefaultDataSet(
							tables.toArray(new ITable[] {}));
					dataSets.setDataSetForSchema(entry.getKey(), ds);
				} catch (Exception e) {
					throw new UnitilsException("构造DataSet失败!", e);
				}
			}
			logger.info("创建数据集<成功>:" + Arrays.toString(dataSetFiles));
			return dataSets;

		} catch (Exception e) {
			throw new UnitilsException("解析EXCEL文件出错:", e);
		}
	}

	private Map<String, List<ITable>> getTables(File[] dataSetFiles) {
		Pattern pattern = Pattern.compile("\\.");
		Map<String, List<ITable>> tableMap = new HashMap<String, List<ITable>>();
		try {
			for (File file : dataSetFiles) {
				logger.info("开始加载xls文件--> {" + file.getCanonicalPath() + "}");
				IDataSet dataSet = new XlsDataSet(new FileInputStream(file));
				Workbook wb = new ExeclUtil()
						.getWorkBookByFile(new FileInputStream(file));
				String[] tableNames = dataSet.getTableNames();
				for (String each : tableNames) {
					String schema = null;
					String tableName;
					String[] temp = pattern.split(each);
					if (temp.length == 2) {
						schema = temp[0];
						tableName = temp[1];
					} else {
						schema = this.defaultSchemaName;
						tableName = each;
					}
					ITable table = dataSet.getTable(each);
					if (!tableMap.containsKey(schema)) {
						tableMap.put(schema, new ArrayList<ITable>());
					}
					tableMap.get(schema)
							.add(new XlsTable(tableName, table, wb));

				}
				logger.info("结束加载xls文件--> {" + file.getCanonicalPath() + "}");

			}
		} catch (Exception e) {
			throw new UnitilsException("创建数据集失败:"
					+ Arrays.toString(dataSetFiles), e);
		}
		return tableMap;
	}

	class XlsTable extends AbstractTable {

		private ITable delegate;
		private String tableName;
		private Workbook wb;
		List<Map<String, Object>> read;

		public XlsTable(String tableName, ITable table) {
			this.delegate = table;
			this.tableName = tableName;
			this.read = new ExeclUtil().read(wb, tableName);
		}

		public XlsTable(String tableName, ITable table, Workbook wb) {
			this.delegate = table;
			this.tableName = tableName;
			this.read = new ExeclUtil().read(wb, tableName);
		}

		@Override
		public int getRowCount() {
			if (CollectionUtils.isEmpty(read) || null == read) {
				return 0;
			} else {
				return read.size();
			}
		}

		@Override
		public ITableMetaData getTableMetaData() {
			ITableMetaData meta = delegate.getTableMetaData();
			try {
				return new DefaultTableMetaData(tableName, meta.getColumns(),
						meta.getPrimaryKeys());
			} catch (Exception e) {
				throw new UnitilsException("Can't get the meta info from "
						+ meta, e);
			}
		}

		@Override
		public Object getValue(int row, String column)// 会在规定的策略里被执行
														// 如果到了结尾的话需要返回
														// RowOutOfBoundsException
														// 才能阻止继续添加
				throws RowOutOfBoundsException {

			if (CollectionUtils.isEmpty(read) || null == read) {
				throw new RowOutOfBoundsException("current row=" + row
						+ ",read's null ");
			}
			if (row >= read.size()) {
				throw new RowOutOfBoundsException("current row=" + row
						+ ",read's size=" + read.size());
			}
			column = WTStringUtils.underlineToCamel(column);
			Object delta = read.get(row).get(column);
			if (delta instanceof String) {
				return delta;
			}

			return null;

		}

	}
}
