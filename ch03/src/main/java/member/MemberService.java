package member;
public interface MemberService {
	int insert(Member member);
	void select(String id);
	void list();
	int update(Member member);
	int delete(String id);

}