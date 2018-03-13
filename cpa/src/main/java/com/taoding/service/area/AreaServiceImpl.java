package com.taoding.service.area;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taoding.common.entity.TreeNode;
import com.taoding.common.service.DefaultTreeServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.area.Area;
import com.taoding.mapper.area.AreaDao;

@Service
public class AreaServiceImpl extends DefaultTreeServiceImpl<AreaDao, Area>
		implements AreaService {

	@Override
	public Object getTreeArea() {
		TreeNode treeNode = treeData("1");
		return treeNode;

	}

	private TreeNode treeData(String id) {
		Area area = dao.get(id);
		TreeNode tree = null;
		if (StringUtils.isNotEmpty(area.getId())&& StringUtils.isNotEmpty(area.getName())) {
			tree = new TreeNode(area.getId(), area.getName());
			List<Area> childrenAreaList = dao.getAreaTreeChildrenByParentId(area.getId());
			for (Area childrenArea : childrenAreaList) {
				if (StringUtils.isNotEmpty(childrenArea.getId())) {
					TreeNode treeNode = treeData(childrenArea.getId());
					tree.getChildren().add(treeNode);
				}
			}
		}
		return tree;
	}
}
