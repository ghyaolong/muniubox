package com.taoding.service.fixedAsset;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.mapper.fixedAsset.AssetTypeDao;

/**
 * 
* @ClassName: AssetTypeServiceImp 
* @Description: TODO(资产类型业务层) 
* @author lixc 
* @date 2017年11月27日 下午4:59:25 
*
 */
@Service
@Transactional(readOnly=true)
public class AssetTypeServiceImp extends DefaultCurdServiceImpl<AssetTypeDao, AssetType> implements AssetTypeService {
	
	public List<AssetType> findAssetTypeListByAccountId(String accountId,Integer type){
		 if(StringUtils.isBlank(accountId)){
			  throw new LogicException("账簿不能为空！");
		 }
		 AssetType assetType = new AssetType();
		 assetType.setAccountId(accountId);
		 assetType.setType(type);
		  return dao.findList(assetType);
	  }

	@Override
	@Transactional(readOnly = false)
	public void saveAssetType(AssetType assetType) {
		
		if(null == assetType) 
			throw new LogicException("资产类型不能为空！");
		
		if(assetType.getType() == AssetType.FIXED_ASSET_TYPE)
		{
			if(null == assetType.getResidualRate()){
				throw new LogicException("残值率不能为空！");
			}else if(null == assetType.getLifecycle()){
				throw new LogicException("使用期限不能为空！");
			}
		}
		//添加处理编号
		if(StringUtils.isBlank(assetType.getId())){
		String maxNo= dao.findMaxNoByAccountIdAndType(assetType.getAccountId(),assetType.getType());
		if(StringUtils.isBlank(maxNo))
		{
			maxNo="001";
		}else{
			int num=0;
			try {
				num=Integer.parseInt(maxNo);
			} catch (NumberFormatException e) {
				maxNo="001";
			}
			num = num+1;
			maxNo = String.format("%03d", num);
		}
		assetType.setNo(maxNo);
		
		}

		save(assetType);
	}
	
}
