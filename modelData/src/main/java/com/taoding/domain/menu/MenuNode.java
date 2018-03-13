package com.taoding.domain.menu;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * 资源树树形结构展示
 * 2017年10月20日15:07:16
 * @author lixc
 */
public class MenuNode {
private int level;//层级
private String name;
private String treeId;
private String parentId;
private String classParnets;
private List<MenuNode> nodeList;//子节点
private boolean isChecked;//是否勾选



public int getLevel() {
	return level;
}

public void setLevel(int level) {
	this.level = level;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getTreeId() {
	return treeId;
}

public void setTreeId(String treeId) {
	this.treeId = treeId;
}

public List<MenuNode> getNodeList() {
	if(null==nodeList)
		nodeList= new ArrayList();
	return nodeList;
}

public void setNodeList(List<MenuNode> nodeList) {
	this.nodeList = nodeList;
}

public boolean isChecked() {
	return isChecked;
}

public void setChecked(boolean isChecked) {
	this.isChecked = isChecked;
}

public String getParentId() {
	return parentId;
}

public void setParentId(String parentId) {
	this.parentId = parentId;
}


public String getClassParnets() {
	return classParnets;
}

public void setClassParnets(String classParnets) {
	this.classParnets = classParnets;
}


//checked=\"true\"
@SuppressWarnings("serial")
private static Map<Integer, String> LevelHtmlList=new HashMap<Integer, String>(){{
	put(1, "<p class=\"title $classParnets$\" cid=\"$cid$\"  pid=\"$pid$\"><input type=\"checkbox\"  parentId=\"$parentId$\"  $checked$ id=\"$treeId$\" /><label for=\"$treeId$\">$treeName$</label></p>");//根节点
	put(2, "<p class=\"secontTitle $classParnets$\" cid=\"$cid$\"  pid=\"$pid$\" ><input type=\"checkbox\" $checked$ parentId=\"$parentId$\" id=\"$treeId$\" /><label for=\"$treeId$\">$treeName$</label></p>");//第二层
	put(3, "<input type=\"checkbox\"  $checked$  parentId=\"$parentId$\" id=\"$treeId$\" /><label for=\"$treeId$\">$treeName$</label>");//第二层
	}};

@Override
	public String toString() {
	String strHtml=LevelHtmlList.get(level);
	if(3>=level){
		strHtml= StringUtils.replace(strHtml, "$cid$", treeId);
		strHtml= StringUtils.replace(strHtml, "$pid$", parentId);
		strHtml= StringUtils.replace(strHtml, "$classParnets$", classParnets);
		
		strHtml= StringUtils.replace(strHtml, "$treeName$", name);
		strHtml= StringUtils.replace(strHtml, "$treeId$", treeId);
		strHtml= StringUtils.replace(strHtml, "$parentId$", parentId);
		if(isChecked){
			strHtml= StringUtils.replace(strHtml, "$checked$", "checked=\"true\"");
		}else{
			strHtml= StringUtils.replace(strHtml, "$checked$", "");
		}
		return null==strHtml?"":strHtml;
	}
		return "";
	}

}
