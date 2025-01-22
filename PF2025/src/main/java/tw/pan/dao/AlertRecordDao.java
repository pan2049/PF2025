package tw.pan.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.pan.entity.enumEntity.IoType;
import tw.pan.entity.po.AlertRecord;

@Repository
public class AlertRecordDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<AlertRecord> selectLastRecordForInitDIO() {
		String sqlStr = "";
		sqlStr += "SELECT dio.alert_id, dio.point_id ";
		sqlStr += "FROM pf.alert_record_dio AS dio ";
		sqlStr += "WHERE dio.return_time IS NULL";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<AlertRecord>(AlertRecord.class));
	}
	
	public List<AlertRecord> selectLastRecordForInitAIO() {
		String sqlStr = "";
		sqlStr += "SELECT aio.alert_id, aio.point_id ";
		sqlStr += "FROM pf.alert_record_aio AS aio ";
		sqlStr += "WHERE aio.return_time IS NULL";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<AlertRecord>(AlertRecord.class));
	}
	
	public List<AlertRecord> selectAlertRecordByPointId(Integer pointId, IoType ioType, String startTime, String endTime) {
		String sqlStr = "";
		switch(ioType.getSignalType()) {
		case DIGITAL -> {
			sqlStr += "SELECT dio.alert_status, dio.start_time, dio.return_time ";
			sqlStr += "FROM pf.alert_record_dio AS dio ";
			sqlStr += "WHERE dio.point_id = ? AND dio.start_time BETWEEN ? AND ? ";
			sqlStr += "ORDER BY dio.alert_id ASC";			
		}
		case ANALOG -> {
			sqlStr += "SELECT aio.alert_value, aio.start_time, aio.return_time ";
			sqlStr += "FROM pf.alert_record_aio AS aio ";
			sqlStr += "WHERE aio.point_id = ? AND aio.start_time BETWEEN ? AND ? ";
			sqlStr += "ORDER BY aio.alert_id ASC";			
		}
		}
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<AlertRecord>(AlertRecord.class), pointId, startTime, endTime);
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
