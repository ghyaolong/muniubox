
package com.taoding.service.assisting;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaCustomerAssisting;
import com.taoding.domain.assisting.CpaCustomerAssistingInfo;
import com.taoding.mapper.assisting.CpaCustomerAssistingInfoDao;

/**
 * 辅助核算Service
 * @author csl
 * @version 2017-11-16
 */
@Service
@Transactional
public class CpaCustomerAssistingInfoServiceImpl 
	extends DefaultCurdServiceImpl<CpaCustomerAssistingInfoDao, CpaCustomerAssistingInfo>
	implements CpaCustomerAssistingInfoService{

	/**
	 * 根据id查询列表(传递参数isAll=true 表示不执行分页，isAll=false 表示执行分页)
	 * 判断条件：辅助核算类型：客户，供应商，第三方支付
	 * @param queryMap
	 * @return
	 * add csl
	 * 2017-11-18 11:53:37
	 */
	public Object findListById(Map<String, Object> queryMap){
		String isAll = queryMap.get("isAll").toString();
		if(StringUtils.isEmpty(isAll) || "false".equals(isAll)){
			//处理分页
			PageUtils.page(queryMap);
		}
		List<CpaCustomerAssistingInfo> lists= dao.findListById(queryMap);
		PageInfo<CpaCustomerAssistingInfo> info = new PageInfo<CpaCustomerAssistingInfo>(lists);	
		if("true".equals(isAll)){
			return lists ;
		}
		return info;
	}

	
	/**
	 * 新增辅助核算类型的详细条目
	 * @param cpaCustomerAssistingInfo
	 * add csl
	 * 2017-11-18 15:54:11
	 */
	public Object insertCpaCustomerAssistingInfo(Map<String, Object> queryMap){
		if (!(queryMap.containsKey("name") && queryMap.get("name") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("name"))))) {
			throw new LogicException("名称不能为空，请输入名称！");
		}
		String name = queryMap.get("name").toString();
		String id = queryMap.get("id").toString();
		if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(id)) {
			CpaCustomerAssistingInfo cpaCustomerAssistingInfo2 = this.findByName(name,id);
			if (cpaCustomerAssistingInfo2 != null && StringUtils.isNotEmpty(cpaCustomerAssistingInfo2.getName())) {
				throw new LogicException("名称已存在！");
			}
		}
		if (StringUtils.isEmpty(queryMap.get("id").toString())) {
			throw new LogicException("辅助核算类型对象为空！");
		}
		CpaCustomerAssistingInfo cpaCustomerAssistingInfo = new CpaCustomerAssistingInfo();
		cpaCustomerAssistingInfo.setCatalogId(id);
		cpaCustomerAssistingInfo.setNo(this.findMaxNoByInfoNo());
		cpaCustomerAssistingInfo.setName(name);
		this.save(cpaCustomerAssistingInfo);
		return cpaCustomerAssistingInfo.getNo();
	}
	
	/**
	 * 编辑辅助核算详情条目中的信息
	 * @param cpaCustomerAssistingInfo
	 * @return
	 * add csl
	 * 2017-11-18 16:51:47
	 */
	public Object updateCpaCustomerAssistingInfo(CpaCustomerAssistingInfo cpaCustomerAssistingInfo){
		if (null!=cpaCustomerAssistingInfo && StringUtils.isNoneEmpty(cpaCustomerAssistingInfo.getId())) {
			//根据辅助核算详细信息的id查询辅助核算类型的id
			CpaCustomerAssistingInfo cpaCustomerAssistingInfo3 = dao.get(cpaCustomerAssistingInfo.getId());
			String catalogId = cpaCustomerAssistingInfo3.getCatalogId();
			//根据名称和辅助核算类型的id获取对象
			CpaCustomerAssistingInfo cpaCustomerAssistingInfo2 = this.findByName(cpaCustomerAssistingInfo.getName(),catalogId);
			if (cpaCustomerAssistingInfo2 != null && !cpaCustomerAssistingInfo.getId().equals(cpaCustomerAssistingInfo2.getId()) ) {
				throw new LogicException("辅助核算类型条目是新增条目，无法编辑！");
			}
			cpaCustomerAssistingInfo.setRemarks(cpaCustomerAssistingInfo.getName());
			int count = dao.update(cpaCustomerAssistingInfo);
			if (count>0) {
				return true;
			}
			return false;
		}
		throw new LogicException("编辑对象为空！");
	}

	/**
	 * 根据名字查找
	 * @param name
	 * @param id（辅助核算类型id）
	 * @return
	 */
	public CpaCustomerAssistingInfo findByName(String name,String id){
		return dao.findByName(name,id);
	}
	
	/**
	 * 查询最大编号
	 * @param accountId
	 * @return
	 */
	public String findMaxNoByInfoNo(){
		String maxNo = dao.findMaxNoByInfoNo();
		String nextNo = "";
		if(StringUtils.isNotEmpty(maxNo)){
			nextNo = NextNoUtils.getNextNo(maxNo);
		}else{
			nextNo = "001";
		}
		return nextNo;
	}

}