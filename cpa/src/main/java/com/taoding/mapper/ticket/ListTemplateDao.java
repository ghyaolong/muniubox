package com.taoding.mapper.ticket;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.domain.ticket.ListTemplate;

@Repository
@Mapper
public interface ListTemplateDao {
    int deleteByPrimaryKey(String id);

    int insert(ListTemplate record);

    int insertSelective(ListTemplate record);

    ListTemplate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ListTemplate record);

    int updateByPrimaryKeyWithBLOBs(ListTemplate record);

    int updateByPrimaryKey(ListTemplate record);
    
    List<ListTemplate> selectList(Map<String, Object> condition);
}