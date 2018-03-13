
package com.taoding.mapper.assisting;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.assisting.CpaAssistingGoodsType;

/**
 * 辅助核算模块的存货类型DAO接口
 * @author csl
 * @version 2017-11-20
 */
@Repository
@Mapper
public interface CpaAssistingGoodsTypeDao extends CrudDao<CpaAssistingGoodsType> {

}