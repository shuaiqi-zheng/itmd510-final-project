// ITMD 510 Final Project by Shuaiqi Zheng

package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import Dao.DBConnect;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.ClientModel;

public class ClientController extends DBConnect implements Initializable {
	// constructor
	public ClientController() {

		cm = new ClientModel();

	}

	private int citNum;
	private String curStatus;
	static int userid;
	static String userName;
	static String custName;
	static String licNum;
	ClientModel cm;

	@FXML
	private Pane pane1; // client main panel
	@FXML
	private Pane pane2; // view all citation panel
	@FXML
	private Pane pane3;
	@FXML
	private Pane pane4;
	@FXML
	private TextField txtCit;
	@FXML
	private Button but; // display info button
	@FXML
	private Button butPay; // pay ticket button
	@FXML
	private Button butApp; // appeal ticket info button
	@FXML
	private Label lblMsgWel; // welcome messages
	@FXML
	private Label lblMsg1; // message box for view all citations
	@FXML
	private Label lblMsg2; // message box for search citations
	@FXML
	private TableView<ClientModel> tblAll; // table view for holding ticket records from DB
	@FXML
	private TableColumn<ClientModel, String> cit;
	@FXML
	private TableColumn<ClientModel, String> lic;
	@FXML
	private TableColumn<ClientModel, String> loc;
	@FXML
	private TableColumn<ClientModel, String> time;
	@FXML
	private TableColumn<ClientModel, String> fine;
	@FXML
	private TableColumn<ClientModel, String> officer;
	@FXML
	private TableColumn<ClientModel, String> status;

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cit.setCellValueFactory(new PropertyValueFactory<ClientModel, String>("cit"));
		lic.setCellValueFactory(new PropertyValueFactory<ClientModel, String>("lic"));
		loc.setCellValueFactory(new PropertyValueFactory<ClientModel, String>("loc"));
		time.setCellValueFactory(new PropertyValueFactory<ClientModel, String>("time"));
		fine.setCellValueFactory(new PropertyValueFactory<ClientModel, String>("fine"));
		officer.setCellValueFactory(new PropertyValueFactory<ClientModel, String>("officer"));
		status.setCellValueFactory(new PropertyValueFactory<ClientModel, String>("status"));
	}

	public void viewCitations() throws IOException {
		clientViewAll(); // make pane2 and pane3 visible
		tblAll.getItems().setAll(cm.getLicCitations(licNum)); // load table data from ClientModel List
		// success messages
		lblMsg1.setText("Data successfully retrieved. You have the following citations: ");
		tblAll.setVisible(true); // set table view to be visible if not
	}

	public void viewAppeals() throws IOException {
		clientViewApp(); // make pane2 and pane 4 visible
		tblAll.getItems().setAll(cm.getLicAppeals(licNum)); // load table data from ClientModel List
		// success messages
		lblMsg1.setText("Data successfully retrieved. You have the following appeals: ");
		tblAll.setVisible(true); // set table view to be visible if not
	}

	public void clientViewAll() {

		pane1.setVisible(false);
		pane2.setVisible(true);
		pane3.setVisible(true);
		pane4.setVisible(false);

	}

	public void clientViewApp() {

		pane1.setVisible(false);
		pane2.setVisible(true);
		pane3.setVisible(false);
		pane4.setVisible(true);

	}

	public void clientSearch() {

		pane1.setVisible(false);
		pane2.setVisible(true);
		pane3.setVisible(true);
		pane4.setVisible(false);

	}

	public void clientGoBack() {

		pane1.setVisible(true);
		pane2.setVisible(false);
		pane3.setVisible(false);
		pane4.setVisible(false);

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

	public static void setUserInfo(int userId, String uName, String name, String lic) throws IOException {
		userid = userId;
		userName = uName;
		custName = name;
		licNum = lic;
		System.out.println("Login Successful.");
		System.out.println("Welcome! Please check your account infomation: ");
		System.out.println("UserID: " + userid + ", " + "Username: " + userName + ", " + "Name: " + custName + ", "
				+ "License Plate Number: " + licNum);
	}

	public void displayUserInfo() {
		lblMsgWel.setText(
				"Username: " + userName + " | " + "Name: " + custName + " | " + "License Plate Number: " + licNum);
		but.setVisible(false);
		but.setDisable(true);
	}

	public void getSelected(MouseEvent event) throws IOException {
		if (tblAll.getSelectionModel().getSelectedItem() != null) {
			ClientModel selected = tblAll.getSelectionModel().getSelectedItem();
			System.out.println(selected.getCit());
			System.out.println(selected.getStatus());
			citNum = selected.getCit();
			curStatus = selected.getStatus();
			if (curStatus.equals("Not Paid")) {
				butPay.setDisable(false);
				butApp.setDisable(false);
			} else {
				butPay.setDisable(true);
				butApp.setDisable(true);
			}
		}

	}

	// "pay" the ticket
	public void payCitation() {
		// SQL to update the status from Not Paid to Paid
		String sql = "Update sz_ticket SET Status = 'Paid' WHERE TicketNumber  = '" + citNum + "';";
		try {
			conn = new DBConnect();
			// Execute a query
			System.out.println("Processing payment...");
			stmt = conn.getConnection().createStatement();
			stmt.executeUpdate(sql);
			conn.getConnection().close();
			System.out.println("Payment successful for citation #" + citNum);
			// success notification to user
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Payment Successful");
			alert.setHeaderText(null);
			alert.setContentText("We have received your payments on citation #" + citNum + ". Thank you!");
			alert.showAndWait();

		} catch (SQLException e) {
			System.out.println("Error occurred while paying the ticket: " + e.getMessage());
		}

	}

// appeal the ticket
	public void appCitation() {
		// SQL to update the status from Not Paid to Appealed
		String sql = "Update sz_ticket SET Status = 'Appealed' WHERE TicketNumber  = '" + citNum + "';";
		try {
			conn = new DBConnect();
			// Execute a query
			System.out.println("Processing appeal...");
			stmt = conn.getConnection().createStatement();
			stmt.executeUpdate(sql);
			conn.getConnection().close();
			System.out.println("Appeal sent for citation #" + citNum);
			// success notification to user
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Appeal Received");
			alert.setHeaderText(null);
			alert.setContentText("We have received your appeal on citation #" + citNum + ". Thank you!");
			alert.showAndWait();

			// also instantly removes this entry from the current ticket view
			tblAll.getItems().removeAll(tblAll.getSelectionModel().getSelectedItem());

		} catch (SQLException e) {
			System.out.println("Error occurred while appealing the ticket: " + e.getMessage());
		}

	}

// cancel appeal on one ticket
	public void cancelApp() {
		// SQL to update the status from Appealed to Not Paid
		String sql = "Update sz_ticket SET Status = 'Not Paid' WHERE TicketNumber  = '" + citNum + "';";
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Appeal Cancellation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to cancel the appeal on citation #" + citNum + "?");
		Optional<ButtonType> action = alert.showAndWait();
		// if the user clicks OK, proceed to cancel it
		if (action.get() == ButtonType.OK) {
			try {
				conn = new DBConnect();
				// Execute a query
				System.out.println("Processing...");
				stmt = conn.getConnection().createStatement();
				stmt.executeUpdate(sql);
				conn.getConnection().close();
				System.out.println("Appeal cancelled for citation #" + citNum);
				// success notification to user
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Appeal Cancelled");
				alert2.setHeaderText(null);
				alert2.setContentText("Appeal on citation #" + citNum + " is cancelled. Thank you!");
				alert2.showAndWait();

				// also instantly removes this entry from the current ticket view
				tblAll.getItems().removeAll(tblAll.getSelectionModel().getSelectedItem());

			} catch (SQLException e) {
				System.out.println("Error occurred while appealing the ticket: " + e.getMessage());
			}

		}

	}

// search tickets by known citation number
	public void showSearchRes() throws IOException {
		clientSearch();
		tblAll.getItems().setAll(cm.getCitCitations(citNum)); // load table data from ClientModel List
		// success messages
		System.out.println("Data successfully retrieved.");
		lblMsg1.setText("Data successfully retrieved for citations #" + citNum + ".");
		tblAll.setVisible(true); // set table view to be visible if not

	}

	public void searchCit() throws IOException {
		// simple validations
		lblMsg2.setText("");
		String txtCit = this.txtCit.getText();
		// error messages
		if (txtCit == null || txtCit.trim().equals("")) {
			lblMsg2.setText("The citation number cannot be empty!");
			return;
		} else if (txtCit.length() != 5) {
			lblMsg2.setText("The citation number must be 5-digit long!");
			return;
		} else {
			try {
				Integer.parseInt(txtCit);
			}

			catch (NumberFormatException e) {
				lblMsg2.setText("The citation number may only contain numbers!");
				return;
			}
			citNum = Integer.parseInt(txtCit);

			showSearchRes();

		}

	}

}
