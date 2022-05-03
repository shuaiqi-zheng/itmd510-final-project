// ITMD 510 Final Project by Shuaiqi Zheng

package controllers;

import java.sql.SQLException;
import java.sql.Statement;

import Dao.DBConnect;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class RegisterController {

	// FX objects
	@FXML
	private TextField txtUname;
	@FXML
	private TextField txtPwd;
	@FXML
	private TextField txtPwd2;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtAddress;
	@FXML
	private TextField txtLic;
	@FXML
	private Label lblMsg;

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	public RegisterController() {
		conn = new DBConnect();
	}

	public void createAcc() {

		// Simple validations
		lblMsg.setText("");
		String username = this.txtUname.getText();
		String pwd = this.txtPwd.getText();
		String pwd2 = this.txtPwd2.getText();
		String name = this.txtName.getText();
		String address = this.txtAddress.getText();
		String lic = this.txtLic.getText();

		// error messages
		if (username == null || username.trim().equals("") || pwd == null || pwd.trim().equals("") || pwd2 == null
				|| pwd2.trim().equals("") || name == null || name.trim().equals("") || address == null
				|| address.trim().equals("") || lic == null || lic.trim().equals("")) {
			lblMsg.setText("There are some empty fields! Please try again.");
			return;
		}

		// error messages
		if (!pwd.equals(pwd2)) {
			lblMsg.setText("The two passwords you entered do not match! Please try again.");
			return;
		}

		System.out.println("Creating new account...");

		// INSERT INTO user table
		try {
			stmt = conn.getConnection().createStatement();
			String sql = null;
			// SQL query to insert data to the database table
			sql = "INSERT INTO sz_user (username, password, name, address, license) values ('" + txtUname.getText()
					+ "','" + txtPwd.getText() + "','" + txtName.getText() + "','" + txtPwd.getText() + "','"
					+ txtLic.getText() + "')";
			stmt.executeUpdate(sql);

			// success message
			System.out.println("Accound created. Please return to the home page for login.");
			lblMsg.setText("Accound created. Please return to the home page for login.");

			conn.getConnection().close();

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public void logout() {
		// redirects to the program home view
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/styles.css").toExternalForm());
			Main.stage.setScene(scene);
			Main.stage.setTitle("MyIIT Parking Portal - Please Log in");
			Main.stage.show();
		} catch (Exception e) {
			System.out.println("Error occurred while inflating the view: " + e.getMessage());
		}
	}
}
