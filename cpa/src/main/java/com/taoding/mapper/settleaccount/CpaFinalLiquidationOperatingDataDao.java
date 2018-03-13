package com.taoding.mapper.settleaccount;

import com.taoding.domain.settleaccount.CpaFinalLiquidationOperatingData;
import com.taoding.domain.settleaccount.CpaFinalLiquidationOperatingDataExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CpaFinalLiquidationOperatingDataDao {
    long countByExample(CpaFinalLiquidationOperatingDataExample example);

    int deleteByExample(CpaFinalLiquidationOperatingDataExample example);

    int deleteByPrimaryKey(String id);

    int insert(CpaFinalLiquidationOperatingData record);

    int insertSelective(CpaFinalLiquidationOperatingData record);

    List<CpaFinalLiquidationOperatingData> selectByExample(CpaFinalLiquidationOperatingDataExample example);

    CpaFinalLiquidationOperatingData selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") CpaFinalLiquidationOperatingData record, @Param("example") CpaFinalLiquidationOperatingDataExample example);

    int updateByExample(@Param("record") CpaFinalLiquidationOperatingData record, @Param("example") CpaFinalLiquidationOperatingDataExample example);

    int updateByPrimaryKeySelective(CpaFinalLiquidationOperatingData record);

    int updateByPrimaryKey(CpaFinalLiquidationOperatingData record);
}