package com.taoding.mapper.dict;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.dict.Dict;
import com.taoding.domain.user.User;


/**
 * <p>Description: 获取数据字典列表</p>  
 * @author csl
 * @date 2017年11月7日
 */
@Repository
@Mapper
public interface DictDao extends CrudDao<Dict>{
	/**
	 * 分页+按条件查询用户
	 * @param queryMap
	 * @return
	 */
	public List<Dict> findAllDictList(Map<String,Object> queryMap);

	
}
