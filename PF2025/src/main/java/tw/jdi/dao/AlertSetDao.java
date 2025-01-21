package tw.jdi.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.jdi.entity.po.AlertSet;

@Repository
public class AlertSetDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<AlertSet> selectAllAlertSetAIO() {
		String sqlStr = "";
		sqlStr += "SELECT aio.point_id, i.io_type, aio.alert_status, aio.alert_upper, aio.alert_lower ";
		sqlStr += "FROM pf.alert_set_aio AS aio ";
		sqlStr += "INNER JOIN pf.point_info AS i ";
		sqlStr += "ON aio.point_id = i.point_id ";
		sqlStr += "ORDER BY aio.point_id";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<AlertSet>(AlertSet.class));
	}
	
	public List<AlertSet> selectAllAlertSetDIO() {
		String sqlStr = "";
		sqlStr += "SELECT dio.point_id, i.io_type, dio.alert_status, dio.alert_define ";
		sqlStr += "FROM pf.alert_set_dio AS dio ";
		sqlStr += "INNER JOIN pf.point_info AS i ";
		sqlStr += "ON dio.point_id = i.point_id ";
		sqlStr += "ORDER BY dio.point_id";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<AlertSet>(AlertSet.class));
	}
}
