
package com.taoding.controller.assisting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingProject;
import com.taoding.service.assisting.CpaAssistingProjectService;

/**
 * 辅助核算模块项目信息表Controller
 * @author csl
 * @version 2017-11-20
 */

@RestController
@RequestMapping(value = "/assisting/project")
public class CpaAssistingProjectController extends BaseController {
	
	@Autowired
	private CpaAssistingProjectService projectService; 
	/**
	 * 编辑企业项目的条目信息
	 * @param cpaAssistingGoods
	 * @return
	 */
	@PutMapping(value="/updateProject")
	public Object updateProject(@RequestBody CpaAssistingProject cpaAssistingProject){
		if (StringUtils.isEmpty(cpaAssistingProject.getId())) {
			throw new LogicException("项目id不能为空！");
		}
		projectService.updateProject(cpaAssistingProject);
		return true;
	}
	
	/**
	 * 根据id删除项目
	 * @param
	 * @return
	 * add csl 
	 * 2017-11-22 14:59:56
	 */
	@DeleteMapping("/deleteProject/{id}")
	public Object deleteProject(@PathVariable("id") String id){
		if (StringUtils.isEmpty(id)) {
			throw new LogicException("辅助核算类型中的职位id为空！");
		}
		projectService.deleteProject(id);
		return true;
	}

}