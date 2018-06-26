package com.hy.entity;

import java.util.ArrayList;
import java.util.List;

public class PlatFormMenu2 extends PlatFormMenu{


    private List<PlatFormButton1> data=new ArrayList<PlatFormButton1>();

    public List<PlatFormButton1> getData() {
        return data;
    }

    public void setData(List<PlatFormButton1> data) {
        this.data = data;
    }

    private String checked;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    /**
     * 使用递归方法建树
     */
    public static List<PlatFormMenu2> buildByRecursive(List<PlatFormMenu> menu,List<PlatFormButton1> menuNodes) {
        List<PlatFormMenu2> trees = new ArrayList<PlatFormMenu2>();
        for( PlatFormMenu m:menu){
            trees.add(findChildren(m,menuNodes));
        }
        return trees;
    }
    /**
     * 递归查找子节点
     */
    public static PlatFormMenu2 findChildren(PlatFormMenu menuNode, List<PlatFormButton1> menuNodes) {
        PlatFormMenu2 menu2= new PlatFormMenu2();
        List<PlatFormButton1> childMenu=new ArrayList<PlatFormButton1>();
        for (PlatFormButton1 it : menuNodes) {
            if(menuNode.getId().equals(it.getMenuid())) {
				/*if (menuNode.getData() == null) {
					menuNode.setData(new ArrayList<PermMenu>());
				}*/
                //menuNode.getData().add(findChildren(it,menuNodes));
                it.setData(new ArrayList<PlatFormButton1>());
                childMenu.add(it);
            }
        }
        menu2.setName(menuNode.getName());
        menu2.setCode(menuNode.getCode());
        menu2.setId(menuNode.getId());
        menu2.setData(childMenu);
        menu2.setChecked("");
        return menu2;
    }


}
