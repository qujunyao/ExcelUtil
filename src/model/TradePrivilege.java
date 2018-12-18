/*******************************************************************
 * TypePrivilege.java   2013-11-15
 * Copyright2013  by GNNT Company. All Rights Reserved.
 * @author:zhusailiu
 * 
 ******************************************************************/
package model;


/**
 *<P>类说明：交易商、客户、交易员权限信息实体类<br/>
 *<br/>
 *</p>
 *修改记录:<br/>
 *<ul>
 *<li>2013-11-15</li>
 *<li>@author:zhusailiu</li>
 *</ul>
 */

public class TradePrivilege  {
	/**
	 * 字段说明: TypePrivilege 类序列化序列编号<br/>
	 */
	private static final long serialVersionUID = 886633L;
	
	/**
	 * 交易权限ID
	 */
	private Integer ID;  
	
	/**
	 * 交易码类型type
	 */
	private Integer type;
	
	/**
	 * 交易码typeID
	 */
	private String typeID;  
	
	/**
	 * 品种/商品kind
	 */
	private Integer kind;
	
	/**
	 * 品种/商品代码kindID
	 */
	private String kindID; 
	
	/**
	 * 买方权限privilegeCode_B
	 */
	private Integer privilegeCode_B;  
	
	/**
	 * 卖方权限privilegeCode_S
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
