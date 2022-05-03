// ITMD 510 Final Project by Shuaiqi Zheng

package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Dao.DBConnect;

public class LoginModel extends DBConnect {

	private Boolean admin;
	private int id;
	private String uName;
	private String name;
	private String lic;

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLic() {
		return lic;
	}

	public void setLic(String lic) {
		this.lic = lic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUname() {
		return uName;
	}

	public void setUname(String uName) {
		this.uName = uName;
	}

	public Boolean isAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}


	public Boolean getCredentials(String username, String password) {
		// get credentials from the user table
		String query = "SELECT * FROM sz_user WHERE Username = ? and Password = ?;";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// get some user information for this account if the login is successful
				setId(rs.getInt("ID"));
				setUname(rs.getString("Username"));
				setName(rs.getString("Name"));
				setLic(rs.getString("License"));
				setAdmin(rs.getBoolean("admin"));
				return true;
			}

		} catch (SQLException e) {
			// error occurred
			e.printStackTrace();
		}
		return false;
	}

}// end class