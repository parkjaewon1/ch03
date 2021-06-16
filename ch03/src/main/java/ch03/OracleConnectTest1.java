package ch03;
import org.junit.Test;

import java.sql.*;
public class OracleConnectTest1 {
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	private static final String USER = "scott";
	private static final String PW = "tiger";
	@Test
	public void testConn() {
		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(URL, USER, PW);
			System.out.println("연결 성공 ㅋㅋ");
			conn.close();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}