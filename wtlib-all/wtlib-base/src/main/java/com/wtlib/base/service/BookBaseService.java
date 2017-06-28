package com.wtlib.base.service;

import java.util.List;

import com.wtlib.base.dto.TotalInfoDto;
import com.wtlib.base.pojo.BookBase;

/**
 * ClassName: BookBaseService
 * @Description: 基础图书服务类接口
 * @author pohoulong
 * @date 2017年1月22日 下午1:44:46
 */
public interface BookBaseService extends BaseService<BookBase> {
	List<BookBase> find(String title) throws Exception;

	void deleteByBaseId(Integer id,Object reviser) throws Exception;

	TotalInfoDto selectTotal();

}
