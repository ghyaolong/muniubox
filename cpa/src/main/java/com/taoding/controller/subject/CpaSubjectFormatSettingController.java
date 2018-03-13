package com.taoding.controller.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.utils.DictUtils;
import com.taoding.controller.dict.DictController;
import com.taoding.domain.subject.CpaSubjectFormatSetting;
import com.taoding.service.subject.CpaSubjectFormatSettingService;

/**
 * 科目Excel导入设置
 * @author fc
 */
@RestController
@RequestMapping(value = "/subjectsetting")
public class CpaSubjectFormatSettingController extends BaseController {

	@Autowired
	private CpaSubjectFormatSettingService csfsService;
	
	/**
	 * 保存科目Excel导入设置
	 * @author fc 
	 * @version 2017年12月20日 上午9:31:26 
	 * @param csfs
	 * @return
	 */
	@PostMapping("/savesetting")
	public Object saveSubjectSetting(@RequestBody CpaSubjectFormatSetting csfs){
		return csfsService.saveSubjectSetting(csfs);
	}
	
	/**
	 * 根据客户id获取Excel导入设置详情
	 * @author fc 
	 * @version 2017年12月20日 上午9:38:02 
	 * @param id
	 * @return
	 */
	@GetMapping("/getsettingdetail/{customerId}")
	public Object getSettingByCustomerId(@PathVariable("customerId") String customerId){
		CpaSubjectFormatSetting csfs = csfsService.getSettingByCustomerId(customerId);
		return csfs;
	}
	
	/**
	 * 判断当前客户是否进行过了科目设置
	 * @author fc 
	 * @version 2017年12月20日 下午2:43:08 
	 * @param customerId
	 * @return
	 */
	@GetMapping("issetting/{customerId}")
	public Object isSetting(@PathVariable("customerId") String customerId){
		CpaSubjectFormatSetting csfs = csfsService.getSettingByCustomerId(customerId);
		if(csfs != null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取分隔符列表
	 * @author fc 
	 * @version 2017年12月20日 下午4:28:49 
	 * @return
	 */
	@GetMapping("/getseparator")
	public Object getSeparator(){
		return DictUtils.getDictList("cpa_subject_seperator");
	}
}
