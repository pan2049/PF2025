package tw.jdi.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;
import tw.jdi.entity.po.PointInfo;

@Repository
public class PointInfoDao {

	@Resource(name = "localJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public List<PointInfo> selectAllPointInfo() {
		String sqlStr = "";
		
	}
}
