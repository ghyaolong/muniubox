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
import com.taoding.domain.subject.CpaSubjectCatalog;
import com.taoding.service.subject.CpaSubjectCatalogService;


/**
 * 科目类型
 * @author czy
 * 2017年11月17日 下午2:10:48
 */
@RestController
@RequestMapping(value = "/subjectcatalog")
public class CpaSubjectCatalogController extends BaseController {

	@Autowired
	private CpaSubjectCatalogService cpaSubjectCatalogService ;
	
	
	/**
	 * 查询全部
	 * 2017年11月17日 下午3:32:38
	 * @return
	 */
	@GetMapping("listData")
	public Object listData(){
		return cpaSubjectCatalogService.findAllList() ;
	}
	
	/**
	 * 根据ID查询科目类型
	 * 
	 * @return
	 */
	@GetMapping("/getInfo/{id}")
	public Object getInfo(@PathVariable("id") String id) {
		if(StringUtils.isNotEmpty(id)){
			return cpaSubjectCatalogService.get(id);
		}
		throw new LogicException("科目类型ID为空") ;
	}
	
    /**
     * 新增科目类型
     * 2017年11月18日 下午2:12:13
     * @param cpaSubjectCatalog
     * @return
     */
    @PutMapping("/save")
    public Object save(@RequestBody CpaSubjectCatalog cpaSubjectCatalog)  {
    	return cpaSubjectCatalogService.insertCpaSubjectCatalog(cpaSubjectCatalog) ;
    } 
    
    /**
     * 修改科目类型
     * 2017年11月18日 下午2:11:58
     * @param cpaSubjectCatalog
     * @return
     */
    @PutMapping("/update")
    public Object update(@RequestBody CpaSubjectCatalog cpaSubjectCatalog)  {
    	return cpaSubjectCatalogService.updateCpaSubjectCatalog(cpaSubjectCatalog);
    } 
	
    /**
     * 根据id删除科目类型
     * 2017年11月18日 下午2:41:21
     * @param id
     * @return
     */
	@PutMapping("/delete/{id}")
	public Object delete(@PathVariable("id") String id) {
		if(StringUtils.isNotEmpty(id)){
			return cpaSubjectCatalogService.deleteById(id);
		}
		throw new LogicException("科目类型ID为空") ;
	}
    
}
