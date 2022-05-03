// ITMD 510 Final Project by Shuaiqi Zheng

package controllers;

import java.sql.Statement;

import Dao.DBConnect;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.LoginModel;

public class LoginController {

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private Label lblError;

	private LoginModel model;

	public LoginController() {
		model = new LoginModel();
	}

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	public void login() {

		lblError.setText("");
		String username = this.txtUsername.getText();
		String password = this.txtPassword.getText();

		// Validations
		if (username == null || username.trim().equals("")) {
			lblError.setText("Username cannot be empty or contain spaces!");
			return;
		}
		if (password == null || password.trim().equals("")) {
			lblError.setText("Password cannot be empty or contain spaces!");
			return;
		}
		if (username == null || username.trim().equals("") && (password == null || password.trim().equals(""))) {
			lblError.setText("Username / password cannot be empty or contain spaces!");
			return;
		}

		// Authentication check
		checkCredentials(username, password);

	}

	public void checkCredentials(String username, String password) {
		Boolean isValid = model.getCredentials(username, password);
		if (!isValid) {
			lblError.setText("User does not exist! Please try again.");
			return;
		}
		try {
			AnchorPane root;
			if (model.isAdmin() && isValid) {
				// If the user is admin, inflate the admin view
				root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/AdminView.fxml"));
				Main.stage.setTitle("MyIIT Parking Portal - Admin Control Panel");

			} else {
				// If the user is customer, inflate the customer view
				root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/ClientView.fxml"));
				// Set user ID, username, name, and license plate number as acquired from DB
				int userId = model.getId();
				String uName = model.getUname();
				String name = model.getName();
				String lic = model.getLic();
				ClientController.setUserInfo(userId, uName, name, lic);
				Main.stage.setTitle("MyIIT Parking Portal - Citations");
			}
			Scene scene = new Scene(root);
			Main.stage.setScene(scene);

		} catch (Exception e) {
			System.out.println("Error occurred while inflating the view: " + e);
		}

	}

	public void register() {

		// If the user wants to create a new account, inflate register view
		try {
			AnchorPane root;
			root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/RegisterView.fxml"));
			Main.stage.setTitle("MyIIT Parking Portal - Creating an Account");
			Scene scene = new Scene(root);
			Main.stage.setScene(scene);

		} catch (Exception e) {
			System.out.println("Error occurred while inflating the view: " + e);
		}

	}

	public void exit() {
		// exit the program
		System.exit(0);
	}
}