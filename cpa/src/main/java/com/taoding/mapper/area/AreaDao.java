package com.taoding.mapper.area;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.TreeDao;
import com.taoding.domain.area.Area;

@Repository
@Mapper
public interface AreaDao extends TreeDao<Area> {
	/**
	 * 根据id查询子机构
	 * 
	 * @return
	 */
	List<Area> getAreaTreeChildrenByParentId(@Param("parentId")String id);

}
