package com.wtlib.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wtlib.base.dto.RecordDto;
import com.wtlib.base.pojo.BorrowRecord;

/**
 * @author zongzi
 * @date 2017年1月21日 下午6:17:13
 */
public interface BorrowRecordMapper extends BaseDao<BorrowRecord> {

	Integer findRecord(@Param("record") BorrowRecord record,@Param("dataStatus") String dataStatus);

	BorrowRecord selectBySingleId(@Param("singleId")Integer singleId,@Param("dataStatus") String dataStatus);

	List<BorrowRecord> selectAllByUserId(@Param("id")Object id, String code,@Param("dataStatus") String dataStatus);

	RecordDto selectRecord();
	
}