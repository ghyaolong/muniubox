package com.taoding.controller.employee;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.excel.ImportExcel;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.user.User;
import com.taoding.service.user.SysEnterpriseUserService;
import com.taoding.service.user.UserService;

@RestController
@RequestMapping(value = "/employee")
public class AtEmployeeController extends BaseController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;
	@Autowired
	private SysEnterpriseUserService sysEnterpriseUserService;

	/**
	 * 员工列表
	 * 
	 * @return
	 * @author mhb
	 */
	@PostMapping("/listData")
	public Object listData(@RequestBody Map<String,Object> queryMap) {
		return userService.findAiUser(queryMap);
	}

	/**
	 * 员工添加
	 * 
	 * @param user
	 * @return
	 * @author mhb
	 */
	@PostMapping("/save")
	public Object save(@RequestBody User user) {
		return userService.insertUser(user);
	}
	
	/**
	 * 恢复初始密码
	 * 
	 * @param user
	 * @return
	 * @author mhb
	 */
	@PutMapping("/initPassword/{id}")
	public Object userinitPassword(@PathVariable("id") String id) {
		return userService.initPassWord(id);
	}

	/**
	 * 员工删除
	 * 
	 * @param id
	 * @return
	 * @author mhb
	 */
	@PutMapping("/delete/{id}")
	public Object delete(@PathVariable("id") String id) {
		return userService.deleteUser(id);
	}
	/**
	 * 获取员工工号
	 * @return
	 * @author mhb
	 */
	@GetMapping("/getNextNo")
	public Object getNextNo() {
		return userService.getNextNo();
	}

	/**
	 * 员工调机构
	 * 
	 * @param userId  员工id
	 * @param newOfficeId  调入后的组织机构id
	 * @param oldOfficeId  调入前的组织机构id
	 * @return
	 * @author mhb
	 */
	@PutMapping("/formAdjusting/{userId}/{newOfficeId}/{oldOfficeId}")
	public Object formAdjusting(@PathVariable("userId") String userId,@PathVariable("newOfficeId") String newOfficeId,
			@PathVariable("oldOfficeId") String oldOfficeId) {
		return sysEnterpriseUserService.updateOfficeUser(userId,newOfficeId, oldOfficeId);
	}

	/**
	 * 获得用户列表
	 * 
	 * @param roleId
	 *            角色Id
	 * @param queryCondition
	 *            查询条件
	 * @return
	 * @author lixc
	 * @date 2017年11月13日14:04:58
	 */
	@PostMapping("/getUserlistByRole")
	public Object getUserlistByRole(@RequestBody Map<String,Object> queryMap) {
		if(null ==queryMap || null==queryMap.get("roleId") || "".endsWith(queryMap.get("roleId").toString())){
			throw new LogicException("角色Id不能为空！");
		}
		return userService.findAllUserByPage(queryMap);
	}

	/**
	 * 员工列表导出
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @author mhb
	 */
	@PostMapping("/exportData")
	public void exportData(@RequestBody Map<String,Object> queryMap,
			HttpServletResponse response) {
		PageInfo<User> pages = userService.findAiUser(queryMap);
		String fileName = "员工数据" + DateUtils.getDate("yyyyMMddHHmmss")+ ".xlsx";
		try {
			int is[] = new int[2];
			is[0] = 1;
			is[1] = 2;
			new ExportExcel("员工数据", User.class,1,1,2).setDataList(pages.getList()).write(response, fileName).dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 导入
	 * 
	 * @param file
	 * @param response
	 * @return
	 * @author mhb
	 */
	@PostMapping("/import")
	public Object importData(MultipartFile file, HttpServletResponse response) throws Exception {
		 
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class,1,2,4);
			if(Collections3.isEmpty(list)){
				throw new LogicException("导入数据为空");
			}
			
		return userService.importData(list);
	}
}
