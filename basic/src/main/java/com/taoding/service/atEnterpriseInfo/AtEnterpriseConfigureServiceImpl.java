package com.taoding.service.atEnterpriseInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseConfigure;
import com.taoding.mapper.atEnterpriseInfo.AtEnterpriseConfigureDao;

/**
 * 企业配置信息Service
 * 
 * @author mhb
 * @version 2017-10-26
 */
@Service
public class AtEnterpriseConfigureServiceImpl
		extends DefaultCurdServiceImpl<AtEnterpriseConfigureDao, AtEnterpriseConfigure>
		implements AtEnterpriseConfigureService {

	/**
	 * 保存企业配置信息
	 */
	@Transactional(readOnly = false)
	public void save(AtEnterpriseConfigure atEnterpriseConfigure) {
		AtEnterpriseConfigure atEnc = dao.findAtEnterpriseConfigureForAtEnterpriseInfoId(atEnterpriseConfigure);
		if (atEnc != null) {
			// 删除表对应关系
			dao.delete(atEnc);
		}
		super.save(atEnterpriseConfigure);
	}

	/**
	 * 根据企业id获取企业配置信息
	 */
	@Override
	public AtEnterpriseConfigure findAtEnterpriseConfigureForAtEnterpriseInfoId(
			AtEnterpriseConfigure atEnterpriseConfigure) {
		return dao.findAtEnterpriseConfigureForAtEnterpriseInfoId(atEnterpriseConfigure);
	}

	/**
	 * 上传图片完成后将图片路径保存到AtEnterpriseConfigure中
	 */
	@Override
	public int updateConfigure(AtEnterpriseConfigure atEnterpriseConfigure) {
		//通过atEnterpriseInfoId查找是否存在该对象
		AtEnterpriseConfigure configure = dao.getConfigure(atEnterpriseConfigure);
		//如果AtEnterpriseConfigure的id不存在，则插入该条数据
		if (StringUtils.isEmpty(configure.getId())) {
			dao.insert(atEnterpriseConfigure);
		}
		return dao.updateConfigure(atEnterpriseConfigure);
		
	}
}