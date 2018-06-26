package com.hy.entity;

import java.util.ArrayList;
import java.util.List;

public class PlatFormButton1  extends  PlatFormButton{


    private String checked;

    private List<PlatFormButton1> data=new ArrayList<PlatFormButton1>();

    public List<PlatFormButton1> getData() {
        return data;
    }

    public void setData(List<PlatFormButton1> data) {
        this.data = data;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    /**
     * 使用递归方法建树
     */
    public static List<PlatFormButton1> buildByRecursive(List<PlatFormButton1> treeNodes) {
        List<PlatFormButton1> trees = new ArrayList<PlatFormButton1>();
        for (PlatFormButton1 menuNode : treeNodes) {
                trees.add(findChildren(menuNode,treeNodes));
        }
        return trees;
    }

    /**
     * 递归查找子节点
     */
    public static PlatFormButton1 findChildren(PlatFormButton1 menuNode,List<PlatFormButton1> menuNodes) {
        List<PlatFormButton1> childMenu=new ArrayList<PlatFormButton1>();
//        for (PlatFormButton1 it : menuNodes) {
//            if(menuNode.getId().equals(it.getParentid())) {
//				/*if (menuNode.getData() == null) {
//					menuNode.setData(new ArrayList<PermMenu>());
//				}*/
//                //menuNode.getData().add(findChildren(it,menuNodes));
//                it.setData(new ArrayList<PlatFormButton1>());
//                childMenu.add(it);
//            }
//        }
        menuNode.setData(childMenu);
        return menuNode;
    }


}
