package com.taoding.mapper.accountingBook;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.accountingbook.AccountSystem;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.accountingbook.Authorise;
import com.taoding.domain.accountingbook.TaxpayerProperty;


/**
* @ClassName: AccountingBookDao 
* @author lixc 
* @date 2017年11月20日 上午10:47:37 
 */
@Repository
@Mapper
public interface AccountingBookDao extends CrudDao<AccountingBook>{
	 
	/**
	 * 
	* @Description: 插入指派授权表 
	* @param entity
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	public int  insertAuthorise(Authorise entity);

	/**
	 * 
	* @Description: 插入指派授权表 
	* @param entity
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	public int  updateAuthorise(Authorise entity);

	/**
	 * 
	* @Description: 删除
	* @param entity
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	public int  deleteAuthorise(@Param("id")String id);
	/**
	 * 
	* @Description: 删除
	* @param id 
	* @return Authorise 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	
	public Authorise getAuthorise(@Param("Id")String Id);
	
	/**
	 * 
	* @Description: TODO(通过实例查询准确查询) 
	* @param entity Authorise 类中的字段，不包括 父类的字段（除了 id,del_falg）
	* @return List<Authorise> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
    public List<Authorise>selectAuthoriseList(Authorise entity);
    /**
     * 
    * @Description: TODO(获得纳税人性质列表) 
    * @param entity
    * @return List<TaxpayerProperty> 返回类型    
    * @throws 
    * @author lixc
    * @date 2017年11月20日
     */
    public List<TaxpayerProperty> findTaxpayerPropertyList(TaxpayerProperty entity);
    
     /**
      * 
     * @Description: TODO(获得会计制度列表) 
     * @param entity
     * @return List<AccountSystem> 返回类型    
     * @throws 
     * @author lixc
     * @date 2017年11月20日
      */
    public List<AccountSystem> findAccountSystemList(AccountSystem entity);
    /**
     * 
    * @Description: TODO(查询账套信息) 
    * @param customerId 客户id  
    * @return AccountingBook 返回类型    
    * @throws 
    * @author mhb
    * @date 2017年12月1日
     */
	public AccountingBook findCustomerAccountBook(@Param("customerId")String customerId);
	/**
     * 
    * 更新账薄
    * @param customerId 客户id  
    * @param type 纳税人类型
    * @throws 
    * @author mhb
    * @date 2017年12月1日
     */
	public void updateType(@Param("id")String id,@Param("taxpayerPropertyId")String type);
	
	
}
