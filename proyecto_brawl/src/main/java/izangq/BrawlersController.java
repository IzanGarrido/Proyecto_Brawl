package izangq;

import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BrawlersController {

    @FXML
    private MenuItem cerrarSesionButton;

    @FXML
    private ComboBox<String> anadirbrawlers;

    @FXML
    private SplitMenuButton sesionActual;

    @FXML
    private Label Nombre;

    @FXML
    private Label Calidad;

    @FXML
    private Label Mapa;

    @FXML
    private Label Aspectos;

    @FXML
    private Label Origen;

    @FXML
    private ImageView brawlerFoto;

    @FXML
    public void initialize() {
        cargarNombresDesdeBaseDeDatos();
        sesionActual();
        seleccionarbrawler();
    }

    @FXML
    public void sesionActual() {
        sesionActual.setText(App.usuario.getText());
    }

    public void volverAPrincipal() throws IOException {
        App.brawlerActual = null;
        App.iniciaprincipal();
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

    public void seleccionarbrawler() {
        try {
            Connection con = App.conectate();
            String query = "SELECT * FROM Brawlers WHERE Brawler LIKE '" + App.brawlerActual + "'";

            Statement st = con.createStatement();
            Statement st2 = con.createStatement();

            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                Nombre.setText(rs.getString("Brawler"));

                ResultSet rs2 = st2
                        .executeQuery("SELECT Calidad FROM Calidades WHERE IdCalidad =" + rs.getInt("IdCalidad"));
                if (rs2.next()) {
                    Calidad.setText(rs2.getString("Calidad"));
                }
                rs2.close();

                ResultSet rs3 = st2.executeQuery(
                        "SELECT Modo, Mapa FROM Mapas AS ma JOIN Modos AS mo ON ma.IdModo = mo.IdModo WHERE IdMapa = "
                                + rs.getString("IdMapa"));
                if (rs3.next()) {
                    Mapa.setText(rs3.getString("Modo") + ":  " + rs3.getString("Mapa"));
                }
                rs3.close();

                Aspectos.setText(rs.getString("Aspectos"));
                Origen.setText(rs.getString("Origen"));
                Image Imagen = new Image("file:./src/main/resources/images/" + rs.getString("Brawler") + ".png");
                brawlerFoto.setImage(Imagen);

                rs.close();
                st.close();
                st2.close();

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void cerrarSesion() {
        try {
            App.iniciarSesion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
