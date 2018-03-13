package com.taoding.service.fixedAsset;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.mapper.fixedAsset.FixedAssetClearDao;
import com.taoding.service.voucher.CpaVoucherService;
/**
 * 
* @ClassName: FixedAssetClearServiceImp 
* @Description: TODO(固定资产清理业务层) 
* @author mhb 
* @date 2017年12月1日 下午3:55:22 
*
 */
@Service
@Transactional(readOnly=true)
public class FixedAssetClearServiceImp extends DefaultCurdServiceImpl<FixedAssetClearDao, FixedAsset> implements FixedAssetClearService {

	@Autowired
	private CpaVoucherService cpaVoucherService;
	
	@Override
	@Transactional(readOnly=false)
	public PageInfo<FixedAsset> findFixedAssetClearListByPage(Map<String, Object> queryMap) { 
			String accountId = queryMap.get("accountId")==null?"":queryMap.get("accountId").toString();
			Date currentPeriod = queryMap.get("currentPeriod")==null?null:DateUtils.parseDate(queryMap.get("currentPeriod").toString());
			
		  if(StringUtils.isBlank(accountId)){
				throw new LogicException("账簿不能为空！");
			}
			
			if(currentPeriod==null){
				throw new LogicException("账期不能为空！");
			}
			
			if(queryMap.get("isAll") == null || "".equals(queryMap.get("isAll")) || "false".equals(queryMap.get("isAll"))){
				PageUtils.page(queryMap);
			}
			queryMap.put("status", FixedAsset.STATUS_ALREADY_CLEAN_OR_HANDE);
			List<FixedAsset> list = dao.findFixedAssetClearListByMap(queryMap); 
			  return new PageInfo<FixedAsset>(list); 
	}

	@Override
	@Transactional(readOnly=false)
	public Object restore(String ids) {
		String[] strIds = ids.split(","); 
		for (String id : strIds) {
			FixedAsset fixedAsset=dao.get(id);
			if(fixedAsset==null){
				throw new LogicException("数据异常");
			}
			fixedAsset.setStatus(FixedAsset.STATUS_NORMAL); 
			//更新固定资产表status状态 
			dao.updateStatus(fixedAsset);
			//更新凭证表status状态 
			cpaVoucherService.deleteById(fixedAsset.getAccountId(), fixedAsset.getVoucherId());	
		}
			return true; 
	}
}
