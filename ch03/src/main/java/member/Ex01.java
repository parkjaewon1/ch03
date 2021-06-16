package member;
import java.util.Scanner;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
public class Ex01 {
	private static MemberService ms;  // 클래스 변수, 객체 생성하지 사용
	private static Scanner sc;        // 전역변수
	public static void main(String[] args) {
		AbstractApplicationContext ac=new GenericXmlApplicationContext("db.xml");
		ms = ac.getBean(MemberService.class);
		sc = new Scanner(System.in);
		String command = "";
		while(true) {
			help();
			command = sc.nextLine();
			if (command.equals("1")) break;
			else if (command.equals("2")) insert();
			else if (command.equals("3")) update();
			else if (command.equals("4")) delete();
			else if (command.equals("5")) select();
			else if (command.equals("6")) list();
		}
		ac.close(); sc.close();
		System.out.println("프로그램 종료");
	}
	private static void delete() {
		System.out.println("삭제할 아이디를 입력하세요");
		String id = sc.nextLine();
		int result = ms.delete(id);
		if (result > 0) System.out.println("삭제 성공 대박 !!!");
	}
	private static void update() {
		System.out.println("수정할 아이디를 입력하세요");
		String id = sc.nextLine();
		System.out.println("이메일을 입력하세요");
		String email = sc.nextLine();
		System.out.println("암호를 입력하세요");
		String password = sc.nextLine();
		System.out.println("암호 확인을 입력하세요");
		String confirmPass = sc.nextLine();
		System.out.println("이름을 입력하세요");
		String name = sc.nextLine();
		if (!password.equals(confirmPass)) {
			System.out.println("암호와 암호확인이 다릅니다");
			return; 
		}
		Member member = new Member();
		member.setId(id);
		member.setEmail(email);
		member.setPassword(password);
		member.setName(name);
		int result = ms.update(member);
		if (result > 0) System.out.println("수정 성공 ㅋㅋ");
	}
	private static void list() {
		ms.list();		
	}
	private static void select() {
		System.out.println("조회할 아이디를 입력하세요");
		String id = sc.nextLine();
		ms.select(id);
	}
	private static void insert() {
		System.out.println("아이디를 입력하세요");
		String id = sc.nextLine();
		System.out.println("이메일을 입력하세요");
		String email = sc.nextLine();
		System.out.println("암호를 입력하세요");
		String password = sc.nextLine();
		System.out.println("암호 확인을 입력하세요");
		String confirmPass = sc.nextLine();
		System.out.println("이름을 입력하세요");
		String name = sc.nextLine();
		if (!password.equals(confirmPass)) {
			System.out.println("암호와 암호확인이 다릅니다");
			return; 
		}
		Member member = new Member();
		member.setId(id);
		member.setEmail(email);
		member.setPassword(password);
		member.setName(name);
		int result = ms.insert(member);
		if (result > 0) System.out.println("입력 성공 ㅋㅋ");
	}
	private static void help() {
		System.out.println("명령어를 선택하세요");
		System.out.println("1. 종료");
		System.out.println("2. 입력");
		System.out.println("3. 수정");
		System.out.println("4. 삭제");
		System.out.println("5. 조회");
		System.out.println("6. 목록");
	}
}