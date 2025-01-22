package tw.pan.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.pan.entity.po.PointInfo;

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
	
	/**
	 * [select] -> find pointInfo by pointId
	 * @param pointId
	 * @return PointInfo only has pointId, pointName, comm, ioType, unit
	 */
	public PointInfo selectPointInfoById(Integer pointId) {
		String sqlStr = "";
		sqlStr += "SELECT i.point_id, i.point_name, i.comm, i.io_type, i.unit ";
		sqlStr += "FROM pf.point_info AS i ";
		sqlStr += "WHERE i.point_id = ?";
		return jdbcTemplate.queryForObject(sqlStr, new BeanPropertyRowMapper<PointInfo>(PointInfo.class), pointId);
	}
	
	
}
