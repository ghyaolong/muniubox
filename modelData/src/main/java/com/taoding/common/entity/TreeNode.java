package com.taoding.common.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
public class TreeNode {
	
	public TreeNode(){
		
	}
	public TreeNode(String id, String label) {
		this.id = id;
		this.label = label;
	}
    
	private String id;//树形结构ID
	
	private String label;//树形结构名称
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String attr;//用于树形结构附加的属相
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<TreeNode> children = new ArrayList<TreeNode>();//子列表
}
