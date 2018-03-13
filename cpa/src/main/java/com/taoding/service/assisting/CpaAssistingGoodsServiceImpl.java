
package com.taoding.service.assisting;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.NextNoUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingGoods;
import com.taoding.domain.assisting.CpaAssistingGoodsType;
import com.taoding.mapper.assisting.CpaAssistingGoodsDao;
import com.taoding.mapper.assisting.CpaAssistingGoodsTypeDao;

/**
 * 辅助核算模块存货信息表Service
 * @author csl
 * @version 2017-11-20
 */
@Service
@Transactional
public class CpaAssistingGoodsServiceImpl extends DefaultCurdServiceImpl<CpaAssistingGoodsDao, CpaAssistingGoods>
	implements CpaAssistingGoodsService{

	@Autowired
	private CpaAssistingGoodsTypeDao cpaAssistingGoodsTypeDao;
	
	@Autowired
	private CpaAssistingGoodsTypeService cpaAssistingGoodsTypeService;
	
	@Autowired
	private CpaAssistingGoodsService cpaAssistingGoodsService;
	
	/**
	 * 查询存货条目信息+分页(传递参数isAll=true 表示不执行分页，isAll=false 表示执行分页)
	 * @param queryMap
	 * add csl
	 * 2017-11-18 15:54:11
	 */
	public Object findAllList(Map<String, Object> queryMap){
		String isAll = queryMap.get("isAll").toString();
		if(StringUtils.isEmpty(isAll) || "false".equals(isAll)){
			//处理分页
			PageUtils.page(queryMap);
		}
		List<CpaAssistingGoods> lists= dao.findAllList(queryMap);
		PageInfo<CpaAssistingGoods> info = new PageInfo<CpaAssistingGoods>(lists);	
		if("true".equals(isAll)){
			return lists ;
		}
		return info;
	}
	
	/**
	 * 新增辅助核算类型中的存货详细条目
	 * @param queryMap
	 * add csl
	 * 2017-11-18 15:54:11
	 */
	public Object insertGoods(Map<String, Object> queryMap){
		if (!(queryMap.containsKey("goodsName") && queryMap.get("goodsName") != null 
				&& StringUtils.isNotEmpty(String.valueOf(queryMap.get("goodsName"))))) {
			throw new LogicException("存货名称不能为空！");
		}
		String goodsName = queryMap.get("goodsName").toString();
		if (StringUtils.isEmpty(goodsName)) {
			throw new LogicException("必须传入存货名称！");
		}
		String accountId = queryMap.get("accountId").toString();
		if (StringUtils.isNotEmpty(goodsName)&& StringUtils.isNoneEmpty(accountId)) {
			CpaAssistingGoods goods = this.findByName(goodsName,accountId);
			if (null!=goods && StringUtils.isNotEmpty(goods.getGoodsName())) {
				throw new LogicException("名称已存在！");
			}
		}
		if (StringUtils.isEmpty(queryMap.get("id").toString())) {
			throw new LogicException("辅助核算类型对象为空！");
		}
		CpaAssistingGoods goods1= new CpaAssistingGoods();
		goods1.setAccountId(accountId);
		goods1.setGoodsNo(this.findMaxNoByInfoNo(accountId));
		goods1.setGoodsName(goodsName);
		if (queryMap.containsKey("goodsSource") && queryMap.get("goodsSource") != null) {
			goods1.setGoodsSource(queryMap.get("goodsSource").toString());
		}
		if (queryMap.containsKey("spec") && queryMap.get("spec") != null) {
			goods1.setSpec(queryMap.get("spec").toString());
		}
		if (queryMap.containsKey("unit") && queryMap.get("unit") != null) {
			goods1.setUnit(queryMap.get("unit").toString());
		}
		if (queryMap.containsKey("initMoney") && queryMap.get("initMoney") != null) {
			goods1.setInitMoney(queryMap.get("initMoney").toString());
		}
		if (queryMap.containsKey("initCount") && queryMap.get("initCount") != null) {
			goods1.setInitCount(queryMap.get("initCount").toString());
		}
		if (!(queryMap.containsKey("goodsId") && queryMap.get("goodsId") != null && StringUtils.isNotEmpty(String.valueOf(queryMap.get("goodsId"))))) {
			//获取存货类型的第一条数据
			CpaAssistingGoodsType cpaAssistingGoodsType = new CpaAssistingGoodsType();
			cpaAssistingGoodsType.setAccountId(accountId);
			List<CpaAssistingGoodsType> goodsTypeLists = cpaAssistingGoodsTypeService.findList(cpaAssistingGoodsType);
			if (Collections3.isEmpty(goodsTypeLists)) {
				throw new LogicException("存货类型的信息不存在！");
			}
			queryMap.put("goodsId", goodsTypeLists.get(0).getId());
		}
		goods1.setGoodsId(queryMap.get("goodsId").toString());
		goods1.setRemarks(queryMap.get("goodsName").toString());
		this.save(goods1);
		return goods1.getGoodsNo();
	}
	
	/**
	 * 根据名字查找
	 * @param name
	 * @param accountId
	 * @return
	 */
	public CpaAssistingGoods findByName(String goodsName,String accountId){
		return dao.findByName(goodsName,accountId);
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
	 * 编辑存货的信息
	 * @param cpaAssistingGoods
	 * @return
	 */
	@Override
	public Object updateGoods(CpaAssistingGoods cpaAssistingGoods) {
		if (StringUtils.isEmpty(cpaAssistingGoods.getId())) {
			throw new LogicException("存货id不能为空！");
		}
		CpaAssistingGoodsType goodsType = new CpaAssistingGoodsType();
		if (StringUtils.isEmpty(cpaAssistingGoods.getAccountId())) {
			throw new LogicException("账薄id不能为空！");
		}
		CpaAssistingGoods cpaAssistingGoods2 = this.findByName(cpaAssistingGoods.getGoodsName(),cpaAssistingGoods.getAccountId());
		if (cpaAssistingGoods2!=null && !cpaAssistingGoods2.getId().equals(cpaAssistingGoods.getId())) {
			throw new LogicException("辅助核算类型条目是新增条目，无法编辑！");
		}
		goodsType.setAccountId(cpaAssistingGoods.getAccountId());
		List<CpaAssistingGoodsType> list = cpaAssistingGoodsTypeService.findList(goodsType);
		for (CpaAssistingGoodsType goodTypes : list) {
			if (goodTypes.getId().equals(cpaAssistingGoods.getGoodsId())) {
				dao.update(cpaAssistingGoods);
				return true;
			}
		}
		throw new LogicException("存货类型不存在！");	
	}
	
	/**
	 * 删除存货
	 * @param id
	 * @return
	 */
	@Override
	public Object deleteGoods(String id) {
		if (dao.get(id)==null) {
			throw new LogicException("你要删除的对象的id不存在！");
		}
		int count = dao.delete(id);
		if (count>0) {
			return true;
		}
		return false;
	}
	
}