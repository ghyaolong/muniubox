package com.taoding.mapper.report.assetliability;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.report.assetliability.OperationSource;


@Repository
@Mapper
public interface OperationSourceDao extends CrudDao<OperationSource>{

	/**
	 * 获取某个模块下的所有取数规则
	 * @param modelId
	 * @return
	 */
	List<OperationSource> listOperationSourceByModelId(Integer modelId);
	
}
