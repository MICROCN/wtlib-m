package com.wtlib.base.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wtlib.base.pojo.LabelInfo;

public interface LabelInfoService extends BaseService<LabelInfo> {

	List<LabelInfo> selectByBaseId(Integer id,String dataStatus);


}
