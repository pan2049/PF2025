package tw.jdi.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.jdi.entity.enumEntity.IoType;

@Repository
public class RecordDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
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
