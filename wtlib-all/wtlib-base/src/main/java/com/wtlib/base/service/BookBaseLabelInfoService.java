package com.wtlib.base.service;


import com.wtlib.base.pojo.BookBaseLabelInfo;

public interface BookBaseLabelInfoService extends BaseService<BookBaseLabelInfo> {

	void deleteByLabelId(Object labelId,Object reviser);

}
