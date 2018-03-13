package com.taoding.service.dict;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.PageUtils;
import com.taoding.domain.dict.Dict;
import com.taoding.mapper.dict.DictDao;

/**
 * 数据字典serviceImpl
 * @author csl
 * @date 2017年11月10日
 */
@Service
public class DictServiceImpl extends DefaultCurdServiceImpl<DictDao, Dict> 
	implements DictService{

	/**
	 * 分页+按条件查询用户
	 * @param queryMap
	 * @return
	 */
	public PageInfo<Dict> findAllDictByPage(Map<String,Object> queryMap){
		PageUtils.page(queryMap);
		List<Dict> dictLists = dao.findAllDictList(queryMap);
		PageInfo<Dict> info = new PageInfo<Dict>(dictLists);
		return info;
	}
}
