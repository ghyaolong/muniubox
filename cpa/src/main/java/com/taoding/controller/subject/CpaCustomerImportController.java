package com.taoding.controller.subject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.taoding.common.controller.BaseController;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.subject.CpaSubjectFormatSetting;
import com.taoding.service.subject.CpaCustomerImportServiceImpl;
import com.taoding.service.subject.CpaCustomerSaveImportService;
import com.taoding.service.subject.CpaSubjectFormatSettingService;


@RestController
@RequestMapping(value = "/customersubject")
public class CpaCustomerImportController extends BaseController {

	
	@Autowired
	private CpaSubjectFormatSettingService csfsService;
	
	@Autowired
	private CpaCustomerSaveImportService ccsisService;
	
	/**
	 * 期初导入-科目导入
	 * @author fc 
	 * @version 2017年12月22日 下午3:04:07 
	 * @param file
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/import")
	public Object importData(MultipartFile file,String customerId) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("isSuccess", false);
		resultMap.put("message", "");
		if(StringUtils.isNotEmpty(customerId)){
			CpaSubjectFormatSetting csfs = csfsService.getSettingByCustomerId(customerId);
			if(csfs == null){
				resultMap.put("message", "请先设置科目格式");
				}else{
				if(file != null){
					CpaCustomerImportServiceImpl ie = new CpaCustomerImportServiceImpl(file, 0,csfs.getSeperator(),csfs.getMaxLevel(),csfs.getLengthPerLevel());
					return ie.getImportCustomer();
				}else{
					resultMap.put("message","未传入Excel文件！");
				}
			}
		}else{
			resultMap.put("message","传入的客户id不能为空！");
		}
		return resultMap;
	}
	
	/**
	 * 保存导入后关联好的信息
	 * @author fc 
	 * @version 2017年12月22日 下午3:06:39 
	 * @return
	 */
	@PostMapping("/saveimport")
	public Object saveImport(@RequestBody List<CpaCustomerSubject> cpaCustomerSubjects){
		return ccsisService.saveImport(cpaCustomerSubjects);
	}
}
