package com.taoding.common.service;

import java.util.List;

import com.taoding.common.dao.CrudDao;
import com.taoding.common.entity.DataEntity;
 
public interface CrudService<D extends CrudDao<T>, T extends DataEntity<T>>{
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T get(String id);
	
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public T get(T entity);
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity);
	
	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	public void save(T entity);
	
	/**
	 * 删除数据
	 * @param entity
	 */
	public void delete(T entity);

	/**
	 * 
	* @Description: TODO(插入前处理创建人，创建时间) 
	* @param entity void 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	public void preInsert(DataEntity<?> entity);
	
	/**
	 * 
	* @Description: TODO(修改时处理修改人，修改时间) 
	* @param entity void 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	public void preUpdate(DataEntity<?> entity);

}
