package com.wtlib.base.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wtlib.base.pojo.BookSingle;

/**
 * @author zongzi
 * @date 2017年1月21日 下午6:17:01
 */
public interface BookSingleMapper extends BaseDao<BookSingle> {
	public BookSingle findByHash(@Param("hash") String hash,@Param("dataStatus") String dataStatus);

	public BookSingle findById(@Param("id") Object id,@Param("dataStatus") String dataStatus);

	public void deleteByBaseId(@Param("id")Integer id,@Param("reviser") Integer reviser);
}