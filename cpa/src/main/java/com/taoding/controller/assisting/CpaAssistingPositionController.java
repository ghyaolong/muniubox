
package com.taoding.controller.assisting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingPosition;
import com.taoding.service.assisting.CpaAssistingPositionService;


/**
 * 职位Controller
 * @author csl
 * @version 2017-11-22
 */
@RestController
@RequestMapping(value = "/assisting/position")
public class CpaAssistingPositionController extends BaseController {

	@Autowired
	private CpaAssistingPositionService cpaPositionService;
	
	/**
	 * 根据id编辑职位的信息
	 * @param cpaPosition
	 * @return
	 */
	@PutMapping(value="/updatePosition")
	public Object updatePosition(@RequestBody CpaAssistingPosition cpaPosition){
		if (StringUtils.isEmpty(cpaPosition.getId())) {
			throw new LogicException("id不能为空！");
		}
		cpaPositionService.updateCpaPosition(cpaPosition);
		return true;
	}
	
	/**
	 * 根据id删除职位
	 * @param
	 * @return
	 * add csl 
	 * 2017-11-22 14:59:56
	 */
	@DeleteMapping("/deletePosition/{id}")
	public Object deletePosition(@PathVariable("id") String id){
		if (StringUtils.isEmpty(id)) {
			throw new LogicException("辅助核算类型中的职位id为空！");
		}
		cpaPositionService.deleteCpaPosition(id);
		return true;
	}
	
	/**
	 * 查询所有的职位
	 * @return
	 */
	@PostMapping("/findPosition")
	public Object findPosition(@RequestBody CpaAssistingPosition cpaPosition){
		return cpaPositionService.findList(cpaPosition);
	}
}