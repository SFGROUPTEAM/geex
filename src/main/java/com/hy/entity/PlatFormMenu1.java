package com.hy.entity;

import com.hy.common.StringUtils;
import com.hy.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PlatFormMenu1 extends PlatFormMenu{

    private String checked;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    private List<PlatFormMenu1> data=new ArrayList<PlatFormMenu1>();

    public List<PlatFormMenu1> getData() {
        return data;
    }

    public void setData(List<PlatFormMenu1> data) {
        this.data = data;
    }

    /**
     * 使用递归方法建树
     */
    public static List<PlatFormMenu1> buildByRecursive(List<PlatFormMenu1> treeNodes) {
        List<PlatFormMenu1> trees = new ArrayList<PlatFormMenu1>();
        for (PlatFormMenu1 menuNode : treeNodes) {
            if (StringUtils.isBlank(menuNode.getParentid()) || "0".equals(menuNode.getParentid())) {
                trees.add(findChildren(menuNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     */
    public static PlatFormMenu1 findChildren(PlatFormMenu1 menuNode,List<PlatFormMenu1> menuNodes) {
        List<PlatFormMenu1> childMenu=new ArrayList<PlatFormMenu1>();
        for (PlatFormMenu1 it : menuNodes) {
            if(menuNode.getId().equals(it.getParentid())) {
				/*if (menuNode.getData() == null) {
					menuNode.setData(new ArrayList<PermMenu>());
				}*/
                //menuNode.getData().add(findChildren(it,menuNodes));
                it.setData(new ArrayList<PlatFormMenu1>());
                childMenu.add(it);
            }
        }
        menuNode.setData(childMenu);
        return menuNode;
    }


}
