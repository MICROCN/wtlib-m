package com.wtlib.base.service.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dao.LabelInfoMapper;
import com.wtlib.base.pojo.BookBaseLabelInfo;
import com.wtlib.base.pojo.LabelInfo;
import com.wtlib.base.service.BookBaseLabelInfoService;
import com.wtlib.base.service.LabelInfoService;

@Service("labelInfoService")
public class LabelInfoServiceImpl implements LabelInfoService {

	@Autowired
	LabelInfoMapper labelInfoMapper;

	@Autowired
	BookBaseLabelInfoService bookBaseLabelInfoService;

	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
		int labelId = labelInfoMapper.deleteById(id,reviser);
		bookBaseLabelInfoService.deleteByLabelId(labelId,reviser);
		return labelId;
	}

	@Override
	public List<LabelInfo> selectByBaseId(Integer id,String dataStatus) {
		return labelInfoMapper.selectByBaseId(id,dataStatus);
	}
	
	@Override
	public int insertBatch(List<LabelInfo> entityList) throws Exception {
		return 0;
	}

	@Override
	public LabelInfo selectById(Object id,String dataStatus) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LabelInfo> selectAll(String dataStatus) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(LabelInfo entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LabelInfo find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insert(LabelInfo info, Integer infoId) throws Exception {
		// 将信息写入LabelInfo表
		Integer id = labelInfoMapper.insert(info);
		Integer userId = info.getUserId();
		// 将信息写入关联表LabelInfoUserId
		BookBaseLabelInfo bookLabel = new BookBaseLabelInfo(id, infoId);
		int num = bookBaseLabelInfoService.insert(bookLabel);
		return num;
	}

	@Override
	public Integer insert(LabelInfo entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
