package com.taoding.common.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.dao.CrudDao;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.user.User;

public class DefaultCurdServiceImpl<D extends CrudDao<T>, T extends DataEntity<T>> implements CrudService<D, T>{
	
	@Autowired
	protected D dao;

	@Override
	@Transactional(readOnly = true)
	public T get(String id) {
		return dao.get(id);
	}

	@Override
	@Transactional(readOnly = true)
	public T get(T entity) {
		return dao.get(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(T entity) {
		if (entity.getIsNewRecord()){
			entity.setId(UUID.randomUUID().toString().replace("-", ""));
			this.preInsert(entity);
			this.preUpdate(entity);
			dao.insert(entity);
		}else{
			this.preUpdate(entity);
			dao.update(entity);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(T entity) {
		dao.delete(entity);
	}
	
	@Override
   public void preInsert(DataEntity<?> entity){
	   if(null == entity) return ;
	   User user= new User(UserUtils.getCurrentUserId());
 	   if(StringUtils.isBlank(entity.getId())){
 		  entity.setId(UUID.randomUUID().toString().replace("-", ""));
 	   }
 	   Date date = new Date();
	   entity.setCreateDate(date);
	   entity.setCreateBy(user);
	   entity.setUpdateDate(date);
	   entity.setUpdateBy(user);
	}
	
	@Override
	public void preUpdate(DataEntity<?> entity){
		if(null == entity) return ;
	 	   User user= new User(UserUtils.getCurrentUserId());
		   entity.setUpdateDate(new Date());
		   entity.setUpdateBy(user);
	}
}
