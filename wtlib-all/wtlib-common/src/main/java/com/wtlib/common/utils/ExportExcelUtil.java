package com.wtlib.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * ClassName: ExportExcelUtil
 * 
 * @Description: 导出excel操作工具类
 * @author zongzi
 * @date 2016年5月11日 下午6:10:23
 */
public class ExportExcelUtil {

	/**
	 * @Description: 根据路径读取excel模板
	 * @param @param filepath
	 * @param @return
	 * @param @throws Exception
	 * @author dapengniao
	 * @date 2016年5月11日 下午6:15:38
	 */
	public static HSSFWorkbook ReadExcelTemple(String filepath)
			throws Exception {
		// excel模板路径
		URL resource = ExportExcelUtil.class.getResource(filepath);
		File fi = new File(resource.getFile());
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fi));
		// 读取excel模板
		Workbook book = null;
		try {
			book = new XSSFWorkbook(fi);
		} catch (Exception ex) {
			book = new HSSFWorkbook(fs);
		}
		return (HSSFWorkbook) book;
	}

	/**
	 * 传入trades 将数据批量导出到excel表格中
	 * 
	 * @Description:
	 * @param @param trades
	 * @author zongzi
	 * @date 2016年8月8日 下午4:52:52
	 */
	/*
	 * public static boolean ExportTradeExcel(List<TradeDto> trades, String
	 * path) throws Exception {
	 * 
	 * HSSFWorkbook wb = ReadExcelTemple("//" +
	 * GlobalConstants.getString("export.trade.temple"));// 读取模板
	 * 
	 * HSSFSheet sheet = wb.getSheetAt(0);// 获得第一张工作表 int rowNum =
	 * sheet.getPhysicalNumberOfRows();// 获得工作表已经有的行数 int i = 0; // 遍历循环
	 * 输出每一个order的数据 for (TradeDto tradeDto : trades) {
	 * 
	 * List<OrderDto> orderDtoS = tradeDto.getOrderDtoS(); // 地址快照 SAddress
	 * sAddress = tradeDto.getsAddress(); Trade trade = tradeDto.getTrade(); for
	 * (OrderDto orderDto : orderDtoS) {
	 * 
	 * HSSFRow createRow = sheet.createRow(rowNum + i); i++; // 产品快照 SProductDto
	 * sProductDto = orderDto.getsProductDto(); Order order =
	 * orderDto.getOrder();
	 * 
	 * HSSFCell createCell = createRow.createCell(0);
	 * createCell.setCellValue(trade.getTradeNumber());// 添加交易流水号
	 * 
	 * HSSFCell createCell2 = createRow.createCell(1);
	 * createCell2.setCellValue(order.getOrderNumber());// 获得订单的流水号
	 * 
	 * HSSFCell createCell3 = createRow.createCell(2);
	 * createCell3.setCellValue(OrderStatusEnum.fromCode(
	 * order.getOrderStatus()).getValue());// 添加交易状态 // 读取枚举文件的valu HSSFCell
	 * createCell4 = createRow.createCell(3);
	 * createCell4.setCellValue(tradeDto.getSexual());//
	 * 
	 * HSSFCell createCell5 = createRow.createCell(4);
	 * createCell5.setCellValue("是");// 是否买家 默认为是
	 * 
	 * HSSFCell createCell6 = createRow.createCell(5);
	 * createCell6.setCellValue(trade.getPayAmount().toString());// 总金额
	 * 
	 * HSSFCell createCell7 = createRow.createCell(6);
	 * createCell7.setCellValue(PayTypeEnum.fromCode(
	 * trade.getPayPlatform()).getDescription());// 获得购买方式
	 * 
	 * HSSFCell createCell8 = createRow.createCell(7); // 处理sku的json数据 String
	 * productPropKey = sProductDto.getProductPropKey(); String skuString =
	 * SkuStringUtil.toSkuString(productPropKey);
	 * 
	 * createCell8.setCellValue(skuString);// 获得sku的值
	 * 
	 * HSSFCell createCell9 = createRow.createCell(8);
	 * 
	 * createCell9.setCellValue(sProductDto.getProductAlias());// 渠道商产品id
	 * 
	 * HSSFCell createCell10 = createRow.createCell(9);
	 * createCell10.setCellValue(orderDto.getChannelName());// 渠道商名称
	 * 
	 * HSSFCell createCell11 = createRow.createCell(10);
	 * createCell11.setCellValue(sAddress.getName());// 设置收件人姓名
	 * 
	 * HSSFCell createCell12 = createRow.createCell(11);
	 * 
	 * String sAddressDetail = new StringBuffer()
	 * .append(sAddress.getProvince()) .append(sAddress.getCity())
	 * .append(StringUtils.isEmpty(sAddress.getCounty()) ? "" :
	 * sAddress.getCounty()) .append(sAddress.getAddressDetail()).toString();
	 * createCell12.setCellValue(sAddressDetail);// 设置地址详细信息
	 * 
	 * HSSFCell createCell13 = createRow.createCell(12);
	 * createCell13.setCellValue(order.getLogisticsType());// 获得物流类型
	 * 
	 * HSSFCell createCell14 = createRow.createCell(13);
	 * createCell14.setCellValue(sAddress.getPhone());// 获得收货人的联系电话
	 * 
	 * HSSFCell createCell15 = createRow.createCell(14);
	 * createCell15.setCellValue(DateUtils.getFormatDate( "yyyy-MM-dd HH:mm:ss",
	 * trade.getCreateTime()));// 获得订单创建时间
	 * 
	 * HSSFCell createCell16 = createRow.createCell(15);
	 * createCell16.setCellValue(order.getProductNum());// 获得产品数量
	 * 
	 * HSSFCell createCell17 = createRow.createCell(16);
	 * createCell17.setCellValue(trade.getMpAlias());// 获得店铺id
	 * 
	 * HSSFCell createCell18 = createRow.createCell(17);
	 * createCell18.setCellValue(tradeDto.getMpName());// 获得店铺的名称
	 * 
	 * HSSFCell createCell19 = createRow.createCell(18);
	 * createCell19.setCellValue(trade.getBuyerRemark());// 获得订单留言
	 * 
	 * HSSFCell createCell20 = createRow.createCell(19);
	 * createCell20.setCellValue(tradeDto.getTrade().getPayNumber()); } }
	 * FileOutputStream out = new FileOutputStream(path); // 生成文件 wb.write(out);
	 * out.close(); return true;
	 * 
	 * }
	 */

	/*
	 * public static boolean ExportTradeExcelList(List<List<String>> lists,
	 * String realPath) throws Exception {
	 * 
	 * try { HSSFWorkbook wb = ReadExcelTemple("//" +
	 * GlobalConstants.getString("import.order.errortemple")); HSSFSheet sheet =
	 * wb.getSheetAt(0); int rowNum = sheet.getPhysicalNumberOfRows();//
	 * 获得工作表已经有的行数 int i = 0; for (List<String> list : lists) { int startRow =
	 * rowNum + i; i++; HSSFRow createRow = sheet.createRow(startRow); int j =
	 * 0; for (String String : list) { HSSFCell createCell =
	 * createRow.createCell(j); createCell.setCellValue(String); j++; } }
	 * FileOutputStream out = new FileOutputStream(realPath); // 生成文件
	 * wb.write(out); out.close(); return true;
	 * 
	 * } catch (Exception e) { throw e; }// 读取模板 }
	 * 
	 * public static boolean ExportTradeExcel(List<TradeExportDto> trades,
	 * Map<Integer, Product> products, Map<String, String> mpInfo, String path,
	 * String type) throws Exception { HSSFWorkbook wb;
	 * 
	 * try { wb = ReadExcelTemple("//" +
	 * GlobalConstants.getString("export.trade.temple"));
	 * 
	 * HSSFSheet sheet = wb.getSheetAt(0);// 获得第一张工作表 int rowNum =
	 * sheet.getPhysicalNumberOfRows();// 获得工作表已经有的行数 int i = 0; String
	 * beforeTradeNumber = new String(); StringBuffer productInfo = new
	 * StringBuffer();
	 * 
	 * int beforRowNumberNeedCombine = 0;// 之前需要合并的行数
	 * 
	 * for (TradeExportDto tradeExportDto : trades) { boolean
	 * isThisCombineBeforLine = false; String currentTradeNumber =
	 * tradeExportDto.getTradeNumber();
	 * 
	 * if (currentTradeNumber.equals(beforeTradeNumber)) {
	 * isThisCombineBeforLine = true;// 代表这一行需要和上一行进行合并
	 * beforRowNumberNeedCombine++; } else { isThisCombineBeforLine = false;
	 * productInfo = new StringBuffer();// 如果不需要合并，就需要一个新的对象存储合并字符串 }
	 * 
	 * int currentRow = rowNum + i;// 当前行
	 * 
	 * HSSFRow createRow = sheet.createRow(currentRow);
	 * 
	 * HSSFCell createCell = createRow.createCell(0);
	 * createCell.setCellValue(tradeExportDto.getTradeNumber());// 添加交易流水号
	 * 
	 * HSSFCell createCell2 = createRow.createCell(1);
	 * createCell2.setCellValue(PayTypeEnum.fromCode(
	 * tradeExportDto.getPayPlatform()).getDescription());// 获得购买方式
	 * 
	 * HSSFCell createCell3 = createRow.createCell(2);
	 * createCell3.setCellValue(tradeExportDto.getPayAmount() .toString());//
	 * 总金额
	 * 
	 * // 处理sku的json数据 String productPropKey =
	 * tradeExportDto.getProductPropKey(); String skuString =
	 * SkuStringUtil.toSkuString(productPropKey);
	 * 
	 * String productAlias = tradeExportDto.getProductAlias(); String productNum
	 * = tradeExportDto.getProductNum().toString();
	 * 
	 * StringBuffer SKUInfo = new StringBuffer().append(productAlias)
	 * .append("【" + skuString + "】") .append("(" + productNum + ")");
	 * productInfo.append(SKUInfo).append("+");
	 * 
	 * HSSFCell createCell4 = createRow.createCell(3); // 如果是单行 的不需要最后的加号 if
	 * (isThisCombineBeforLine) {
	 * createCell4.setCellValue(productInfo.toString());// 商品信息 } else {
	 * createCell4.setCellValue(SKUInfo.toString());// 单个商品的信息去掉 }
	 * 
	 * HSSFCell createCell5 = createRow.createCell(4);
	 * createCell5.setCellValue(tradeExportDto.getName());// 设置收件人姓名
	 * 
	 * HSSFCell createCell6 = createRow.createCell(5);
	 * createCell6.setCellValue(tradeExportDto.getPhone());// 设置收件人手机
	 * 
	 * String sAddressDetail = new StringBuffer()
	 * .append(tradeExportDto.getProvince()) .append(" ")
	 * .append(tradeExportDto.getCity()) .append(" ")
	 * .append(StringUtils.isEmpty(tradeExportDto.getCounty()) ? "" :
	 * tradeExportDto.getCounty()).append(" ")
	 * .append(tradeExportDto.getAddressDetail()).toString(); HSSFCell
	 * createCell7 = createRow.createCell(6);
	 * createCell7.setCellValue(sAddressDetail);// 设置收件人收货地址
	 * 
	 * HSSFCell createCell8 = createRow.createCell(7);
	 * createCell8.setCellValue(tradeExportDto.getMpUserRemark());// 获得卖家备注
	 * 
	 * HSSFCell createCell9 = createRow.createCell(8);
	 * createCell9.setCellValue(tradeExportDto.getOrderNumber());// 获得子订单的流水号
	 * 
	 * HSSFCell createCell10 = createRow.createCell(9);
	 * createCell10.setCellValue(OrderStatusEnum.fromCode(
	 * tradeExportDto.getOrderStatus()).getValue());// 添加交易状态
	 * 
	 * // 保存子订单金额 HSSFCell createCell11 = createRow.createCell(10);
	 * createCell11.setCellValue(tradeExportDto .getOrderPrice() .multiply( new
	 * BigDecimal(tradeExportDto.getProductNum())) .setScale(2).toString());
	 * 
	 * HSSFCell createCell12 = createRow.createCell(11);
	 * createCell12.setCellValue(skuString);// sku描述值
	 * 
	 * HSSFCell createCell13 = createRow.createCell(12);
	 * createCell13.setCellValue(SKUInfo.toString());// sku描述值
	 * 
	 * HSSFCell createCell14 = createRow.createCell(13);
	 * createCell14.setCellValue(productAlias);// 获取商家编码
	 * 
	 * Product product = products.get(tradeExportDto.getProductId());
	 * 
	 * HSSFCell createCell15 = createRow.createCell(14);// 渠道商名称 if (product !=
	 * null) { createCell15.setCellValue(product.getChannelName()); } else {
	 * createCell15.setCellValue("");// 渠道商名称 }
	 * 
	 * HSSFCell createCell16 = createRow.createCell(15);
	 * createCell16.setCellValue(DateUtils.getFormatDate( "yyyy-MM-dd HH:mm:ss",
	 * tradeExportDto.getCreateTime()));// 订单创建时间
	 * 
	 * HSSFCell createCell17 = createRow.createCell(16);
	 * createCell17.setCellValue(tradeExportDto.getProductNum());// 宝贝数量
	 * 
	 * HSSFCell createCell18 = createRow.createCell(17);
	 * createCell18.setCellValue(tradeExportDto.getMpAlias());// 获得店铺id
	 * 
	 * HSSFCell createCell19 = createRow.createCell(18);
	 * createCell19.setCellValue(mpInfo == null ? "" : mpInfo .get("mpName"));//
	 * 这里不需要修改
	 * 
	 * HSSFCell createCell20 = createRow.createCell(19);
	 * createCell20.setCellValue(tradeExportDto.getBuyerRemark());// 订单留言
	 * 
	 * HSSFCell createCell21 = createRow.createCell(20);
	 * createCell21.setCellValue(tradeExportDto.getPayNumber());// 支付流水号
	 * 
	 * // 导出客服人员的备注 HSSFCell createCell22 = createRow.createCell(21);
	 * createCell22 .setCellValue(tradeExportDto.getOrderMpUserRemark());
	 * 
	 * HSSFCell createCell23 = createRow.createCell(22); if (product != null) {
	 * String url = product.getImportUrl();// daor
	 * createCell23.setCellValue(url); if (StringUtils.isNotEmpty(url)) { if
	 * (url.contains("www.hznzcn.com")) {// 是否是杭走女装网 // 获得产品的货号 HSSFCell
	 * createCell24 = createRow.createCell(23);
	 * createCell24.setCellValue(URLUtils .getProductCodeInUrl(url)); // 获得产品颜色
	 * HSSFCell createCell25 = createRow.createCell(24);
	 * createCell25.setCellValue(SkuStringUtil .getKeyString("颜色",
	 * productPropKey)); // 尺寸 HSSFCell createCell26 = createRow.createCell(25);
	 * createCell26.setCellValue(SkuStringUtil .getKeyString("尺码尺寸",
	 * productPropKey));
	 * 
	 * } } } else { createCell23.setCellValue(""); } // 如果当前行和前面的行数不一样，
	 * 而且有需要合并的行 将前面的行合并
	 * 
	 * boolean isEndBeforeRepeat = false; if ((!isThisCombineBeforLine &&
	 * beforRowNumberNeedCombine != 0) || (isEndBeforeRepeat = (currentRow ==
	 * trades.size() && beforRowNumberNeedCombine != 0))) {
	 * 
	 * int startCombineRow = currentRow - beforRowNumberNeedCombine - 1;
	 * 
	 * int endCombineRow = currentRow - 1;
	 * 
	 * if (isEndBeforeRepeat) {// 如果是最后一行和前面的重复 那么就需要把最后一行加上 startCombineRow++;
	 * endCombineRow++; } // 赋值完整商品信息 HSSFRow lastRepeatRow =
	 * sheet.getRow(endCombineRow); StringBuffer completeSkuInfo = new
	 * StringBuffer( lastRepeatRow.getCell(3).getStringCellValue());
	 * completeSkuInfo.deleteCharAt(completeSkuInfo.length() - 1);// 去掉最后一个+号
	 * for (int j = endCombineRow; j != startCombineRow - 1; j--) { HSSFRow
	 * lastDidNotCombineRow = sheet.getRow(j); // 将最后一行的值赋值给所有的合并单元格
	 * lastDidNotCombineRow.getCell(3).setCellValue(
	 * completeSkuInfo.toString()); }
	 * 
	 * // 合并tradeNumber列 sheet.addMergedRegion(new
	 * CellRangeAddress(startCombineRow, endCombineRow, 0, 0));
	 * 
	 * // 合并购买方式 sheet.addMergedRegion(new CellRangeAddress(startCombineRow,
	 * endCombineRow, 1, 1)); // 订单金额 sheet.addMergedRegion(new
	 * CellRangeAddress(startCombineRow, endCombineRow, 2, 2)); // 产品数量详情
	 * sheet.addMergedRegion(new CellRangeAddress(startCombineRow,
	 * endCombineRow, 3, 3)); // 收货人 sheet.addMergedRegion(new
	 * CellRangeAddress(startCombineRow, endCombineRow, 4, 4)); // 收货人联系手机
	 * sheet.addMergedRegion(new CellRangeAddress(startCombineRow,
	 * endCombineRow, 5, 5)); // 收货地址 sheet.addMergedRegion(new
	 * CellRangeAddress(startCombineRow, endCombineRow, 6, 6)); // 用户的备注
	 * sheet.addMergedRegion(new CellRangeAddress(startCombineRow,
	 * endCombineRow, 7, 7)); beforRowNumberNeedCombine = 0;// 合并之后待合并的就是0
	 * productInfo = new StringBuffer();// 合并之后字符串也是空 } else { if
	 * (!isThisCombineBeforLine) { beforRowNumberNeedCombine = 0;//
	 * 如果这一行不和之前的行合并，那么待合并的行数就是0 }
	 * 
	 * }
	 * 
	 * beforeTradeNumber = currentTradeNumber;// 更换前后的tradeNumber i++;
	 * 
	 * } FileOutputStream out = new FileOutputStream(path); // 生成文件
	 * wb.write(out); out.close(); return true;
	 * 
	 * } catch (Exception e) {
	 * 
	 * throw e; }// 读取模板
	 * 
	 * }
	 * 
	 * public static void export(HttpServletResponse response, Workbook wb,
	 * String fileName) throws Exception {
	 * response.setHeader("Content-Disposition", "attachment;filename=" + new
	 * String(fileName.getBytes("utf-8"), "iso8859-1"));
	 * response.setContentType("application/ynd.ms-excel;charset=UTF-8");
	 * OutputStream out = response.getOutputStream(); wb.write(out);
	 * out.flush(); out.close(); }
	 */
}
