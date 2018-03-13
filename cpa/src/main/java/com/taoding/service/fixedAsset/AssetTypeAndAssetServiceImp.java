package com.taoding.service.fixedAsset;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.fixedAsset.AssetTypeAndAsset;
import com.taoding.mapper.fixedAsset.AssetTypeDao;
import com.taoding.mapper.fixedAsset.FixedAssetDao;

@Service
@Transactional(readOnly=true)
public class AssetTypeAndAssetServiceImp implements AssetTypeAndAssetService {

	@Autowired
	private FixedAssetDao fixedAssetDao;
	
	@Autowired
	private AssetTypeDao assetTypeDao;
	
	@Override
	public AssetType findAssetType(String accountId, int type, String assetName) {
		if (StringUtils.isBlank(accountId) || StringUtils.isBlank(assetName)) {
			throw new LogicException("账薄或资产名称不能为空！");
		}
		if(AssetType.FIXED_ASSET_TYPE != type && AssetType.INTANGIBLE_ASSET_TYPE != type){
			throw new LogicException("不存在资产业务【"+type+"】！");
		}
		
		List<AssetTypeAndAsset> list = fixedAssetDao.findAssetTypeAndAssetList(
				assetName, accountId, type,null);

		if (!CollectionUtils.isEmpty(list)) {
			AssetTypeAndAsset assetTypeAndAsset = list.get(0);
			return assetTypeDao.get(assetTypeAndAsset.getAssetTypeId());
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public void checkAssetTypeAndAsset(AssetTypeAndAsset assetTypeAndAsset) {
		
		if(null == assetTypeAndAsset) return ;
		
		if (StringUtils.isBlank(assetTypeAndAsset.getAccountId()) || StringUtils.isBlank(assetTypeAndAsset.getAssetName())) {
			throw new LogicException("账薄或资产名称不能为空！");
		}
		if(AssetType.FIXED_ASSET_TYPE != assetTypeAndAsset.getType() && AssetType.INTANGIBLE_ASSET_TYPE != assetTypeAndAsset.getType()){
			throw new LogicException("不存在资产业务【"+assetTypeAndAsset.getType()+"】！");
		}
		
		List<AssetTypeAndAsset> list = fixedAssetDao.findAssetTypeAndAssetList(
				assetTypeAndAsset.getAssetName(), assetTypeAndAsset.getAccountId(),assetTypeAndAsset.getType(),assetTypeAndAsset.getAssetTypeId());

		if (CollectionUtils.isEmpty(list)) {
			assetTypeAndAsset.setLastUpdate(new Date());
			fixedAssetDao.insertAssetTypeAndAsset(assetTypeAndAsset);

		}else{
			assetTypeAndAsset = list.get(0);
			assetTypeAndAsset.setLastUpdate(new Date());
			fixedAssetDao.updateAssetTypeAndAsset(assetTypeAndAsset);
		}
	}
	
}
