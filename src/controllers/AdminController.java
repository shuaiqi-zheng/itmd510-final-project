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

public class AdminController extends DBConnect implements Initializable {
	// constructor
	public AdminController() {

		cm = new ClientModel();

	}

	ClientModel cm;

	private int citNum;
	private String curStatus;

	// FX objects
	@FXML
	private Pane pane1;
	@FXML
	private Pane pane2;
	@FXML
	private Pane pane3;
	@FXML
	private Pane pane4;
	@FXML
	private Pane pane5;
	@FXML
	private TextField adminLic;
	@FXML
	private TextField adminLoc;
	@FXML
	private TextField adminFine;
	@FXML
	private TextField adminOfficer;
	@FXML
	private Label lblMsg; // admin Add New Citation
	@FXML
	private Label lblMsg1; // admin View All Citation
	@FXML
	private Label lblMsg2; // admin View All Appeals
	@FXML
	private Label lblT; // admin View All Appeals

	@FXML
	private TableView<ClientModel> tblAll;
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

	// initialization
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

	// submit the new ticket to the DB
	public void adminSubmitNew() throws IOException {
		// simple validations
		lblMsg.setText("");
		String lic = this.adminLic.getText();
		String loc = this.adminLoc.getText();
		String fine = this.adminFine.getText();
		String officer = this.adminOfficer.getText();

		// error messages
		if (lic == null || lic.trim().equals("") || loc == null || loc.trim().equals("") || fine == null
				|| fine.trim().equals("") || officer == null || officer.trim().equals("")) {
			lblMsg.setText("There are some empty fields! Please try again.");
			return;
		}

		// INSERT INTO sz_ticket table
		try {
			conn = new DBConnect();
			// Execute a query
			System.out.println("Inserting records into the table...");
			stmt = conn.getConnection().createStatement();
			// Insert data to the database table
			String sql = "insert into sz_ticket(License, Location, Fine, Officer) values " + "('" + adminLic.getText()
					+ "','" + adminLoc.getText() + "','" + adminFine.getText() + "','" + adminOfficer.getText() + "')";
			stmt.executeUpdate(sql);

			// success messages
			conn.getConnection().close();
			System.out.println("New citation successfully created for " + adminLic.getText());
			lblMsg.setText("New citation successfully created for " + adminLic.getText());

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	// view all citations in the DB
	public void viewCitations() throws IOException {
		adminViewAll(); // show the required pane
		tblAll.getItems().setAll(cm.getCitations()); // load table data from ClientModel List

		// success messages
		System.out.println("Data successfully retrieved.");
		lblMsg1.setText("Data successfully retrieved. You have the following citations:");
		tblAll.setVisible(true); // set table view to be visible if not

	}

	public void viewAppeals() throws IOException {
		adminViewAppeal(); // show the required pane
		tblAll.getItems().setAll(cm.getAppeals()); // load table data from ClientModel List

		// success messages

		lblMsg1.setText("Data successfully retrieved. You have the following appeals:");
		tblAll.setVisible(true); // set table view to be visible if not

	}

	// get citNum, the PK in sz_ticket table, on mouse clicking
	public void getSelected(MouseEvent event) throws IOException {
		if (tblAll.getSelectionModel().getSelectedItem() != null) {
		ClientModel selected = tblAll.getSelectionModel().getSelectedItem();
		citNum = selected.getCit();
		curStatus = selected.getStatus();
	}
	}


	// update the selected ticket (from Unpaid to Paid or Paid to Unpaid)
	public void updateThis() throws IOException {
		// SQL query to update selected ticket
		String sql = null;
		if (curStatus.equals("Paid")) {
			sql = "Update sz_ticket SET Status = 'Not Paid' WHERE TicketNumber = '" + citNum + "';";
		} else if (curStatus.equals("Not Paid")) {
			sql = "Update sz_ticket SET Status = 'Paid' WHERE TicketNumber = '" + citNum + "';";
		}
		System.out.print(sql);
		try {
			conn = new DBConnect();
			// Execute a query
			System.out.println("Updating ticket status...");
			stmt = conn.getConnection().createStatement();
			stmt.executeUpdate(sql);
			// System.out.println(sql);
			// success messages
			conn.getConnection().close();
			System.out.println("Status updated for citation #" + citNum);
			// success notification to user
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Update Confirmation");
			alert.setHeaderText(null);
			alert.setContentText("Status updated for citation #" + citNum + ".");
			alert.showAndWait();
		} catch (SQLException e) {
			System.out.println("Error occurred while fetching the data: " + e.getMessage());
		}
	}



	// delete the selected ticket (
	public void deleteThis() throws IOException {
		// SQL query to update selected ticket
		String sql = "DELETE FROM sz_ticket WHERE TicketNumber = '" + citNum + "';";
		// initialize confirmation dialog
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete citation #" + citNum + "?");
		Optional<ButtonType> action = alert.showAndWait();
		// if the user clicks OK, proceed to delete the record from the DB
		if (action.get() == ButtonType.OK) {
			try {
				conn = new DBConnect();
				// Execute a query
				System.out.println("Deleting records into the table...");
				stmt = conn.getConnection().createStatement();
				stmt.executeUpdate(sql);
				System.out.println(sql);
				// success messages
				conn.getConnection().close();
				System.out.println("Record deleted for citation #" + citNum);
				// also instantly removes this entry from the current table view
				tblAll.getItems().removeAll(tblAll.getSelectionModel().getSelectedItem());
				// success notification to user
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Delete Confirmation");
				alert2.setHeaderText(null);
				alert2.setContentText("Record deleted for citation #" + citNum + ".");
				alert2.showAndWait();
			} catch (SQLException e) {
				System.out.println("Error occurred while fetching the data: " + e.getMessage());
			}
		}

	}
	// accept the appeal (update status from appealed to paid)
	public void acceptApp() throws IOException {

			// SQL query to update selected ticket
			String sql = "Update sz_ticket SET Status = 'Paid' WHERE TicketNumber = '" + citNum + "';";
			System.out.print(sql);
			try {
				conn = new DBConnect();
				// Execute a query
				System.out.println("Updating ticket status...");
				stmt = conn.getConnection().createStatement();
				stmt.executeUpdate(sql);
				System.out.println(sql);
				// success messages
				conn.getConnection().close();
				System.out.println("Appeal accepted for citation #" + citNum);
				// success notification to user
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Update Confirmation");
				alert.setHeaderText(null);
				alert.setContentText("Appeal accepted for citation #" + citNum + ".");
				alert.showAndWait();
			} catch (SQLException e) {
				System.out.println("Error occurred while fetching the data: " + e.getMessage());
			}
		}


	// decline the appeal (update status from appealed to unpaid)
	public void declineApp() throws IOException {

			// SQL query to update selected ticket
			String sql = "Update sz_ticket SET Status = 'Not Paid' WHERE TicketNumber = '" + citNum + "';";
			System.out.print(sql);
			try {
				conn = new DBConnect();
				// Execute a query
				System.out.println("Updating ticket status...");
				stmt = conn.getConnection().createStatement();
				stmt.executeUpdate(sql);
				System.out.println(sql);
				// success messages
				conn.getConnection().close();
				System.out.println("Appeal declined for citation #" + citNum);
				// success notification to user
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Update Confirmation");
				alert.setHeaderText(null);
				alert.setContentText("Appeal declined for citation #" + citNum + ".");
				alert.showAndWait();
			} catch (SQLException e) {
				System.out.println("Error occurred while fetching the data: " + e.getMessage());
			}
		}

	// set visibilities for different panes
	public void adminAddNew() {
		// for button "Add New Citations"
		pane1.setVisible(false);
		pane2.setVisible(true);
		pane3.setVisible(false);
		pane4.setVisible(false);
		pane5.setVisible(false);
	}

	public void adminViewAll() {
		// for button "View All Citations"
		pane1.setVisible(false);
		pane2.setVisible(false);
		pane3.setVisible(true);
		pane4.setVisible(true);
		pane5.setVisible(false);
	}

	public void adminViewAppeal() {
		// for button "View All Appeals"
		pane1.setVisible(false);
		pane2.setVisible(false);
		pane3.setVisible(true);
		pane4.setVisible(false);
		pane5.setVisible(true);
	}

	public void adminGoBack() {
		// for button "Go Back"
		pane1.setVisible(true);
		pane2.setVisible(false);
		pane3.setVisible(false);
		pane4.setVisible(false);
		pane5.setVisible(false);
	}

	public void logout() {
		// for logout button, which redirects to the program home view
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
