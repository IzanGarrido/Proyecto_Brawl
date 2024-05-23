package izangq;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrincipalController {

    @FXML
    private MenuItem cerrarSesionButton;

    @FXML
    private ComboBox<String> anadirbrawlers;

    @FXML
    private SplitMenuButton sesionActual;

    @FXML
    public void initialize() {
        cargarNombresDesdeBaseDeDatos();
        sesionActual();

    }

    private void cargarNombresDesdeBaseDeDatos() {
        try {
            Connection con = App.conectate();
            String query = "SELECT Brawler FROM Brawlers";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                String nombre = resultSet.getString("Brawler");
                anadirbrawlers.getItems().add(nombre);
                anadirbrawlers.setValue(App.brawlerActual);
            }
    
            statement.close();
            resultSet.close();
        } catch (SQLException e) {  
            e.printStackTrace();
        }
    
        anadirbrawlers.setOnAction(event -> {
            String nombreActual = anadirbrawlers.getValue();
            if (nombreActual != null && !anadirbrawlers.getItems().contains(nombreActual)) {
                muestraAlerta("Error", "El nombre ingresado no está en la lista."); 
                
            } else {
                App.brawlerActual = nombreActual;
                try {
                    App.iniciabrawlers();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
    }   

    private void muestraAlerta(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void sesionActual() {
        sesionActual.setText(App.usuario.getText());

    }

    @FXML
    private void cerrarSesion() {

        try {

            App.iniciarSesion();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void GoToCreacionBrawler() {
        try {
            App.iniciaCreaBrawlers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void GoToGestionBrawler() {
        try {
            App.iniciaGestionBrawlers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}