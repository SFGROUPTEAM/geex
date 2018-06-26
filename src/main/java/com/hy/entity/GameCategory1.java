package com.hy.entity;

import com.hy.common.StringUtils;
import com.hy.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GameCategory1 extends GameCategory {
    private String checked;
    private boolean disabled;
    private String skin;

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    private List<GameCategory1> data=new ArrayList<GameCategory1>();

    public List<GameCategory1> getData() {
        return data;
    }

    public void setData(List<GameCategory1> data) {
        this.data = data;
    }



    /**
     * 使用递归方法建树
     */
    public static List<GameCategory1> buildByRecursive(List<GameCategory1> treeNodes) {
        List<GameCategory1> trees = new ArrayList<GameCategory1>();
        for (GameCategory1 menuNode : treeNodes) {
            //)
            if ((StringUtils.isBlank(menuNode.getParentid()))|| "00".equals(menuNode.getParentid())  ){
                trees.add(findChildren(menuNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     */
    public static GameCategory1 findChildren(GameCategory1 menuNode, List<GameCategory1> menuNodes) {
        List<GameCategory1> childMenu=new ArrayList<GameCategory1>();
        for (GameCategory1 it : menuNodes) {
            if(menuNode.getId().equals(it.getParentid())) {
//				if (it.getData() == null) {
//					it.setData(new ArrayList<GameCategory1>());
//                    it.getData().add(findChildren(it,menuNodes));
//				}
                //menuNode.getData().add(findChildren(it,menuNodes));
                it.setData(new ArrayList<GameCategory1>());
                it.getData().add(findChildren(it,menuNodes));
                if(it.getData()!=null&&it.getData().size()>0){
                    it.setDisabled(true);
                }else {
                    it.setDisabled(false);
                }
                if (it.getState()==0){
                    it.setSkin("red");
                }
                childMenu.add(it);
            }
        }
        if (menuNode.getState()==0){
            menuNode.setSkin("red");
        }
        menuNode.setDisabled(true);
        menuNode.setData(childMenu);
        return menuNode;
    }
}
