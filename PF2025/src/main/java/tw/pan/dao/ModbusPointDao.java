package tw.pan.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.pan.entity.po.ModbusPoint;

@Repository
public class ModbusPointDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<ModbusPoint> selectAllModbusPoint() {
		String sqlStr = "";
		sqlStr += "SELECT i.point_id, i.io_type, i.arithmetic, ";
		sqlStr += "i.correct, m.`function`, m.`slave`, m.address, m.`format` ";
		sqlStr += "FROM pf.modbus_point AS m ";
		sqlStr += "LEFT JOIN pf.point_info AS i ";
		sqlStr += "ON m.point_id = i.point_id ";
		sqlStr += "ORDER BY m.`slave`, m.address ASC";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<ModbusPoint>(ModbusPoint.class));
	}
	
	public ModbusPoint selectModbusPointByPointId(Integer pointId) {
		String sqlStr = "";
		sqlStr += "SELECT g.ip, g.`port`, m.`function`, m.`slave`, m.address, m.`format` ";
		sqlStr += "FROM pf.modbus_point AS m ";
		sqlStr += "INNER JOIN pf.gateway_info AS g ";
		sqlStr += "ON m.gateway_id = g.gateway_id ";
		sqlStr += "WHERE m.point_id = ?";
		return jdbcTemplate.queryForObject(sqlStr, new BeanPropertyRowMapper<ModbusPoint>(ModbusPoint.class));
	}
	
	public List<ModbusPoint> selectModbusPointByGatewayAndSlave(Integer gatewayId, Integer slave) {
		String sqlStr = "";
		sqlStr += "SELECT i.point_id, i.io_type, i.arithmetic, ";
		sqlStr += "i.correct, m.`function`, m.`slave`, m.address, m.`format` ";
		sqlStr += "FROM pf.modbus_point AS m ";
		sqlStr += "LEFT JOIN pf.point_info AS i ";
		sqlStr += "ON m.point_id = i.point_id ";
		sqlStr += "WHERE m.gateway_id = ? AND m.`slave` = ? ";
		sqlStr += "ORDER BY m.address ASC";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<ModbusPoint>(ModbusPoint.class), gatewayId, slave);
	}
	
	public List<Integer> selectModbusPointSlaveByGatewayId(Integer gatewayId) {
		String sqlStr = "";
		sqlStr += "SELECT m.`slave` ";
		sqlStr += "FROM pf.modbus_point AS m ";
		sqlStr += "LEFT JOIN pf.point_info AS i ";
		sqlStr += "ON m.point_id = i.point_id ";
		sqlStr += "WHERE m.gateway_id = ? ";
		sqlStr += "GROUP BY m.`slave` ";
		sqlStr += "ORDER BY m.`slave`";
		return jdbcTemplate.queryForList(sqlStr, Integer.class, gatewayId);
	}
	
}
