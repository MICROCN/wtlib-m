package com.wtlib.base.dao;

import org.apache.ibatis.annotations.Param;

import com.wtlib.base.pojo.BookBaseSupport;
import com.wtlib.base.pojo.BookSingle;

/**
 * ClassName: BookBaseSupportMapper
 * 
 * @Description: 图书扩展类
 * @author zongzi
 * @date 2017年2月15日 上午11:58:56
 */
public interface BookBaseSupportMapper extends BaseDao<BookBaseSupport> {

	public BookBaseSupport selectBookBaseSupportByBookBaseId(
			@Param("bookBaseId") Integer bookBaseId,
			@Param("dataStatus") String dataStatus);
	

	/**
	 * 根据bookbaseSupport的bookId来更新图书辅助表信息
	 * 
	 * @param @param bookBaseSupportTemp
	 * @param @return
	 * @author zongzi
	 * @date 2017年2月15日 下午1:56:11
	 */
	public Integer updateByBookId(BookBaseSupport bookBaseSupportTemp);


	public void deleteByBaseId(@Param("id")Integer id,@Param("reviser") Integer reviser);


}