/*******************************************************************
 * RMIConfigUtil.java   2013-3-29
 * Copyright2012  by GNNT Company. All Rights Reserved.
 * @author:liuchao
 * 
 ******************************************************************/
package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <P>
 * ��˵������ȡϵͳ���ù�����<br/>
 * <br/>
 * </p>
 * �޸ļ�¼:<br/>
 * <ul>
 * ���ȡ�����ݶ˿ڣ�������������� rmi���ø�Ϊ��c_trademodule�ж�ȡ mod by liuchao20130426
 * <li>|20120101|xxxx</li>
 * </ul>
 */

public class SysConfig {

	public static final int SYS_MODULEID = 18;
	public static final String SYS_NAME = "front";

	public static Map<String, Object> getRMIConfig(DataSource ds) {
		Map<String, Object> map = null;
		PreparedStatement state = null;
		ResultSet rs = null;
		Connection conn = null;
		Log logger = LogFactory.getLog(SysConfig.class);
		try {
			String sql = "";
			conn = ds.getConnection();
			sql = "select hostip,port,RMIDATAPORT from C_TradeModule where moduleId='" + SYS_MODULEID + "'";
			logger.debug(sql);
			state = conn.prepareStatement(sql);
			// state.setString(1, moduleId);
			rs = state.executeQuery();
			if (rs.next()) {
				map = new HashMap<String, Object>();
				map.put("host", rs.getString(1));
				logger.debug("ip:" + map.get("host"));
				map.put("port", rs.getInt(2));
				logger.debug("port:" + map.get("port"));
				map.put("rmidataport", rs.getInt(3));
				logger.debug("rmidataport:" + map.get("rmidataport"));
			}
			rs.close();
			rs = null;
			state.close();
			state = null;
		} catch (Exception e) {
			logger.error("getRMIConfig error!", e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
				logger.error(ex);
			}
		}
		return map;
	}

}
