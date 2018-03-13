package com.taoding.mapper.checkoutSetting;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.checkoutSetting.SettleAccountOperatingSetting;
/**
 * 经营数据分析设置Dao
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Repository
@Mapper
public interface SettleAccountOperatingSettingDao extends CrudDao<SettleAccountOperatingSetting> {
	/**
	 * 初始化插入经营数据分析设置
	 * @param settleAccountOperatingSettingList 经营数据分析设置 
	 * @return
	 */
	void batchInsert(@Param("settleAccountOperatingSettingList")List<SettleAccountOperatingSetting> settleAccountOperatingSettingList);
	/**
	 * 经营数据分析设置
	 * @param bookId 账薄id
	 * @return
	 */
	List<SettleAccountOperatingSetting> findList(String bookId);

}
