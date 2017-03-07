package com.wtlib.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wtlib.base.pojo.BookBase;
import com.wtlib.base.pojo.LabelInfo;

public interface LabelInfoMapper extends BaseDao<LabelInfo>{

	List<LabelInfo> selectByBaseId(@Param("id")Integer id,@Param("dataStatus")String dataStatus);


}
