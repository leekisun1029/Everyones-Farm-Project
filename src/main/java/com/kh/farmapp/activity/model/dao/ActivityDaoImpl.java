package com.kh.farmapp.activity.model.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.dto.Application;
import common.dto.EveryonesFarmFile;
import common.dto.Farm;
import common.dto.FarmActivity;
import common.dto.FarmActivitySchedule;
import common.dto.Farmer;

@Repository
public class ActivityDaoImpl implements ActivityDao {

	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public List<Map<String, Object>> selectActivityList(Map<String, Object> map) {
		return sqlSession.selectList("ACTIVITY.selectActivityList", map);
	}

	@Override
	public List<EveryonesFarmFile> selectActivityFileThumbnail() {
		return sqlSession.selectList("ACTIVITY.selectActivityFileThumbnail");
	}

	@Override
	public Map<String, Object> selectActivityDetail(int activityNo) {
		return sqlSession.selectOne("ACTIVITY.selectActivityWithFarmByActivityNo", activityNo);
	}

	@Override
	public List<EveryonesFarmFile> selectActivityFileWithActivity(int activityNo) {
		return sqlSession.selectList("ACTIVITY.selectActivityFileByActivityNo", activityNo);
	}

	@Override
	public int insertApplication(Application application) {
		return sqlSession.insert("ACTIVITY.insertApplication", application);
	}

	@Override
	public int insertActivity(FarmActivity farmActivity) {
		return sqlSession.insert("ACTIVITY.insertActivity", farmActivity);
	}

	@Override
	public Farm selectFarmByFarmerNo(Farmer farmer) {
		return sqlSession.selectOne("ACTIVITY.selectFarmByFarmerNo", farmer);
	}

	@Override
	public List<FarmActivity> selectActivityListByFarmNo(Farm farm) {
		return sqlSession.selectList("ACTIVITY.selectActivityListByFarmNo", farm);
	}

	@Override
	public int insertFile(EveryonesFarmFile activityFile) {
		return sqlSession.insert("ACTIVITY.insertActivityFile", activityFile);
	}

	@Override
	public int insertActivitySchedule(FarmActivitySchedule schedule) {
		return sqlSession.insert("ACTIVITY.insertActivitySchedule", schedule);
	}

	@Override
	public int selectActivityCnt(Map<String, Object> map) {
		return sqlSession.selectOne("ACTIVITY.selectActivityCnt", map);
	}

	@Override
	public List<Farm> selectFarmList() {
		return sqlSession.selectList("ACTIVITY.selectFarmList");
	}

	@Override
	public List<FarmActivitySchedule> selectScheduleByActivityNo(int activityNo) {
		return sqlSession.selectList("ACTIVITY.selectActivityScheduleByActivityNo", activityNo);
		
	}

	@Override
	public FarmActivity selectActivityByActivityNo(int activityNo) {
		return sqlSession.selectOne("ACTIVITY.selectActivityByActivityNo", activityNo);
	}

	@Override
	public int deleteActivity(int activityNo) {
		return sqlSession.delete("ACTIVITY.deleteActivity", activityNo);
	}
	
	@Override
	public int deleteActivityFile(int activityNo) {
		return sqlSession.delete("ACTIVITY.deleteActivityFile", activityNo);
	}
	
	@Override
	public Farm selectFarmByFarmNo(int farmNo) {
		return sqlSession.selectOne("ACTIVITY.selectFarmByFarmNo", farmNo);
	}
	
}
