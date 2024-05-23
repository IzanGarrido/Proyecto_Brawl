package izangq;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GestionController {

    @FXML
    private TableView<Brawler> tablaBrawlers;

    @FXML
    private TableColumn<Brawler, Integer> id;

    @FXML
    private TableColumn<Brawler, String> brawler;

    @FXML
    private TableColumn<Brawler, Integer> idMapa;

    @FXML
    private TableColumn<Brawler, Integer> idCalidad;

    @FXML
    private TableColumn<Brawler, Integer> Aspectos;

    @FXML
    private TableColumn<Brawler, String> Origen;

    private ObservableList<Brawler> brawlerList = FXCollections.observableArrayList();

    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("idBrawler"));
        brawler.setCellValueFactory(new PropertyValueFactory<>("brawler"));
        idMapa.setCellValueFactory(new PropertyValueFactory<>("idMapa"));
        idCalidad.setCellValueFactory(new PropertyValueFactory<>("idCalidad"));
        Aspectos.setCellValueFactory(new PropertyValueFactory<>("aspectos"));
        Origen.setCellValueFactory(new PropertyValueFactory<>("origen"));

        cargarNombresDesdeBaseDeDatos();
    }

    private void cargarNombresDesdeBaseDeDatos() {
        try {
            Connection con = App.conectate();
            String query = "SELECT * FROM Brawlers";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int idBrawler = rs.getInt("IdBrawler");
                String brawlerName = rs.getString("Brawler");
                int idMapa = rs.getInt("IdMapa");
                int idCalidad = rs.getInt("IdCalidad");
                int aspectos = rs.getInt("Aspectos");
                String origen = rs.getString("Origen");

                Brawler brawler = new Brawler(idBrawler, brawlerName, idMapa, idCalidad, aspectos, origen);
                brawlerList.add(brawler);
            }

            tablaBrawlers.setItems(brawlerList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarBrawler() {
        Brawler brawlerSeleccionado = tablaBrawlers.getSelectionModel().getSelectedItem();

        if (brawlerSeleccionado != null) {
            try {
                Connection con = App.conectate();
                String query = "DELETE FROM Brawlers WHERE IdBrawler = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setInt(1, brawlerSeleccionado.getIdBrawler());
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    brawlerList.remove(brawlerSeleccionado);
                } else {
                    System.out.println("No se encontró el brawler en la base de datos.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            muestraAlerta("Brawler no seleccionado", "Por favor, selecciona un brawler para eliminar.");
        }
    }

    @FXML
    private void modificarBrawler() {
        Brawler brawlerSeleccionado = tablaBrawlers.getSelectionModel().getSelectedItem();

        if (brawlerSeleccionado != null) {
            App.idBrawlerActual = brawlerSeleccionado.getIdBrawler();
            try {
                App.iniciaModificarBrawlers();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            muestraAlerta("Brawler no seleccionado", "Por favor, selecciona un brawler para Modificar.");
        }

    }

    private void muestraAlerta(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void volverAPrincipal() throws IOException {
        App.iniciaprincipal();
    }

}