package com.cognition.bit.common.until;

import com.cognition.bit.system.persistence.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * 树形菜单组装
 * @author taoya
 */
public class BuildTree {

	public static <T> Tree<T> build(List<Tree<T>> nodes) {
		//树节数非空
		if (nodes == null) {
			return null;
		}

		List<Tree<T>> topNodes = new ArrayList<Tree<T>>();
		//循环拆解
		for (Tree<T> children : nodes) {
			String pid = children.getParentId();

			if (pid == null || "0".equals(pid)) {
				topNodes.add(children);
				continue;
			}

			for (Tree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setChildren(true);
					continue;
				}
			}

		}

		Tree<T> root = new Tree<T>();
		if (topNodes.size() == 1) {
			root = topNodes.get(0);
		} else {
			root.setId("-1");
			root.setParentId("");
			root.setHasParent(false);
			root.setChildren(true);
			root.setChecked(true);
			root.setChildren(topNodes);
			root.setText("顶级节点");
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			root.setState(state);
		}

		return root;
	}

	/**
	 *
	 * @param nodes  实体类Tree对象数列
	 * @param idParam
	 * @param <T>
	 * @return
	 */
	public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
		if (nodes == null) {
			return null;
		}

		//初始化树列
		List<Tree<T>> topNodes = new ArrayList<Tree<T>>();
		//循环传进来的额数列
		for (Tree<T> children : nodes) {
			//获取该列的父级对象
			String pid = children.getParentId();
			//如果等于空或者等于0，则加入到列中
			if (pid == null || idParam.equals(pid)) {
				topNodes.add(children);
				continue;
			}
			//
			for (Tree<T> parent : nodes) {
				String id = parent.getId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setChildren(true);
					continue;
				}
			}
		}
		return topNodes;
	}

}