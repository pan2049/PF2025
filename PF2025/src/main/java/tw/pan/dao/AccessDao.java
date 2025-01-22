package tw.pan.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.pan.entity.po.Access;

@Repository
public class AccessDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public Access selectAccessInfo(String username) {
		String sqlStr = "";
		sqlStr += "SELECT a.username, a.`access`, a.`password` ";
		sqlStr += "FROM pf.access AS a ";
		sqlStr += "WHERE a.`access` = ?";
		return jdbcTemplate.queryForObject(sqlStr, new BeanPropertyRowMapper<Access>(Access.class));
	}
}
