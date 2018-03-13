
package com.taoding.service.assisting;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingPosition;
import com.taoding.mapper.assisting.CpaAssistingPositionDao;

/**
 * 职位Service
 * @author csl
 * @version 2017-11-22
 */
@Service
@Transactional
public class CpaAssistingPositionServiceImpl extends DefaultCurdServiceImpl<CpaAssistingPositionDao, CpaAssistingPosition>
	implements CpaAssistingPositionService{

	/**
	 * 查询所有的辅助核算类型中的职位信息(传递参数isAll=true 表示不执行分页，isAll=false 表示执行分页)
	 * @param queryMap
	 * @return info
	 */
	public Object findAllList(Map<String, Object> queryMap){
		String isAll = queryMap.get("isAll").toString();
		if(StringUtils.isEmpty(isAll) || "false".equals(isAll)){
			//处理分页
			PageUtils.page(queryMap);
		}
		List<CpaAssistingPosition> lists= dao.findAllList(queryMap);
		PageInfo<CpaAssistingPosition> info = new PageInfo<CpaAssistingPosition>(lists);	
		if("true".equals(isAll)){
			return lists ;
		}
		return info;
	}
	
	
	/**
	 * 新增辅助核算类型中的职位详细条目
	 * @param queryMap
	 * add csl
	 * 2017-11-18 15:54:11
	 */
	public Object insertPosition(Map<String, Object> queryMap){
		if (!(queryMap.containsKey("positionName") && queryMap.get("positionName") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("positionName"))))) {
			throw new LogicException("职位名称不能为空！");
		}
		String positionName = queryMap.get("positionName").toString();
		String accountId = queryMap.get("accountId").toString();
		if (StringUtils.isNotEmpty(positionName)&& StringUtils.isNoneEmpty(accountId)) {
			CpaAssistingPosition position = this.findByName(positionName,accountId);
			if (position!=null && StringUtils.isNotEmpty(position.getPositionName())) {
				throw new LogicException("名称已存在！");
			}
		}
		if (StringUtils.isEmpty(queryMap.get("id").toString())) {
			throw new LogicException("辅助核算类型对象为空！");
		}
		CpaAssistingPosition cpaPosition = new CpaAssistingPosition();
		cpaPosition.setAccountId(accountId);
		cpaPosition.setPositionName(positionName);
		cpaPosition.setPositionNo(this.findMaxNoByInfoNo(accountId));
		this.save(cpaPosition);
		return cpaPosition.getPositionNo();
	}
	
	/**
	 * 根据名字查找
	 * @param positionName
	 * @param accountId
	 * @return
	 */
	public CpaAssistingPosition findByName(String positionName,String accountId){
		return dao.findByName(positionName,accountId);
	}
	
	/**
	 * 查询最大编号
	 * @param accountId
	 * @return
	 */
	public String findMaxNoByInfoNo(String accountId){
		String maxNo = dao.findMaxNoByInfoNo(accountId);
		String nextNo = "";
		if(StringUtils.isNotEmpty(maxNo)){
			nextNo = NextNoUtils.getNextNo(maxNo);
		}else{
			nextNo = "001";
		}
		return nextNo;
	}


	/**
	 * 编辑职位
	 * @param cpaPosition
	 * @return
	 */
	@Override
	public Object updateCpaPosition(CpaAssistingPosition cpaPosition) {
		if (cpaPosition!=null && StringUtils.isNoneEmpty(cpaPosition.getId()) && StringUtils.isNoneEmpty(cpaPosition.getAccountId())) {
			CpaAssistingPosition cpaPosition2 = this.findByName(cpaPosition.getPositionName(),cpaPosition.getAccountId());
			if (cpaPosition2!=null && !cpaPosition2.getId().equals(cpaPosition.getId())) {
				throw new LogicException("辅助核算类型条目是新增条目，无法编辑！");
			}
			int count = dao.update(cpaPosition);
			if (count>0) {
				return true;
			}
			return false;
		}
		throw new LogicException("编辑对象为空！");
	}


	/**
	 * 删除职位
	 * @param id
	 * @return
	 */
	@Override
	public Object deleteCpaPosition(String id) {
		if (dao.get(id)==null) {
			throw new LogicException("你要删除的对象的id不存在！");
		}
		int count = dao.delete(id);
		if (count>0) {
			return true;
		}
		return false;
	}


	/**
	 * 查找所有的职位(模糊搜索)
	 * @param 
	 * @return
	 */
	public List<CpaAssistingPosition> findAllList() {
		return dao.findAllList();
	}
	
	
}