package com.zw.stm.pojo;


import java.util.List;


public class Role {	

	private Long rid;//���
	private String rolename;//����
	
	private List<Menu> menus;//��ɫ�µĲ˵�Ȩ��
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public boolean equals(Object obj) {
        if (obj instanceof Role) {      
        	Role u = (Role) obj;           
		    return this.rid.equals(u.rid);
		}        
		return super.equals(obj);  
	}
}


