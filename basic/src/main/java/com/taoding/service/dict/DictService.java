package com.taoding.service.dict;

import java.util.Map;
import com.github.pagehelper.PageInfo;
import com.taoding.common.service.CrudService;
import com.taoding.domain.dict.Dict;
import com.taoding.mapper.dict.DictDao;

/**
 * 数据字典service 
 * @author csl
 * @date 2017年11月10日
 */
public interface DictService extends CrudService<DictDao, Dict>{
	
	/**
	 * 分页+按条件查询用户
	 * @param queryMap
	 * @return
	 */
	public PageInfo<Dict> findAllDictByPage(Map<String,Object> queryMap); 

}
