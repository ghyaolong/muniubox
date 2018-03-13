package com.taoding.service.report.assetliability;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.report.assetliability.OperationSource;
import com.taoding.mapper.report.assetliability.OperationSourceDao;

@Service
@Transactional
public class OperationSourceServiceImpl extends DefaultCurdServiceImpl<OperationSourceDao, OperationSource> implements OperationSourceService{

	@Override
	public List<OperationSource> listOperationSourceListByModelId(Integer modelId) {
		return dao.listOperationSourceByModelId(modelId);
	}

}
