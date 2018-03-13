package com.taoding.service.ticket.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.ticket.ListTemplate;
import com.taoding.mapper.ticket.ListTemplateDao;
import com.taoding.service.ticket.ListTemplateService;

@Service
@Transactional
public class ListTemplateServiceImpl implements ListTemplateService {
	
	@Autowired
	private ListTemplateDao listTemplateDao;

	@Override
	public ListTemplate geListTemplate(String tableName) {
		Map<String, Object> condition = new HashMap<>();
		condition.put("tableName", tableName);
		condition.put("expiredVersion", 0);
		List<ListTemplate> listTemplates = listTemplateDao.selectList(condition);
		return listTemplates == null || listTemplates.size() == 0 ? null : listTemplates.get(0);
	}

	@Override
	public void insertTemplate(String tableName, String template) {
		if (StringUtils.isBlank(tableName) || StringUtils.isBlank(template)) {
			throw new LogicException("参数错误!");
		}
		ListTemplate listTemplate = geListTemplate(tableName);
		
		if (listTemplate == null) {
			listTemplate = new ListTemplate();
			listTemplate.setId(UUIDUtils.getUUid());
			listTemplate.setTableName(tableName);
			listTemplate.setTemplateList(template);
			listTemplate.setVersion("1");
			listTemplate.setRecommendUpdate((byte) 1);
		} else {
			listTemplate.setId(UUIDUtils.getUUid());
			listTemplate.setTemplateList(template);
			int version = Integer.parseInt(listTemplate.getVersion());
			listTemplate.setVersion(String.valueOf(version + 1));
		}
		Date date = new Date();
		listTemplate.setCreated(date);
		listTemplate.setUpdated(date);
		listTemplateDao.insertSelective(listTemplate);
	}

}
