package tw.jdi.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.jdi.entity.po.AlertRecord;

@Repository
public class AlertRecordDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<AlertRecord> selectLastRecordFotInitDIO() {
		String sqlStr = "";
		sqlStr += "SELECT dio.alert_id, dio.point_id ";
		sqlStr += "FROM pf.alert_record_dio AS dio ";
		sqlStr += "WHERE dio.return_time IS NULL";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<AlertRecord>(AlertRecord.class));
	}
	
	public List<AlertRecord> selectLastRecordFotInitAIO() {
		String sqlStr = "";
		sqlStr += "SELECT aio.alert_id, aio.point_id ";
		sqlStr += "FROM pf.alert_record_aio AS aio ";
		sqlStr += "WHERE aio.return_time IS NULL";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<AlertRecord>(AlertRecord.class));
	}
	
	public Integer insertAlertRecordDIO(Integer pointId, Boolean state) {
		String sqlStr = "INSERT INTO `pf`.`alert_record_dio` (`point_id`, `alert_status`) VALUES (?, ?)";
		jdbcTemplate.update(sqlStr, pointId, state ? "ON" : "OFF");
		String sqlStr2 = "SELECT LAST_INSERT_ID() FROM pf.alert_record_dio";
		return jdbcTemplate.queryForObject(sqlStr2, Integer.class);
	}
	
	public void updateAlertRecordDIO(Integer alertId, String time) {
		String sqlStr = "UPDATE `pf`.`alert_record_dio` SET `return_time`= ? WHERE `alert_id`= ?";
		jdbcTemplate.update(sqlStr, time, alertId);
	}
	
	public Integer insertAlertRecordAIO(Integer pointId, Float value) {
		String sqlStr = "INSERT INTO `pf`.`alert_record_aio` (`point_id`, `alert_value`) VALUES (?, ?)";
		jdbcTemplate.update(sqlStr, pointId, value);
		String sqlStr2 = "SELECT LAST_INSERT_ID() FROM pf.alert_record_aio";
		return jdbcTemplate.queryForObject(sqlStr2, Integer.class);
	}
	
	public void updateAlertRecordAIO(Integer alertId, String time) {
		String sqlStr = "UPDATE `pf`.`alert_record_aio` SET `return_time`= ? WHERE `alert_id`= ?";
		jdbcTemplate.update(sqlStr, time, alertId);
	}
	
	
	
}
