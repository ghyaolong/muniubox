package com.taoding.mapper.ticket;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.domain.ticket.TicketList;
import com.taoding.domain.ticket.TicketListExample;
import com.taoding.domain.ticket.vo.TicketListVo;

@Repository
@Mapper
public interface TicketListDao {
	long countByExample(TicketListExample example);

    int deleteByExample(TicketListExample example);

    int deleteByPrimaryKey(String id);

    int insert(TicketList record);

    int insertSelective(TicketList record);

    List<TicketList> selectByExample(TicketListExample example);

    TicketList selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TicketList record, @Param("example") TicketListExample example);

    int updateByExample(@Param("record") TicketList record, @Param("example") TicketListExample example);

    int updateByPrimaryKeySelective(TicketList record);

    int updateByPrimaryKey(TicketList record);
    
    
    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<自定义方法>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    
    /**
     * 查询列表
     * @param condition 检索条件集合
     * @return
     */
    List<TicketList> selectList(Map<String, Object> condition);
    
    /**
     * 查询列表 含目录下票据数量
     * @param condition 检索条件集合
     * @return
     */
    List<TicketListVo> selectListVo(Map<String, Object> condition);
    
}