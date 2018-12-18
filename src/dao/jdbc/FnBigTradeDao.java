package dao.jdbc;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import model.TradePrivilege;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.commons.dbcp.PoolableConnection;
import org.springframework.jdbc.core.ParameterMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.util.Assert;

public class FnBigTradeDao extends JdbcDaoSupport {

	

	public String batchAddPrivilege(final String type, final String members, String firmids,
			TradePrivilege entity) {
		final Object [] firmid = firmids.split(",");
		final TradePrivilege privilege = entity;
		BatchAddPrivilegeProcedure sfunc = new BatchAddPrivilegeProcedure(this.getDataSource());
		
		ParameterMapper parameterMapper = new ParameterMapper() {
			@Override
			public Map createMap(Connection conn) throws SQLException {
				conn = ((PoolableConnection) conn.getMetaData().getConnection()).getDelegate();
				
				Map inputs = new HashMap();
				inputs.put("p_type", type);
				inputs.put("p_members", members);
				StructDescriptor recDesc = StructDescriptor.createDescriptor(
						"K_TYPE_Privilege_firmids", conn);
				ArrayList<STRUCT> pstruct = new ArrayList<STRUCT>();
				STRUCT item = new STRUCT(recDesc, conn, firmid);
				pstruct.add(item);
				
				ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor(
						"k_type_array_privilege_firmids", conn);
				ARRAY vArray = new ARRAY(tabDesc, conn, pstruct
						.toArray(new STRUCT[pstruct.size()]));

				inputs.put("p_arr_privilege_firmids", vArray);
				inputs.put("p_kind", privilege.getKind());
				inputs.put("p_kindid", privilege.getKindID());
				inputs.put("p_privilegecode_b", privilege.getPrivilegeCode_B());
				inputs.put("p_privilegecode_s", privilege.getPrivilegeCode_S());

				return inputs;
			}

		};
		Map results = sfunc.execute(parameterMapper);
		return String.valueOf(((Integer) results.get("ret")).intValue());
	}
	private class BatchAddPrivilegeProcedure extends StoredProcedure {
		private static final String SFUNC_NAME = "fn_k_batchaddprivilege";

		public BatchAddPrivilegeProcedure(DataSource ds) {
			super(ds, SFUNC_NAME);
			setFunction(true);
			declareParameter(new SqlOutParameter("ret", Types.VARCHAR));
			declareParameter(new SqlParameter("p_type", Types.VARCHAR));
			declareParameter(new SqlParameter("p_members", Types.VARCHAR));
			declareParameter(new SqlParameter("p_arr_privilege_firmids", Types.ARRAY));
			declareParameter(new SqlParameter("p_kind", Types.INTEGER));
			declareParameter(new SqlParameter("p_kindid", Types.VARCHAR));
			declareParameter(new SqlParameter("p_privilegecode_b", Types.INTEGER));
			declareParameter(new SqlParameter("p_privilegecode_s", Types.INTEGER));
			compile();
		}

		public Map execute(Map map) {
			return super.execute(map);
		}
	}
}
