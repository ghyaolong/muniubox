package com.taoding.mapper.ticket;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.domain.ticket.TicketListTemplate;

@Repository
@Mapper
public interface TicketListTemplateDao {
    int deleteByPrimaryKey(String id);

    int insert(TicketListTemplate record);

    int insertSelective(TicketListTemplate record);

    TicketListTemplate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TicketListTemplate record);

    int updateByPrimaryKey(TicketListTemplate record);
    
    List<TicketListTemplate> selectList(Map<String, Object> condition);
}