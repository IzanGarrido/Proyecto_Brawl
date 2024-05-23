package izangq;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class RegistrarseController {

    @FXML
    private TextField Nombre;

    @FXML
    private TextField Apellidos;

    @FXML
    private TextField Telefono;

    @FXML
    private TextField Email;

    @FXML
    private TextField Usuario;

    @FXML
    private PasswordField Contrasenya;

    @FXML
    private Button CrearUsuario;

    @FXML
    public void initialize() {
        limiteTexto(Usuario, 15);
        limiteTexto(Contrasenya, 20);
        limiteTexto(Nombre, 15);
        limiteTexto(Apellidos, 40);
        limiteTexto(Email, 40);
        limiteTexto(Telefono, 9);

        soloNumeros(Telefono);

    }

    private void limiteTexto(TextField textField, int maxLength) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (textField.getText().length() >= maxLength) {
                event.consume();
            }
        });
    }

    private void soloNumeros(TextField textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("\\d")) {
                event.consume();
            }
        });
    }

    private boolean validarEmail(String email) {
        // Sencilla validación de email usando expresión regular
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");
    }

    @FXML
    public void CrearUsuario() {

        if (Usuario.getText().isEmpty() || Contrasenya.getText().isEmpty() ||
                Nombre.getText().isEmpty() || Apellidos.getText().isEmpty() ||
                Email.getText().isEmpty() || Telefono.getText().isEmpty()) {

            showAlert("Error", "Todos los campos son obligatorios.");
            return;
        }

        // Validar formato de email
        if (!validarEmail(Email.getText())) {
            showAlert("Error", "El formato del email es incorrecto.");
            return;
        }

        try {
            Connection con = App.conectate();
            String queryInsert = "INSERT INTO Usuarios (Usuario, Contrasena, Nombre, Apellidos, Email, Telefono) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statementInsert = con.prepareStatement(queryInsert);
            statementInsert.setString(1, Usuario.getText());
            statementInsert.setString(2, Contrasenya.getText());
            statementInsert.setString(3, Nombre.getText());
            statementInsert.setString(4, Apellidos.getText());
            statementInsert.setString(5, Email.getText());
            statementInsert.setString(6, Telefono.getText());

            statementInsert.executeUpdate();

            GoToIniciarSesion();

            statementInsert.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void GoToIniciarSesion() throws IOException {
        App.iniciarSesion();
    }


}