import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class OnlineComplaintBox extends Application {

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        connect();

        // UI Elements
        Label title = new Label("Online Complaint Box for Students");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label msgLabel = new Label("Complaint:");
        TextArea msgArea = new TextArea();

        Button submitBtn = new Button("Submit Complaint");
        Button viewBtn = new Button("View Complaints");

        Label statusLabel = new Label();

        // Submit Button Action
        submitBtn.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String msg = msgArea.getText();

            if (name.isEmpty() || email.isEmpty() || msg.isEmpty()) {
                statusLabel.setText("Please fill all fields!");
                return;
            }

            try {
                pst = conn.prepareStatement("INSERT INTO complaints(name,email,message) VALUES ()");
                pst.setString(1, name);
                pst.setString(2, email);
                pst.setString(3, msg);
                pst.executeUpdate();
                statusLabel.setText(" Complaint Submitted Successfully!");
                nameField.clear();
                emailField.clear();
                msgArea.clear();
            } catch (Exception ex) {
                statusLabel.setText(" Error: " + ex.getMessage());
            }
        });

        // View Button Action
        viewBtn.setOnAction(e -> viewComplaints());

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(title, 0, 0, 2, 1);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(msgLabel, 0, 3);
        grid.add(msgArea, 1, 3);
        grid.add(submitBtn, 0, 4);
        grid.add(viewBtn, 1, 4);
        grid.add(statusLabel, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Online Complaint Box");
        primaryStage.show();
    }

    // JDBC Connection
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/complaintdb", "root", "YOUR_PASSWORD");
            System.out.println(" Database Connected Successfully!");
        } catch (Exception e) {
            System.out.println(" Database Connection Failed: " + e.getMessage());
        }
    }

    // View All Complaints in a New Window
    public void viewComplaints() {
        Stage viewStage = new Stage();
        TableView<Complaint> table = new TableView<>();

        TableColumn<Complaint, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> c.getValue().idProperty().asObject());

        TableColumn<Complaint, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());

        TableColumn<Complaint, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(c -> c.getValue().emailProperty());

        TableColumn<Complaint, String> msgCol = new TableColumn<>("Message");
        msgCol.setCellValueFactory(c -> c.getValue().messageProperty());

        TableColumn<Complaint, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c -> c.getValue().statusProperty());

        table.getColumns().addAll(idCol, nameCol, emailCol, msgCol, statusCol);

        try {
            pst = conn.prepareStatement("SELECT * FROM complaints");
            rs = pst.executeQuery();
            while (rs.next()) {
                table.getItems().add(new Complaint(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("message"),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox vbox = new VBox(table);
        Scene scene = new Scene(vbox, 600, 400);
        viewStage.setTitle("All Complaints");
        viewStage.setScene(scene);
        viewStage.show();
    }
}
