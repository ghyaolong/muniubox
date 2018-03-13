package com.taoding.service.subject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.subject.CpaSubjectFormatSetting;
import com.taoding.mapper.subject.CpaSubjectFormatSettingDao;

/**
 * 科目Excel导入设置
 * @author fc
 */
@Service
public class CpaSubjectFormatSettingServiceImpl extends DefaultCurdServiceImpl<CpaSubjectFormatSettingDao, CpaSubjectFormatSetting> implements CpaSubjectFormatSettingService {

	@Override
	public boolean saveSubjectSetting(CpaSubjectFormatSetting csfs) {
		boolean isSuccess = true;
		if(StringUtils.isEmpty(csfs.getCustomerId())){
			return false;
		}else{
			CpaSubjectFormatSetting cs = this.getSettingByCustomerId(csfs.getCustomerId());
			if(cs != null && StringUtils.isNotEmpty(cs.getId())){
				if(delSettingById(cs.getId())){
					try{
						this.save(csfs);
					}catch(Exception e){
						isSuccess = false;
					}
				}else{
					isSuccess = false;
				}
			}else{
				try{
					this.save(csfs);
				}catch(Exception e){
					isSuccess = false;
				}
			}	
		}
		return isSuccess;
	}

	@Override
	public CpaSubjectFormatSetting getSettingByCustomerId(String customerId) {
		CpaSubjectFormatSetting csfs = dao.getSettingByCustomerId(customerId);
		return csfs;
	}

	@Override
	public boolean delSettingById(String id) {
		int i = dao.delSettingById(id);
		if(i > 0){
			return true;
		}else{
			return false;	
		}
	}

	
}
