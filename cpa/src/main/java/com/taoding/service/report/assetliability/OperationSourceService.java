package com.taoding.service.report.assetliability;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.report.assetliability.OperationSource;
import com.taoding.mapper.report.assetliability.OperationSourceDao;

public interface OperationSourceService extends CrudService<OperationSourceDao, OperationSource>{
	
	/**
	 * 根据模块id获取相应的取数规则列表
	 * @param modelId
	 * @return
	 */
	List<OperationSource> listOperationSourceListByModelId(Integer modelId);
	
}
