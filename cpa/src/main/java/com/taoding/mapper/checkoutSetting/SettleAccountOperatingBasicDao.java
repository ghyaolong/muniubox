package com.taoding.mapper.checkoutSetting;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.checkoutSetting.SettleAccountOperatingBasic;
/**
 * 经营数据分析基础Dao
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Repository
@Mapper
public interface SettleAccountOperatingBasicDao extends CrudDao<SettleAccountOperatingBasic> {

}
