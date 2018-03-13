package com.taoding.mapper.ticket;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.domain.ticket.Ticket;
import com.taoding.domain.ticket.TicketExample;

@Repository
@Mapper
public interface TicketDao {
	
	long countByExample(TicketExample example);
	
	int deleteByExample(TicketExample example);
	
    int deleteByPrimaryKey(@Param("bookId") String bookId, @Param("id") String id);

    int insert(Ticket record);

    int insertSelective(Ticket record);

    Ticket selectByPrimaryKey(@Param("bookId") String bookId, @Param("id") String id);
    
    List<Ticket> selectByExample(TicketExample example);
    
    int updateByExampleSelective(@Param("record") Ticket record, @Param("example") TicketExample example);

    int updateByExample(@Param("record") Ticket record, @Param("example") TicketExample example);

    int updateByPrimaryKeySelective(Ticket record);

    int updateByPrimaryKey(Ticket record);
    
    /**
     * 为一个账簿初始化一张票据表
     * @param bookId
     * @return
     */
    int init(@Param("bookId") String bookId);
    
    /**
     * 按照条件集合查询票据
     * @param condition 条件集合
     * @return
     */
    List<Ticket> selectList(Map<String, Object> condition);
    
}