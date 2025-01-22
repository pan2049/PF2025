package tw.pan.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.pan.entity.enumEntity.IoType;
import tw.pan.entity.po.RecordPo;

@Repository
public class RecordDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<RecordPo> selectRecordByDate(Integer pointId, IoType ioType, String startTime, String endTime) {
		String sqlStr = "";
		switch(ioType) {
		case DI -> {
			sqlStr += "SELECT r.`status`, r.`time` ";
			sqlStr += "FROM pf_record_di."+ pointId +" AS r ";	
		}
		case DO -> {
			sqlStr += "SELECT r.`status`, r.`time` ";
			sqlStr += "FROM pf_record_do."+ pointId +" AS r ";	
		}
		case AI -> {
			sqlStr += "SELECT r.`value`, r.`time` ";
			sqlStr += "FROM pf_record_ai."+ pointId +" AS r ";			
		}
		case AO -> {
			sqlStr += "SELECT r.`value`, r.`time` ";
			sqlStr += "FROM pf_record_ao."+ pointId +" AS r ";		
		}
		}
		sqlStr += "WHERE r.`time` BETWEEN ? AND ? ";
		sqlStr += "ORDER BY r.record_id ASC";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<RecordPo>(RecordPo.class), startTime, endTime);
	}
	
	
	
	
	
	public void insertRecord(Integer pointId, IoType ioType, Boolean state, String recordTime) {
		String sqlStr = "";
		switch(ioType) {
		case DI -> {
			sqlStr = "INSERT INTO `pf_record_di`.`"+ pointId +"` (`status`, `time`) VALUES (?, ?)";
		}
		case DO -> {
			sqlStr = "INSERT INTO `pf_record_do`.`"+ pointId +"` (`status`, `time`) VALUES (?, ?)";
		}
		default -> {}
		}
		jdbcTemplate.update(sqlStr, state ? "ON" : "OFF", recordTime);
		
	}
	
	public void insertRecord(Integer pointId, IoType ioType, Float value, String recordTime) {
		String sqlStr = "";
		switch(ioType) {
		case AI -> {
			sqlStr = "INSERT INTO `pf_record_ai`.`"+ pointId +"` (`value`, `time`) VALUES (?, ?)";
		}
		case AO -> {
			sqlStr = "INSERT INTO `pf_record_ao`.`"+ pointId +"` (`value`, `time`) VALUES (?, ?)";
		}
		default -> {}
		}
		jdbcTemplate.update(sqlStr, value, recordTime);
	}
}
