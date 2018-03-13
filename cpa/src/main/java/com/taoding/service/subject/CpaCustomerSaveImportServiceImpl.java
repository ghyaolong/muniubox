package com.taoding.service.subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.domain.assisting.CpaCustomerAssisting;
import com.taoding.domain.subject.CpaAssistingData;
import com.taoding.domain.subject.CpaAssistingEntity;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.service.assisting.CpaCustomerAssistingService;

@Service
public class CpaCustomerSaveImportServiceImpl  implements CpaCustomerSaveImportService{

	@Autowired
	private CpaCustomerSubjectService cpaCustomerSubjectService;
	
	@Autowired
	private CpaCustomerAssistingService cpaCustomerAssistingService;
	
	@Override
	@Transactional
	public boolean saveImport(List<CpaCustomerSubject> cpaCustomerSubjectList) {
		for(CpaCustomerSubject cpaCustomerSubject : cpaCustomerSubjectList){
			if(StringUtils.isNotEmpty(cpaCustomerSubject.getId())){ //合并操作（修改科目金额）
				cpaCustomerSubjectService.updateSubjectMoney(cpaCustomerSubject);
			}else{ //导入下一级 
				if(cpaCustomerSubject.getAssistingdata() != null && cpaCustomerSubject.getAssistingdata().length > 0){  //插入辅助核算项
					//更新父级为辅助核算项 --需要优化，这里有可能会做多次重复动作
					CpaCustomerSubject parent =  cpaCustomerSubjectService.findBySubjectNo(cpaCustomerSubject.getBookId(),cpaCustomerSubject.getParent());
					if(!parent.isAssisting()){
						parent.setAssistingdata(cpaCustomerSubject.getAssistingdata());
						cpaCustomerSubjectService.updateCustomerSubject(parent);
						if(cpaCustomerSubject.getAssistingdata().length <= 0){
							throw new LogicException("插入辅助核算失败！");
						}
					}
					//插入辅助核算项
					CpaAssistingData cpaAssistingData = new CpaAssistingData();
					cpaAssistingData.setId(parent.getId());
					cpaAssistingData.setAssistingSize(cpaCustomerSubject.getAssistingdata().length);
					List<CpaAssistingEntity> cpaAssistingList = new ArrayList<CpaAssistingEntity>();
					for(int i = 0;i < cpaCustomerSubject.getAssistingdata().length; i++){
						
						CpaCustomerAssisting cpaCustomerAssisting = cpaCustomerAssistingService.get(cpaCustomerSubject.getAssistingdata()[i]);
						String assistingInfoType=cpaCustomerAssisting.getAssistingInfoType(); 
						//构造cpaAssistingData
						CpaAssistingEntity cpaAssistingEntity = new CpaAssistingEntity();
						//获取辅助核算编号
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("id",cpaCustomerSubject.getAssistingdata()[i]);
						map.put("accountId",cpaCustomerSubject.getBookId());
						if(CpaCustomerAssisting.TYPE_CONSTOMER.equals(assistingInfoType) || CpaCustomerAssisting.TYPE_PROVIDER.equals(assistingInfoType)
								 || CpaCustomerAssisting.TYPE_EMPLOYEE.equals(assistingInfoType) || CpaCustomerAssisting.TYPE_PAYMENT.equals(assistingInfoType)
								 || CpaCustomerAssisting.TYPE_CUSTOM.equals(assistingInfoType)){
							map.put("name",cpaCustomerSubject.getSubjectName());
						}
						if(CpaCustomerAssisting.TYPE_DEPART.equals(assistingInfoType)){
							map.put("departmentName",cpaCustomerSubject.getSubjectName());
						}
						if(CpaCustomerAssisting.TYPE_GOODS.equals(assistingInfoType)){
							map.put("goodsName",cpaCustomerSubject.getSubjectName());
						}
						if(CpaCustomerAssisting.TYPE_POSITION.equals(assistingInfoType)){
							map.put("positionName",cpaCustomerSubject.getSubjectName());
						}
						if(CpaCustomerAssisting.TYPE_PROJECT.equals(assistingInfoType)){
							map.put("projectName",cpaCustomerSubject.getSubjectName());
						}
						String cpaAssistingNo = cpaCustomerAssistingService.insertAllDetail(map) + "";
						cpaAssistingEntity.setNo(cpaAssistingNo);
						cpaAssistingEntity.setName(cpaCustomerSubject.getSubjectName());
						cpaAssistingEntity.setType(assistingInfoType);
						cpaAssistingList.add(cpaAssistingEntity);
					}
					cpaAssistingData.setLists(cpaAssistingList);
					//新增辅助核算
					cpaCustomerSubjectService.insertAssistingSubject(cpaAssistingData);
				}else{  //插入下一级科目
					cpaCustomerSubjectService.insertCustomerSubject(cpaCustomerSubject);
				}
			}
		}
		return true;
	}

}
