package izangq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App extends Application {

    private static Stage stage;
    private static Scene scene;

    public static TextField usuario;

    public static String brawlerActual;
    public static int idBrawlerActual;

    public static Connection conectate() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:33006/Brawl_Stars", "root",
                    "dbrootpass");
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return con;

    }

    public void start(Stage stage) throws IOException {
        App.stage = stage;
        App.stage.getIcons().add(new Image("file:./src/main/resources/images/Logo.jpg"));
        iniciarSesion();
        stage.setResizable(false);
        stage.show();

    }

    public static void iniciarSesion() throws IOException {
        stage.setTitle("Iniciar Sesión");
        Parent root = loadFXML("iniciaSesion");
        Scene scene = new Scene(root, 350, 270);
        stage.setScene(scene);
    }

    public static void IniciaRegistrarse() throws IOException {
        stage.setTitle("Registrarse");
        Parent root = loadFXML("Registrarse");
        Scene scene = new Scene(root, 350, 480);
        stage.setScene(scene);
    }

    public static void iniciaprincipal() throws IOException {
        stage.setTitle("Principal");
        Parent root = loadFXML("principal");
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);

    }

    public static void iniciabrawlers() throws IOException {
        stage.setTitle("Brawlers");
        Parent root = loadFXML("brawlers");
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
    }

    public static void iniciaCreaBrawlers() throws IOException {
        stage.setTitle("Creacion de brawlers");
        Parent root = loadFXML("CreaBrawler");
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
    }

    public static void iniciaGestionBrawlers() throws IOException {
        stage.setTitle("Gestion de brawlers");
        Parent root = loadFXML("Gestion");
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
    }

    public static void iniciaModificarBrawlers() throws IOException {
        stage.setTitle("Modificación de brawlers");
        Parent root = loadFXML("ModificaBrawler");
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }

}