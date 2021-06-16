package member;
import java.util.List;
public interface MemberDao {
	Member select(String id);
	int insert(Member member);
	List<Member> list();
	int update(Member member);
	int delete(String id);

}