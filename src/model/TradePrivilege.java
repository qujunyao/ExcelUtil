/*******************************************************************
 * TypePrivilege.java   2013-11-15
 * Copyright2013  by GNNT Company. All Rights Reserved.
 * @author:zhusailiu
 * 
 ******************************************************************/
package model;


/**
 *<P>��˵���������̡��ͻ�������ԱȨ����Ϣʵ����<br/>
 *<br/>
 *</p>
 *�޸ļ�¼:<br/>
 *<ul>
 *<li>2013-11-15</li>
 *<li>@author:zhusailiu</li>
 *</ul>
 */

public class TradePrivilege  {
	/**
	 * �ֶ�˵��: TypePrivilege �����л����б��<br/>
	 */
	private static final long serialVersionUID = 886633L;
	
	/**
	 * ����Ȩ��ID
	 */
	private Integer ID;  
	
	/**
	 * ����������type
	 */
	private Integer type;
	
	/**
	 * ������typeID
	 */
	private String typeID;  
	
	/**
	 * Ʒ��/��Ʒkind
	 */
	private Integer kind;
	
	/**
	 * Ʒ��/��Ʒ����kindID
	 */
	private String kindID; 
	
	/**
	 * ��Ȩ��privilegeCode_B
	 */
	private Integer privilegeCode_B;  
	
	/**
	 * ����Ȩ��privilegeCode_S
	 */
	private Integer privilegeCode_S;
	
	
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}



	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}



	public String getTypeID() {
		return typeID;
	}
	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}



	public Integer getKind() {
		return kind;
	}
	public void setKind(Integer kind) {
		this.kind = kind;
	}



	public String getKindID() {
		return kindID;
	}
	public void setKindID(String kindID) {
		this.kindID = kindID;
	}



	public Integer getPrivilegeCode_B() {
		return privilegeCode_B;
	}
	public void setPrivilegeCode_B(Integer privilegeCodeB) {
		privilegeCode_B = privilegeCodeB;
	}



	public Integer getPrivilegeCode_S() {
		return privilegeCode_S;
	}
	public void setPrivilegeCode_S(Integer privilegeCodeS) {
		privilegeCode_S = privilegeCodeS;
	}



	
}
