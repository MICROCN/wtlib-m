package com.wtlib.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * ClassName: ExeclUtil
 * 
 * @Description: excel操作
 * @author dapengniao
 * @date 2016年5月9日 下午3:53:39
 */
/**
 * ClassName: ExeclUtil
 * 
 * @Description: TODO
 * @author zongzi
 * @date 2017年2月17日 下午2:47:05
 */
public class ExeclUtil {
	/** 总行数 */
	private int totalRows = 0;
	/** 总列数 */
	private int totalCells = 0;
	/** 错误信息 */
	private String errorInfo;

	/** 构造方法 */
	public ExeclUtil() {

	}

	/**
	 * @Description: 得到总行数
	 * @param @return
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:51:45
	 */
	public int getTotalRows() {

		return totalRows;

	}

	/**
	 * @Description: 得到总列数
	 * @param @return
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:52:17
	 */
	public int getTotalCells() {

		return totalCells;

	}

	/**
	 * @Description: 得到错误信息
	 * @param @return
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:52:34
	 */
	public String getErrorInfo() {

		return errorInfo;

	}

	/**
	 * @Description: 验证excel文件
	 * @param @param filePath
	 * @param @return
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:53:27
	 */
	public boolean validateExcel(String filePath) {
		/** 检查文件名是否为空或者是否是Excel格式的文件 */
		if (filePath == null
				|| !(WDWUtil.isExcel2003(filePath) || WDWUtil
						.isExcel2007(filePath))) {

			errorInfo = "文件名不是excel格式";
			return false;
		}

		/** 检查文件是否存在 */
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			errorInfo = "文件不存在";
			return false;
		}
		return true;
	}

	/**
	 * @Description: 根据路径名返回wb
	 * 
	 * @param @param filePath
	 * @param @return
	 * @author zongzi
	 * @throws Exception
	 * @date 2017年2月17日 下午2:47:11
	 */
	public Workbook getWorkBookByFile(InputStream filePath) throws Exception {
		try {

			return new HSSFWorkbook(filePath);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Description: 根据sheet名字读取表格
	 * @param @param in
	 * @param @param tableName
	 * @param @param isExcel2003
	 * @param @return
	 * @author zongzi
	 * @date 2017年2月17日 下午2:47:49
	 */
	public List<Map<String, Object>> read(InputStream in, String tableName,
			Boolean isExcel2003) {

		List<Map<String, Object>> dataLst = null;
		try {
			/** 根据版本选择创建Workbook的方式 */
			Workbook wb = null;
			if (isExcel2003) {
				wb = new HSSFWorkbook(in);
			} else {
				wb = new XSSFWorkbook(in);
			}
			dataLst = read(wb, tableName);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

		return dataLst;
	}

	/**
	 * @Description: 根据文件名读取excel文件
	 * @param @param filePath
	 * @param @return
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:54:38
	 */
	public List<List<String>> read(String filePath) {
		List<List<String>> dataLst = new ArrayList<List<String>>();
		InputStream is = null;
		try {
			/** 验证文件是否合法 */
			if (!validateExcel(filePath)) {
				System.out.println(errorInfo);
				return null;
			}
			/** 判断文件的类型，是2003还是2007 */
			boolean isExcel2003 = true;
			if (WDWUtil.isExcel2007(filePath)) {
				isExcel2003 = false;
			}
			/** 调用本类提供的根据流读取的方法 */
			File file = new File(filePath);
			is = new FileInputStream(file);
			dataLst = read(is, isExcel2003);
			is.close();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					is = null;
				}
			}
		}
		/** 返回最后读取的结果 */
		return dataLst;
	}

	/**
	 * @Description: 根据流读取Excel文件
	 * @param @param inputStream
	 * @param @param isExcel2003
	 * @param @return
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:55:41
	 */
	public List<List<String>> read(InputStream inputStream, boolean isExcel2003) {
		List<List<String>> dataLst = null;
		try {
			/** 根据版本选择创建Workbook的方式 */
			Workbook wb = null;
			if (isExcel2003) {
				wb = new HSSFWorkbook(inputStream);
			} else {
				wb = new XSSFWorkbook(inputStream);
			}
			dataLst = read(wb);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return dataLst;
	}

	/**
	 * @Description: 读取数据
	 * @param @param wb
	 * @param @return
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:56:19
	 */
	private List<List<String>> read(Workbook wb) {
		List<List<String>> dataLst = new ArrayList<List<String>>();
		/** 得到第一个sheet */
		Sheet sheet = wb.getSheetAt(0);
		/** 得到Excel的行数 */
		this.totalRows = sheet.getPhysicalNumberOfRows();
		/** 得到Excel的列数 */
		if (this.totalRows >= 1 && sheet.getRow(0) != null) {
			this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		/** 循环Excel的行 */
		for (int r = 0; r < this.totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			List<String> rowLst = new ArrayList<String>();
			/** 循环Excel的列 */
			int nullNumber = 0;// 记录列中空值的数量 当数量的值是列数时不添加
			for (int c = 0; c < this.getTotalCells(); c++) {
				Cell cell = row.getCell(c);
				String cellValue = "";
				if (null != cell) {
					// 以下是判断数据的类型
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC: // 数字
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							SimpleDateFormat dateformat = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date dt = HSSFDateUtil.getJavaDate(cell
									.getNumericCellValue());
							cellValue = dateformat.format(dt);
							break;
						}
						DecimalFormat df = new DecimalFormat("#");
						cellValue = df.format(cell.getNumericCellValue()) + "";

						break;
					case HSSFCell.CELL_TYPE_STRING: // 字符串
						cellValue = cell.getStringCellValue().trim();
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						cellValue = cell.getBooleanCellValue() + "";
						break;
					case HSSFCell.CELL_TYPE_FORMULA: // 公式
						cellValue = cell.getCellFormula() + "";
						break;
					case HSSFCell.CELL_TYPE_BLANK: // 空值
						cellValue = "";
						nullNumber++;
						break;
					case HSSFCell.CELL_TYPE_ERROR: // 故障
						cellValue = "非法字符";
						break;
					default:
						cellValue = "未知类型";
						break;
					}
				} else {
					nullNumber++;
				}
				rowLst.add(cellValue);
			}
			/** 保存第r行的第c列 只有当 行中的空值列数小于行中的列的总行数时 才进行保存这一行 */
			if (rowLst.size() > nullNumber) {
				dataLst.add(rowLst);
			}
		}
		return dataLst;
	}

	/**
	 * @Description:根据tableName来产生一个表的所有记录
	 * 
	 * @param @param wb
	 * @param @param tableName
	 * @param @return
	 * @author zongzi
	 * @date 2017年2月17日 下午1:59:06
	 */
	public List<Map<String, Object>> read(Workbook wb, String tableName) {
		List<Map<String, Object>> dataLst = new ArrayList<Map<String, Object>>();
		List<String> columNames = new ArrayList<String>();// 列名
		/** 得到name指定的sheet */
		Sheet sheet = wb.getSheetAt(wb.getSheetIndex(tableName));
		/** 得到Excel的行数 */
		this.totalRows = sheet.getPhysicalNumberOfRows();
		// ------totalRows----
		System.out.println(totalRows + "tableName----" + tableName
				+ "------------->");
		// --------------
		/** 得到Excel的列数 以及列名列表 */
		if (this.totalRows >= 1 && sheet.getRow(0) != null) {
			this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
			for (int i = 0; i < this.getTotalCells(); i++) {
				String columName = sheet.getRow(0).getCell(i)
						.getStringCellValue();
				if (StringUtils.isEmpty(columName)) {
					this.totalCells--;
					continue;// 列名为空 跳过
				}
				columNames.add(WTStringUtils.underlineToCamel(columName));
			}
		}

		/** 循环Excel的行 */
		for (int r = 1; r < this.totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			Map<String, Object> props = new HashMap<String, Object>();
			boolean notNullFlag = false;
			for (int c = 0; c < row.getPhysicalNumberOfCells(); c++) {
				String columName = columNames.get(c);// 获得列名
				Cell cell = row.getCell(c);
				Object cellValue = getCellValue(cell);
				
				if (null != cellValue) {
					notNullFlag = true;
				}
				props.put(columName, cellValue);
			}
			if(notNullFlag){
				dataLst.add(props);
			}
		}
		return dataLst;
	}

	/**
	 * @Description: 获得cell之中的数据内容
	 * @param @param cell
	 * @param @return
	 * @author zongzi
	 * @date 2017年2月17日 下午3:09:08
	 */
	private Object getCellValue(Cell cell) {
		Object cellValue = null;
		if (cell != null) {
			// 以下是判断数据的类型
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC: // 数字
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date dt = HSSFDateUtil.getJavaDate(cell
							.getNumericCellValue());
					cellValue = dateformat.format(dt);
					break;
				}
				DecimalFormat df = new DecimalFormat("#");
				cellValue = df.format(cell.getNumericCellValue()) + "";

				break;
			case HSSFCell.CELL_TYPE_STRING: // 字符串
				cellValue = cell.getStringCellValue().trim();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				cellValue = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_FORMULA: // 公式
				cellValue = cell.getCellFormula();
				break;
			case HSSFCell.CELL_TYPE_BLANK: // 空值
				cellValue = null;
				break;
			case HSSFCell.CELL_TYPE_ERROR: // 故障
				cellValue = "非法字符";
				break;
			default:
				cellValue = "未知类型";
				break;
			}
		} else {
			return null;
		}
		return cellValue;
	}

	/**
	 * @Description: main测试方法
	 * @param @param args
	 * @param @throws Exception
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:57:23
	 */
	public static void main(String[] args) throws Exception {
		ExeclUtil poi = new ExeclUtil();
		List<List<String>> list = poi
				.read("c://weidian/2016-05-19-13-33-124.21晚单子.xlsx");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				System.out.print("第" + (i) + "行");
				List<String> cellList = list.get(i);
				for (int j = 0; j < cellList.size(); j++) {
					System.out.print("    第" + (j + 1) + "列值：");
					System.out.print("    " + cellList.get(j));
				}
				System.out.println();
			}
			System.out.println("结束了");
		} else {
			System.out.println("空的");
		}

	}

}

/**
 * ClassName: WDWUtil
 * 
 * @Description: 工具类
 * @author dapengniao
 * @date 2016年4月23日 下午1:58:25
 */
class WDWUtil {
	/**
	 * @Description: 是否是2003的excel，返回true是2003
	 * @param @param filePath
	 * @param @return
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:58:44
	 */
	public static boolean isExcel2003(String filePath) {

		return filePath.matches("^.+\\.(?i)(xls)$");

	}

	/**
	 * @Description: 是否是2007的excel，返回true是2007
	 * @param @param filePath
	 * @param @return
	 * @author dapengniao
	 * @date 2016年4月23日 下午1:59:02
	 */
	public static boolean isExcel2007(String filePath) {

		return filePath.matches("^.+\\.(?i)(xlsx)$");

	}

}