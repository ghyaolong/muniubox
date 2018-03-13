package com.taoding.service.area;

import com.taoding.common.service.CrudService;
import com.taoding.domain.area.Area;
import com.taoding.mapper.area.AreaDao;


public interface AreaService extends CrudService<AreaDao, Area> {
	/**
	 * 获取省市树结构数据
	 * @return  
	 */
	public Object getTreeArea();
}
