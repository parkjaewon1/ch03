package ch03;
import java.sql.Date;
import lombok.Data;
@Data
public class Member {
	private String id;
	private String email;
	private String password;
	private String name; 
	private Date regdate; 
}