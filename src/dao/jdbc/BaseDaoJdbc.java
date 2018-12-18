package dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import dao.BaseDao;

/**
 * Excel操作接口实现.
 */
public class BaseDaoJdbc extends JdbcDaoSupport implements BaseDao {

	private LobHandler lobHandler;

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public List getList(String sql) {
		return getJdbcTemplate().queryForList(sql);
	}

	public void insertToDB(String sql) {
		getJdbcTemplate().update(sql);
	}

	public void insertToDB(String sql, final byte[] b) {
		try {
			getJdbcTemplate().execute(sql, new AbstractLobCreatingPreparedStatementCallback(this.lobHandler) {
				protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
					lobCreator.setBlobAsBytes(ps, 1, b);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
