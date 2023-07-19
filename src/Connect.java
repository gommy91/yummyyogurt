import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
	private Statement st;
	private Connection conn;
	
	public Connect() {
		try {  
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yogurt","root","");
			st = conn.createStatement(1004, 1008);
			System.out.println("Connection Successful");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection Error");
		}
	}

	public ResultSet executeQuery(String query) {
		ResultSet rs = null;
		try {
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connection Error");
		}
		return rs;
	}

	public void executeUpdate(String query) {
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connection Error");
		}
	}
}