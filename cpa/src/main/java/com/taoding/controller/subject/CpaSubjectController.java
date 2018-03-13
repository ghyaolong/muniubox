package com.taoding.controller.subject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.domain.subject.CpaSubject;
import com.taoding.service.subject.CpaSubjectService;


/**
 * 科目详情
 * @author czy
 * 2017年11月17日 下午2:10:48
 */
@RestController
@RequestMapping(value = "/subject")
public class CpaSubjectController extends BaseController {

	@Autowired
	private CpaSubjectService subjectService ;
	
	/**
	 * 查询全部
	 * 2017年11月17日 下午3:32:38
	 * @return
	 */
	@GetMapping("listData/{type}")
	public Object listData(@PathVariable("type") boolean type){
		return subjectService.findAllList(type) ;
	}
	
	/**
	 * 根据科目id查询科目信息
	 * 
	 * @return
	 */
	@GetMapping("/getInfo/{id}")
	public Object getInfo(@PathVariable("id") String id) {
		if(StringUtils.isNotEmpty(id)){
			return subjectService.get(id);
		}
		throw new LogicException("科目ID为空") ;
	}
	
    /**
     * 新增科目
     * @return
     */
    @PutMapping("/save")
    public Object save(@RequestBody CpaSubject subject)  {
    	return subjectService.insertCpaSubject(subject);
    } 
    
    /**
     * 修改科目
     * @return
     */
    @PutMapping("/update")
    public Object update(@RequestBody CpaSubject subject)  {
    	return subjectService.updateCpaSubject(subject);
    }
    
    /**
     * 根据id删除科目
     * 2017年11月18日 上午10:14:01
     * @param officeId
     * @return
     */
	@PutMapping("/delete/{id}")
	public Object delete(@PathVariable("id") String id) {
		if(StringUtils.isNotEmpty(id)){
			return subjectService.deleteById(id);
		}
		throw new LogicException("科目ID为空") ;
	}
	
    /**
     * 根据id查询子科目信息
     * 2017年11月18日 上午10:14:01
     * @param officeId
     * @return
     */
	@GetMapping("/getChildInfo/{id}")
	public Object getChildInfo(@PathVariable("id") String id) {
		if(StringUtils.isNotEmpty(id)){
			CpaSubject subject = subjectService.get(id);
			if(subject != null){
				return subjectService.findByParentNo(subject.isType(),subject.getSubjectNo());
			}
		}
		throw new LogicException("科目ID为空") ;
	}
	
	/**
	 * 根据科目类型查询科目信息
	 * 2017年11月18日 下午1:07:11
	 * @param parentNo
	 * @return
	 */
	@GetMapping("/getByCatalog/{id}")
	public Object getByCatalog(@PathVariable("id") String catalogId) {
		if(StringUtils.isNotEmpty(catalogId)){
			return subjectService.findAllBycatalogId(catalogId);
		}
		throw new LogicException("科目类型ID不能为空") ;
	}
}
