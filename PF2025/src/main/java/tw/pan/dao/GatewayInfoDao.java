package tw.pan.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.pan.entity.po.GatewayInfo;

@Repository
public class GatewayInfoDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<GatewayInfo> selectAllGatewayInfo() {
		String sqlStr = "";
		sqlStr += "SELECT gi.gateway_id, gi.ip, gi.`port` ";
		sqlStr += "FROM pf.gateway_info AS gi ";
		sqlStr += "ORDER BY gi.gateway_id ASC";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<GatewayInfo>(GatewayInfo.class));
	}

}
