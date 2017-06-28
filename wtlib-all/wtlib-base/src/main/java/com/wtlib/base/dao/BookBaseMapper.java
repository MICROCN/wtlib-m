package com.wtlib.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wtlib.base.dto.TotalInfoDto;
import com.wtlib.base.pojo.BookBase;

/**
 * @author zongzi
 * @date 2017年1月21日 下午6:16:26
 */
public interface BookBaseMapper extends BaseDao<BookBase> {

	List<BookBase> findByTitle(@Param("title") String title,@Param("dataStatus") String dataStatus);

	BookBase find(@Param("book") BookBase entity,@Param("dataStatus")String dataStatus);

	TotalInfoDto selectTotal();

}