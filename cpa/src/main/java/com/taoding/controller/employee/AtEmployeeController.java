package com.taoding.controller.employee;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.excel.ImportExcel;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.DateUtils;
import com.taoding.domain.user.SysEnterpriseUser;
import com.taoding.domain.user.User;
import com.taoding.service.role.SysRoleGroupService;
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
	@Autowired
	private SysRoleGroupService sysRoleGroupService;

	/**
	 * 员工列表
	 * 
	 * @return
	 * @author mhb
	 */
	@PostMapping("/listData")
	public Object listData(HttpServletRequest request) {
		Map<String,Object> queryMap = getRequestParams(request);
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
		if(user==null){
		 return false;
		}
		 userService.insertUser(user);
		return true;
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
		userService.deleteUser(id);
		return true;
	}

	/**
	 * 根据id查询员工
	 * 
	 * @param id
	 * @return
	 * @author mhb
	 */
	@GetMapping("/editForm/{id}")
	public Object edit(@PathVariable("id") String id) {
		return userService.findUserById(id);
	}

	/**
	 * 员工调机构
	 * 
	 * @param sysEnterpriseUser
	 * @param oldOfficeId
	 * @return
	 * @author mhb
	 */
	@PutMapping("/formAdjusting/{userId}/{newOfficeId}/{oldOfficeId}")
	public Object formAdjusting(@PathVariable("userId") String userId,@PathVariable("oldOfficeId") String newOfficeId,
			@PathVariable("oldOfficeId") String oldOfficeId) {
		sysEnterpriseUserService.updateOfficeUser(userId,newOfficeId, oldOfficeId);
		return "调机构成功!";
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
	@GetMapping("/getUserlistByRole")
	public Object getUserlistByRole(String roleId) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("isPage", true);
		queryMap.put("roleId", roleId);
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
	@GetMapping("/exportData")
	public void exportData(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> queryMap = getRequestParams(request);
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
			List<User> list = ei.getDataList(User.class,1,2);
			if(Collections3.isEmpty(list)){
				return false;
			}
			userService.importData(list);
		return "导入成功";
	}
}
