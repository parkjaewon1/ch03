package member;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class MemberDaoImpl implements MemberDao {
	@Autowired
	private JdbcTemplate jt;
	public Member select(String id) {
		Member member = null;
		try {
			member = jt.queryForObject(
				"select * from member3 where id=?",
				new BeanPropertyRowMapper<Member>(Member.class), id);
		}catch (Exception e) {		} // 못 읽으면 값이 null
		return member;
	}
	public int insert(Member member) {
		int result = jt.update(
			"insert into member3 values(?,?,?,?,sysdate)",
			member.getId(),member.getEmail(),
			member.getPassword(),member.getName());
		return result;
	}
	public List<Member> list() {
		List<Member> list = jt.query(
			"select * from member3 order by regdate desc",
			new BeanPropertyRowMapper<Member>(Member.class));
		return list;
	}
	public int update(Member member) {
		int result = jt.update(
			"update member3 set email=?, password=?,name=? where id=?",
				member.getEmail(),member.getPassword(),
				member.getName(),member.getId());
		return result;
	}
	public int delete(String id) {
		return jt.update("delete from member3 where id=?",id);
	}
}