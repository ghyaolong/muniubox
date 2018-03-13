package com.taoding.mapper.ticket;

import com.taoding.domain.ticket.BankStatement;
import com.taoding.domain.ticket.BankStatementExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BankStatementDao {
    long countByExample(BankStatementExample example);

    int deleteByExample(BankStatementExample example);

    int deleteByPrimaryKey(String id);

    int insert(BankStatement record);

    int insertSelective(BankStatement record);

    List<BankStatement> selectByExample(BankStatementExample example);

    BankStatement selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") BankStatement record, @Param("example") BankStatementExample example);

    int updateByExample(@Param("record") BankStatement record, @Param("example") BankStatementExample example);

    int updateByPrimaryKeySelective(BankStatement record);

    int updateByPrimaryKey(BankStatement record);
}