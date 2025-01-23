package tw.pan.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.pan.entity.enumEntity.AlertStatus;
import tw.pan.entity.po.AlertSet;

@Repository
public class AlertSetDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<AlertSet> selectAllAlertSetAIO() {
		String sqlStr = "";
		sqlStr += "SELECT aio.point_id, i.point_name, i.io_type, aio.alert_status, aio.alert_upper, aio.alert_lower ";
		sqlStr += "FROM pf.alert_set_aio AS aio ";
		sqlStr += "INNER JOIN pf.point_info AS i ";
		sqlStr += "ON aio.point_id = i.point_id ";
		sqlStr += "ORDER BY aio.point_id";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<AlertSet>(AlertSet.class));
	}
	
	public List<AlertSet> selectAllAlertSetDIO() {
		String sqlStr = "";
		sqlStr += "SELECT dio.point_id, i.point_name, i.io_type, dio.alert_status, dio.alert_define ";
		sqlStr += "FROM pf.alert_set_dio AS dio ";
		sqlStr += "INNER JOIN pf.point_info AS i ";
		sqlStr += "ON dio.point_id = i.point_id ";
		sqlStr += "ORDER BY dio.point_id";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<AlertSet>(AlertSet.class));
	}
	
	public void updateAlertSetDIO(Integer pointId, AlertStatus alertStatus, AlertStatus alertDefine) {
		String sqlStr = "";
		sqlStr += "UPDATE pf.alert_set_dio ";
		sqlStr += "SET `alert_status` = ?, `alert_define` = ? ";
		sqlStr += "WHERE `point_id`= ?";
		jdbcTemplate.update(sqlStr, alertStatus.toString(), alertDefine.toString(), pointId);
	}
	
	public void updateAlertSetAIO(Integer pointId, AlertStatus alertStatus, Float alertUpper, Float alertLower) {
		String sqlStr = "";
		sqlStr += "UPDATE pf.alert_set_aio ";
		sqlStr += "SET `alert_status` = ?, `alert_upper` = ?, `alert_lower` = ? ";
		sqlStr += "WHERE `point_id` = ?";
		jdbcTemplate.update(sqlStr, alertStatus, alertUpper, alertLower, pointId);
	}
}
