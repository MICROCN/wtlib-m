package com.wtlib.base.service;

import java.util.List;


import com.wtlib.base.dto.SupportWebDto;
import com.wtlib.base.pojo.BookBaseSupport;

public interface BookBaseSupportService extends BaseService<BookBaseSupport> {

	SupportWebDto selectByBaseId(Integer id) throws Exception;

	BookBaseSupport selectBookBaseSupportByBookBaseId(Integer id,String dataStatus);

	Integer updateByBookId(BookBaseSupport bookBaseSupportTemp);

}
