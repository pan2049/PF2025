package tw.jdi.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.jdi.entity.po.PointInfo;

@Repository
public class PointInfoDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * [select] -> find all point 
	 * @return PointInfo only has pointId, ioType
	 */
	public List<PointInfo> selectAllPointId() {
		String sqlStr = "";
		sqlStr += "SELECT i.point_id, i.io_type ";
		sqlStr += "FROM pf.point_info AS i ";
		sqlStr += "ORDER BY i.point_id";
		return jdbcTemplate.query(sqlStr, new BeanPropertyRowMapper<PointInfo>(PointInfo.class));
	}
	
	
}
