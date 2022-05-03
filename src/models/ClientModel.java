//ITMD 510 Final Project, Shuaiqi Zheng

package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Dao.DBConnect;

public class ClientModel extends DBConnect {

	private int cit;
	private String lic;
	private String loc;
	private String time;
	private int fine;
	private String officer;
	private String status;

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	public ClientModel() {

		conn = new DBConnect();

	}

	/////////////////////
	// Admin functions //
	/////////////////////

	// display all citations in DB
	public List<ClientModel> getCitations() {
		List<ClientModel> citations = new ArrayList<>();
		// get all citations, order by status
		String sql = "SELECT * FROM sz_ticket WHERE Status != 'Appealed' ORDER BY Status ASC;";
		try {
			conn = new DBConnect();
			// Execute a query
			System.out.println("Fetching records from the database...");
			stmt = conn.getConnection().createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				ClientModel citation = new ClientModel();
				// grab record data by table field name into ClientModel account object
				citation.setCit(resultSet.getInt("TicketNumber"));
				citation.setLic(resultSet.getString("License"));
				citation.setLoc(resultSet.getString("Location"));
				citation.setTime(resultSet.getString("Time"));
				citation.setFine(resultSet.getInt("Fine"));
				citation.setOfficer(resultSet.getString("Officer"));
				citation.setStatus(resultSet.getString("Status"));
				citations.add(citation); // add all ticket data to the arraylist
			}
			conn.getConnection().close();
			System.out.println("Data successfully retrieved.");

		} catch (SQLException e) {
			System.out.println("Error occurred while fetching the data: " + e);
		}
		return citations; // return arraylist
	}

	// display all appeals in DB
	public List<ClientModel> getAppeals() {
		List<ClientModel> appeals = new ArrayList<>();
		// get all appeals
		String sql = "SELECT * FROM sz_ticket WHERE Status = 'Appealed' ";
		try {
			conn = new DBConnect();
			// Execute a query
			System.out.println("Fetching records from the database...");
			stmt = conn.getConnection().createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			while (resultSet.next()) {
				ClientModel appeal = new ClientModel();
				// grab record data by table field name into ClientModel account object
				appeal.setCit(resultSet.getInt("TicketNumber"));
				appeal.setLic(resultSet.getString("License"));
				appeal.setLoc(resultSet.getString("Location"));
				appeal.setTime(resultSet.getString("Time"));
				appeal.setFine(resultSet.getInt("Fine"));
				appeal.setOfficer(resultSet.getString("Officer"));
				appeal.setStatus(resultSet.getString("Status"));
				appeals.add(appeal); // add all ticket data to the arraylist
			}
			conn.getConnection().close();
			System.out.println("Data successfully retrieved.");

		} catch (SQLException e) {
			System.out.println("Error occurred while fetching the data: " + e);
		}
		return appeals; // return arraylist
	}

	/////////////////////
	// User functions ///
	/////////////////////

	// display citations on this license plate number only
	public List<ClientModel> getLicCitations(String licNum) {
		List<ClientModel> citations = new ArrayList<>();
		// using prepared statements
		String sql = "SELECT * FROM sz_ticket WHERE License = ? ORDER BY Status ASC;";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			// pass the value of licNum into "?"
			stmt.setString(1, licNum);
			// Execute a query
			conn = new DBConnect();
			System.out.println("Fetching records from the database...");
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				ClientModel citation = new ClientModel();
				// grab record data by table field name into ClientModel account object
				citation.setCit(resultSet.getInt("TicketNumber"));
				citation.setLic(resultSet.getString("License"));
				citation.setLoc(resultSet.getString("Location"));
				citation.setTime(resultSet.getString("Time"));
				citation.setFine(resultSet.getInt("Fine"));
				citation.setOfficer(resultSet.getString("Officer"));
				citation.setStatus(resultSet.getString("Status"));
				citations.add(citation); // add all ticket data to the arraylist
			}
			conn.getConnection().close();
			System.out.println("Data successfully retrieved.");

		} catch (SQLException e) {
			System.out.println("Error occurred while fetching the data: " + e.getMessage());
		}
		return citations; // return arraylist
	}

	// display active appeals on this license plate number only
	public List<ClientModel> getLicAppeals(String licNum) {
		List<ClientModel> appeals = new ArrayList<>();
		// using prepared statements
		String sql = "SELECT * FROM sz_ticket WHERE License = ? AND Status = 'Appealed';";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			// pass the value of licNum into "?"
			stmt.setString(1, licNum);
			// Execute a query
			conn = new DBConnect();
			System.out.println("Fetching records from the database...");
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				ClientModel appeal = new ClientModel();
				// grab record data by table field name into ClientModel account object
				appeal.setCit(resultSet.getInt("TicketNumber"));
				appeal.setLic(resultSet.getString("License"));
				appeal.setLoc(resultSet.getString("Location"));
				appeal.setTime(resultSet.getString("Time"));
				appeal.setFine(resultSet.getInt("Fine"));
				appeal.setOfficer(resultSet.getString("Officer"));
				appeal.setStatus(resultSet.getString("Status"));
				appeals.add(appeal); // add all ticket data to the arraylist
			}
			conn.getConnection().close();
			System.out.println("Data successfully retrieved.");

		} catch (SQLException e) {
			System.out.println("Error occurred while fetching the data: " + e.getMessage());
		}
		return appeals; // return arraylist
	}

	// display any ticket by searching citation number
	public List<ClientModel> getCitCitations (int citNum) {
		List<ClientModel> appeals = new ArrayList<>();
		// using prepared statements
		String sql = "SELECT * FROM sz_ticket WHERE TicketNumber = ?;";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			// pass the value of citNum into "?"
			stmt.setInt(1, citNum);
			// Execute a query
			conn = new DBConnect();
			System.out.println("Fetching records from the database...");
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				ClientModel appeal = new ClientModel();
				// grab record data by table field name into ClientModel account object
				appeal.setCit(resultSet.getInt("TicketNumber"));
				appeal.setLic(resultSet.getString("License"));
				appeal.setLoc(resultSet.getString("Location"));
				appeal.setTime(resultSet.getString("Time"));
				appeal.setFine(resultSet.getInt("Fine"));
				appeal.setOfficer(resultSet.getString("Officer"));
				appeal.setStatus(resultSet.getString("Status"));
				appeals.add(appeal); // add all ticket data to the arraylist
			}
			conn.getConnection().close();
			System.out.println("Data successfully retrieved.");

		} catch (SQLException e) {
			System.out.println("Error occurred while fetching the data: " + e.getMessage());
		}
		return appeals; // return arraylist
	}

	/* getters & setters */

	public int getCit() {
		return cit;
	}

	public void setCit(int cit) {
		this.cit = cit;
	}

	public String getLic() {
		return lic;
	}

	public void setLic(String lic) {
		this.lic = lic;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getFine() {
		return fine;
	}

	public void setFine(int fine) {
		this.fine = fine;
	}

	public String getOfficer() {
		return officer;
	}

	public void setOfficer(String officer) {
		this.officer = officer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}