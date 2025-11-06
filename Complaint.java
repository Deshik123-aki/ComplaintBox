import javafx.beans.property.*;

public class Complaint {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty email;
    private StringProperty message;
    private StringProperty status;

    public Complaint(int id, String name, String email, String message, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.message = new SimpleStringProperty(message);
        this.status = new SimpleStringProperty(status);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public StringProperty messageProperty() { return message; }
    public StringProperty statusProperty() { return status; }
}
