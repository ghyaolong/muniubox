package com.taoding.controller.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.authentication.JwtUtil;
import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.JsonUtil;
import com.taoding.common.utils.UserUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.user.LoginEntity;
import com.taoding.domain.user.User;
import com.taoding.service.menu.MenuService;
import com.taoding.service.user.UserService;

@RestController
public class LoginController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;// 菜单服务
	
	@PostMapping("/login")
	public Object login(@RequestBody LoginEntity loginEntity,HttpServletResponse response)		
			throws Exception {
		// 校验用户名密码
		User user = userService.getUserByLoginName(loginEntity.getLoginName());
		if (user != null) {
			if (Global.NO.equals(user.getLoginFlag())) {
				throw new LogicException("该已帐号禁止登录.");
			}
			
			if (StringUtils.isEmpty(loginEntity.getPassword())) {
				throw new LogicException("密码不能为空.");
			}
			
			if (!UserUtils.validatePassword(loginEntity.getPassword(), user.getPassword())) {
				throw new LogicException("密码不正确.");
			}

			// 登陆成功 返回菜单权限map 
			Map<String, Object> map = new HashMap<>();
			map.put("userId", user.getId());
			map.put("userName", user.getName());
			map.put("loginName", user.getLoginName());
			//后面要改变成真的企业id
			map.put("enterpriseId", user.getEnterpriseMarking());
			map.put("exp", System.currentTimeMillis() + 1800000);
			String sign = JwtUtil.sign(JsonUtil.objectToJson(map));
			response.addHeader("Authorization", "Bearer " + sign);
			return  menuService.getMapByUser(user);
			
		} else {
			return false;
		}
	}
	
	//根据用户名、手机号、邮箱 登录
	@PostMapping(value = "/loginRandomWays")
	public Object findUserInfo(String info, String password){
		User user = userService.findUserInfo(info);
		if(!UserUtils.validatePassword(password, user.getPassword())) {
			return "密码不正确";
		}else {
			return "true";
		}
	}
	
	
}
