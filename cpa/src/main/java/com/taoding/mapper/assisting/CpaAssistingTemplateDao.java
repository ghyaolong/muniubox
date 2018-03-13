
package com.taoding.mapper.assisting;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaAssistingTemplate;

/**
 * 辅助核算类型模板DAO接口
 * @author CSL
 * @version 2017-11-17
 */
@Repository
@Mapper
public interface CpaAssistingTemplateDao extends CrudDao<CpaAssistingTemplate> {
	
}