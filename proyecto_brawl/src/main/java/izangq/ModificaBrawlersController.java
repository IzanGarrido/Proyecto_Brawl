package izangq;

import java.sql.Statement;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

public class ModificaBrawlersController {

    @FXML
    private Label BrawlerNombre;

    @FXML
    private ComboBox<String> Calidad;

    @FXML
    private ComboBox<String> MejorMapa;

    @FXML
    private TextField nAspectos;

    @FXML
    private TextArea Origen;

    @FXML
    private Button Boton;

    @FXML
    private Button btnSeleccionarImagen;

    @FXML
    private ImageView imagenSeleccionada;


    private File imagenFile;

    private int brawlerId;

    public void initialize() {
        insertaCalidades();
        insertaMapas();
        cargarBrawler();
        soloNumeros(nAspectos);
        limiteTexto(nAspectos, 2);
    }

    private void limiteTexto(TextField textField, int maxLength) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (textField.getText().length() >= maxLength) {
                event.consume();
            }
        });
    }

    public void volverAPrincipal() throws IOException {
        App.iniciaprincipal();
    }

    public void insertaCalidades() {
        try {
            Connection con = App.conectate();
            String query = "SELECT Calidad FROM Calidades WHERE CALIDAD != 'Brawler_inicial';";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Calidad.getItems().add(resultSet.getString("Calidad"));
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertaMapas() {
        try {
            Connection con = App.conectate();
            String query = "SELECT Modo, Mapa FROM Mapas JOIN Modos ON Mapas.IdModo = Modos.IdModo;";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                MejorMapa.getItems().add(resultSet.getString("Mapa"));
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cargarBrawler() {
        this.brawlerId = App.idBrawlerActual;
        try {
            Connection con = App.conectate();
            String query = "SELECT Brawler FROM Brawlers WHERE IdBrawler = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, brawlerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BrawlerNombre.setText(resultSet.getString("Brawler"));
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imagenFile = selectedFile;
            Image image = new Image(selectedFile.toURI().toString());
            imagenSeleccionada.setImage(image);
        }
    }

    public void modificarBrawler() {
        int IdCalidad = 0;
        int IdMapa = 0;

        if (Calidad.getValue().isEmpty() || MejorMapa.getValue().isEmpty() ||
                nAspectos.getText().isEmpty() || Origen.getText().isEmpty()) {

            muestraAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        try {
            Connection con = App.conectate();
            String queryUpdate = "UPDATE Brawlers SET IdCalidad = ?, IdMapa = ?, Aspectos = ?, Origen = ? WHERE IdBrawler = ?";

            String query = "SELECT IdCalidad FROM Calidades WHERE CALIDAD = '" + Calidad.getValue() + "';";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                IdCalidad = rs.getInt("IdCalidad");
            }

            query = "SELECT IdMapa FROM Mapas WHERE MAPA = '" + MejorMapa.getValue() + "'; ";
            rs = st.executeQuery(query);

            while (rs.next()) {
                IdMapa = rs.getInt("IdMapa");
            }

            String brawlerNombre = BrawlerNombre.getText().trim();
            String nuevoNombreImagen = brawlerNombre + ".png";
            String imagePath = "./src/main/resources/images/" + nuevoNombreImagen;

            if (imagenFile != null) {
                Files.copy(imagenFile.toPath(), new File(imagePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            PreparedStatement statementUpdate = con.prepareStatement(queryUpdate);
            statementUpdate.setInt(1, IdCalidad);
            statementUpdate.setInt(2, IdMapa);
            statementUpdate.setInt(3, Integer.parseInt(nAspectos.getText()));
            statementUpdate.setString(4, Origen.getText());
            statementUpdate.setInt(5, brawlerId);

            statementUpdate.executeUpdate();

            volverAPrincipal();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void muestraAlerta(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void soloNumeros(TextField textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("\\d")) {
                event.consume();
            }
        });
    }
}
