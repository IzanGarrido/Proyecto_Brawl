package izangq;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class IniciaSesionController {

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField contrasenaField;

    @FXML
    private Button iniciarSesionButton;

    @FXML
    private Label Registrarse;

    @FXML
    private void iniciarSesion() {
        String usuario = usuarioField.getText();
        String contrasena = contrasenaField.getText();
        App.usuario = usuarioField;

        try {
            Connection con = App.conectate();

            String query = "SELECT * FROM Usuarios WHERE Usuario = ? AND Contrasena = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, contrasena);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                App.iniciaprincipal();
            } else {
                alertaError("Error", "Usuario o contraseña incorrectos.");
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void alertaError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void GoToRegistrarse() throws IOException {
        App.IniciaRegistrarse();
    }

    @FXML
    private void confirmacionRestablecer(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de restablecimiento");
        alert.setHeaderText("¿Estás seguro de que deseas restablecer la base de datos?");
        alert.setContentText("Perderás todos los datos añadidos recientemente, incluidos los usuarios.");

        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            restablecerBBDD();
        }
    }

    private void restablecerBBDD() {

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:33006/", "root", "dbrootpass");
            Statement st = con.createStatement();

            String borrarDatabase = "DROP DATABASE IF EXISTS Brawl_Stars";
            st.executeUpdate(borrarDatabase);
            
            String crearDatabase = "CREATE DATABASE IF NOT EXISTS Brawl_Stars";
            st.executeUpdate(crearDatabase);

            String usarBrawl = "USE Brawl_Stars";
            st.executeUpdate(usarBrawl);

            String crearCalidades = "CREATE TABLE IF NOT EXISTS Calidades ("
                    + "IdCalidad INT NOT NULL AUTO_INCREMENT, "
                    + "Calidad VARCHAR(15) NULL, "
                    + "PRIMARY KEY (IdCalidad)"
                    + ")";
            st.executeUpdate(crearCalidades);

            String crearModos = "CREATE TABLE IF NOT EXISTS Modos ("
                    + "IdModo INT NOT NULL AUTO_INCREMENT, "
                    + "Modo VARCHAR(30) NULL, "
                    + "PRIMARY KEY (IdModo)"
                    + ")";
            st.executeUpdate(crearModos);

            String crearMapas = "CREATE TABLE IF NOT EXISTS Mapas ("
                    + "IdMapa INT NOT NULL AUTO_INCREMENT, "
                    + "IdModo INT NULL, "
                    + "Mapa VARCHAR(45) NULL, "
                    + "PRIMARY KEY (IdMapa), "
                    + "FOREIGN KEY (IdModo) REFERENCES Modos(IdModo)"
                    + ")";
            st.executeUpdate(crearMapas);

            String crearBrawlers = "CREATE TABLE IF NOT EXISTS Brawlers ("
                    + "IdBrawler INT NOT NULL AUTO_INCREMENT, "
                    + "Brawler VARCHAR(20) NULL, "
                    + "IdCalidad INT NULL, "
                    + "IdMapa INT NULL, "
                    + "Aspectos INT NULL, "
                    + "Origen VARCHAR(400) NULL, "
                    + "PRIMARY KEY (IdBrawler), "
                    + "FOREIGN KEY (IdMapa) REFERENCES Mapas(IdMapa), "
                    + "FOREIGN KEY (IdCalidad) REFERENCES Calidades(IdCalidad)"
                    + ")";
            st.executeUpdate(crearBrawlers);

            String crearUsuarios = "CREATE TABLE IF NOT EXISTS Usuarios ("
                    + "IdUsuario INT NOT NULL AUTO_INCREMENT, "
                    + "Usuario VARCHAR(40) NULL, "
                    + "Contrasena VARCHAR(40) NULL, "
                    + "Nombre VARCHAR(40) NULL, "
                    + "Apellidos VARCHAR(40) NULL, "
                    + "Email VARCHAR(40) NULL, "
                    + "Telefono VARCHAR(40) NULL, "
                    + "PRIMARY KEY (IdUsuario)"
                    + ")";
            st.executeUpdate(crearUsuarios);
            
            String insertaCalidades = "INSERT INTO Calidades (Calidad) VALUES "
                    + "('Brawler_inicial'), "
                    + "('Especial'), "
                    + "('Superespecial'), "
                    + "('Épico'), "
                    + "('Mítico'), "
                    + "('Legendario')";
            st.executeUpdate(insertaCalidades);
            
            String insertaModos = "INSERT INTO Modos (Modo) VALUES "
                    + "('Atrapagemas'), "
                    + "('Supervivencia Solo'), "
                    + "('Supervivencia Duo'), "
                    + "('Atraco'), "
                    + "('Caza Estelar'), "
                    + "('Balón Brawl'), "
                    + "('Zona Restringida'), "
                    + "('Noqueo'), "
                    + "('Destrucción')";
            st.executeUpdate(insertaModos);
            
            String insertaMapas = "INSERT INTO Mapas (IdModo, Mapa) VALUES " +
                    "(1, 'Mina rocosa'), " +
                    "(1, 'Hidra venenosa'), " +
                    "(1, 'Fuerte de gemas'), " +
                    "(1, 'Cueva subterránea'), " +
                    "(1, 'Ángulo agudo'), " +
                    "(1, 'Salón recreativo rústico'), " +
                    "(1, 'Espacio abierto'), " +
                    "(1, 'Última parada'), " +
                    "(2, 'Arroyo Calavera'), " +
                    "(2, 'Castillos de ladrillo'), " +
                    "(2, 'Todo o nada'), " +
                    "(2, 'Cueva profunda'), " +
                    "(2, 'Doble dilema'), " +
                    "(2, 'Valle tempestuoso'), " +
                    "(2, 'Pasaje oscuro'), " +
                    "(2, 'Paraíso del tiro de élite'), " +
                    "(2, 'Cincuenta y cinco'), " +
                    "(3, 'Arroyo Calavera'), " +
                    "(3, 'Castillos de ladrillo'), " +
                    "(3, 'Todo o nada'), " +
                    "(3, 'Cueva profunda'), " +
                    "(3, 'Doble dilema'), " +
                    "(3, 'Valle tempestuoso'), " +
                    "(3, 'Pasaje oscuro'), " +
                    "(3, 'Paraíso del tiro de élite'), " +
                    "(3, 'Cincuenta y cinco'), " +
                    "(4, 'Cañón explosivo'), " +
                    "(4, 'Refugio'), " +
                    "(4, 'Cementerio G.G.'), " +
                    "(4, 'Patata caliente'), " +
                    "(5, 'Tiroteo estelar'), " +
                    "(5, 'Canal grande'), " +
                    "(5, 'Qué intriga'), " +
                    "(6, 'Estadio brawl'), " +
                    "(6, 'Campos furtivos'), " +
                    "(6, 'Superplaya'), " +
                    "(6, 'Palco central'), " +
                    "(6, 'Pelota de playa'), " +
                    "(6, 'Fútbol soleado'), " +
                    "(6, 'Retina'), " +
                    "(6, 'Estadio estruendoso'), " +
                    "(7, 'Campo abierto'), " +
                    "(7, 'Estrategias paralelas'), " +
                    "(7, 'Pista ardiente'), " +
                    "(7, 'Duelo de escarabajos'), " +
                    "(8, 'Barranco del brazo de oro'), " +
                    "(8, 'Roca de Belle'), " +
                    "(8, 'Hasta el fondo'), " +
                    "(8, 'Fénix en llamas'), " +
                    "(8, 'A la intemperie'), " +
                    "(8, 'Nuevos horizontes'), " +
                    "(8, 'Entre ríos'), " +
                    "(8, 'Cuatro niveles'), " +
                    "(9, 'Destino del infinito'), " +
                    "(9, 'Paraíso destructivo'), " +
                    "(9, 'Daño cuatriplicado'), " +
                    "(9, 'Gran explanada');";
            st.executeUpdate(insertaMapas);

            String insertaBrawlers = "INSERT INTO Brawlers (Brawler, IdCalidad, IdMapa, Aspectos, Origen) " +
            "VALUES " +
                "('Shelly','1','1','16','Shelly es la tiradora perfecta: donde pone el ojo, pone la bala. No entiende por qué Colt es quien siempre se lleva todas las miradas...'), " +
                "('Nita','2','1','12','¡Furiosa como una bestia, Nita nunca se retira de una pelea! El oso de peluche que lleva en la cabeza es un claro aviso para sus rivales: ni se os ocurra tocarlo.'), " +
                "('Colt','2','1','19','¡Colt es la atracción principlal de Starr Park! Su carisma, sus trucos con la pistola y su sonrisa resplandeciente conquistan hasta al pública más exigente. Excepto a Shelly, claro...'), " +
                "('Bull','2','1','11','Bull ya no es el toro salvaje de su juventud, pero lejos de estar oxidado, no dudará en demostrar que todavía es duro como el metal si alguien lo pone a prueba.'), " +
                "('Brock','2','1','20','Brock es un entusiasta de los videojuegos y todo un guerrero del teclado. Aunque no lo parezca, es bastante introvertido, ¡pero hará lo que sea para ganar!'), " +
                "('El_primo','2','1','16','El primo es el centro de todas las miradas ¡y es que es el rey del cuadrilátero! Es experto en meterse al público en el bolsillo, aunque no le resulta tan fácil con sus compañeros...'), " +
                "('Barley','2','1','9','Este barman robótico ha sido diseñado para preparar bebidas y charlas con la clientela. Barley también se dedica a mantener el bar limpio como una patena a toda costa, aunque tenga que patear el trasero de algún que otro cliente patoso.'), " +
                "('Poco','2','1','8','¡Poco cree que la música puede cambiarte la vida! Siente tanta pasión por el tema que nunca deja de tocar... aunque se lo pidan.'), " +
                "('Rosa','2','1','7','Rosa es dedica a la botánica y siente una fuerte conexión con las plantas. También es boxeadora, ¡y no dudará en dar un puñetazo a los especimenes que es pasen de la raya!'), " +
                "('Jessie','3','1','9','Jessie es una niña prodigia capaz de construir armas y gadgets a partir de lo que encuentra en la chatarrería. Quizá algún día su madre, Pam, se dé cuenta de lo que es capaz.'), " +
                "('Dynamike','3','1','15','Dynamike es un viejo minero y explorador que siente auténtica pasión por los explosivos. Nada le pone de mejor humor que oir un buen ¡BUM!'), " +
                "('Tick','3','1','6','Tick sigue a Penny allá a donde va cuál perrito faldero mientras ella planea su próximo gran golpe. Aparte de hacer saltar todo por los aires, no hay muchas cosas que se le den bien, ¡pero con eso basta y sobra!'), " +
                "('8-Bit','3','1','9','8-BIT tiene la reputación de ser el juego más difícil del salón recreativo y odia a sus jugadores. Ahora que le falta una de sus armas, ¡le hierve la sagnre más que nunca!'), " +
                "('Rico','3','1','9','Rico es un cazarrecompensas espacial que persigue a los criminales más peligrosos de la galaxia. ¿Cómo? No, no es una máquina de chicles con aires de grandeza...'), " +
                "('Darryl','3','1','9','Darryl se puso el sombrero capitán pirata para intentar escaquearse del trabajo, ¡pero ahora tiene que defender su barco! ¿Quién le iba a decir que estar al mando conllevaría tanto esfuerzo?'), " +
                "('Penny','3','1','8','Penny no está interesada en leer maaps o descifrar viejos acertijos de marineros. Ella es una pirata, ¡así que puede hacer explotar cosas y llevarse lo que quiera!'), " +
                "('Carl','3','1','11','Carl es un robot minero que siente fasinación por las rocas y piedras de todo tipo. Podría pasarse horas y horas hablando de su última obsesión: ¡los efectos de las gemas!'), " +
                "('Jacky','3','1','8','Jacky es una minera a la que le encanta hablar de sus locas experiencias laborales con un lenguaje de lo más colorido. Por suerte, las palabrotas se ven ahogadas por el sonido de su martillo mecánico.'), " +
                "('Gus','3','1','5','Gus no es un niño fantasma, auqnue lo confundan a menudo con uno. Simplemente de la casualidad de uqe siente fascinación por lo sobrenatural y los lugares encantados'), " +
                "('Bo','4','1','10','Bo lleva mucho tiempo sobreviviendo en la selva. Lo más impresionante de todo es que para ello, utiliza juguetes que compró en rebajas en la tienda de souvenirs.'), " +
                "('Emz','4','1','6','En teoría, Emz trabaja en la funeraria de su tío Mortis, pero rara vez aparece por allí. Ser influencer en redes sociales y promocionar su laca para el pelo ya es bastante trabajo.'), " +
                "('Stu','4','1','7','A lo largo de su carrera como especialista en acrobacias, Stu se ha dado demasiados golpes y ha inhalado demasiados gases tóxicos, así que estos días le cuesta un poco mantenerse sobre la rueda.'), " +
                "('Piper','4','1','9','Piper sueña con se la pastelera local y preparar tartas, galletas y otras delicias dulces para todos. Eso sí, no lo preguntes por su pasado.'), " +
                "('Pam','4','1','5','Pam está hasta arriba de trabajo en la chatarrería, ¡pero siempre es capaz de lidiar con todo! Aunque es una pensa que no pueda pasar más tiempo con su hija, Jessie...'), " +
                "('Frank','4','1','10','Frank es un grandullón con un corazón de oro que echa una mano en la funeraria de dia y trabaja como DJ en la discoteca de noche. No duerme demasiado y se nota.'), " +
                "('Bibi','4','1','11','Si ves a Bibi acercarse con su bate, ten cuidado: es tan peligrosa como parece. También es un poco friki, pero es lo mantiene en secreto, ¡no puede manchar su reputación!'), " +
                "('Bea','4','1','8','Bea pasa el dia observando insectos e imaginando qué dirian si ella puediera entenderlos... O incluso cómo sería se una de ellos.'), " +
                "('Nani','4','1','6','Nani, una antigua cámara de seguridad, se encarga de echarle un ojo a Jessie, pero ser capaz de seguirle el ritmo es otro cantar. ¡Vaya con la niña! '), " +
                "('Edgar','4','1','11','Edgar cree que nadie lo entiende. Su madre la que menos, pues se piensa que está pasando por una fase. Solo él sabe que la oscuridad que se aferra a su almda es para siempre'), " +
                "('Griff','4','1','5','Griff tiene la actitud de un empresario rico y de éxito, pero su único negocio es la tienda de souvenirs, que está en bancarrota. Qué curioso que nunca esté cuando llaman los del banco.'), " +
                "('Grom','4','1','5','Grom es un musculoso guardia de seguridad con un punto débil: sus recuerdos traumáticos de cuando trabajaba en una guardería. Esos niños no tenían piedad.'), " +
                "('Bonnie','4','1','5','Bonnie está llena de una energía incrontrolable y destructiva, así que nunca para quieta. ¿Su sueño? Salir disparada de su cañón hasta alcanzar la luna.'), " +
                "('Gale','4','1','5','Gale se dedica a retirar la nieve que rodea el Hotel nevado del señor P con su poderoso quitanieves. Nadie le dijo que tuviera que parar si los clientes se cruzan en su camino...'), " +
                "('Colette','4','1','10','Colette cuenta en su colección con las figuras, peluches y juguetes de todos y cada uno de los brawlers. ¿Está obsesionada? Si. ¿Ha perdido la cabeza? Sin duda.'), " +
                "('Belle','4','1','5','Belle es la lider de la infame banda del brazo de oro y sus ambiciones van más allá del dinero. Lo que ella realmente quiere es descubrir la verdad sobre Starr Park ¡y llevarlo a su ruina!'), " +
                "('Ash','4','1','6','El sueño de Ash era ser actor, pero lejos de recibir aplausos, su ingrato trabajo consiste en limpiar el parque. Se ha metido en una papelera para protegerse de los cristales rotos y de las mordeduras de rata. Normal que esté de un humor de perros.'), " +
                "('Lola','4','1','8','Al entrar en una sala, Lola atrae todas las miradas. Es experta en montar una escena para conseguir lo que quiere ¡y se enorgullece mucho de ello!'), " +
                "('Sam','4','1','5','Sam, antiguo trabajador de la fábrica y actual miembro de la banda del brazo de oro, siempre se asegura de que Belle no se pase de la raya ¡por el bien de la banda!'), " +
                "('Mandy','4','1','5','En su tienda de caramelos, Mandy reparte golosinas vestida con un disfraz digno de la princesa de un dulce reino. A veces el papel se le sube a la cabeza y reina sobre sus dominios con puño de hierro.'), " +
                "('Maisie','4','1','5','Maisie trabaja como coordinadora de seguridad, pero en el fondo le encanta el riesgo. De hecho, es experta en lidiar con las situaciones más peligrosas, aunque a veces parece que sea quien las crea...'), " +
                "('Hank','4','1','2','Hank es una gamba soldada que, subida a su tanque, tiene como misión liberar a toda criatura marina de las cocinas y mercados del mundo entero. Chefs, ¡quedáis avisados!'), " +
                "('Pearl','4','1','4','Pearl ha conseguido derretir los duros corazones de Belle y Sam gracias a su carácter cálido y encantador. Además, ¿quién puede resistirse a unas galletas de chocolate recién hechas?'), " +
                "('Larry_y_Lawrie','4','1','2','Larry vende las entradas para Starr Park bajo la atenta mirada de su gemelo Lawrie. Larry adora las reglas, ¡hacen que todo sea mas fácil! A Lawrie le gusta más asegurarse de que se cumplan. Forman un gran equipo'), " +
                "('Angelo','4','1','1','Angelo es el Cupido en funciones del pantano del amor. Este mosquito revolotea por los canales en busca de tortolitos a los que deslumbrar con su encanto y a los que enseñar el significado de la palabra flechazo'), " +
                "('Mortis','5','1','14','Mortis avanza a palazos. Su súper consiste en invocar una bandada de murciélagos para queinflifan daño a los enemigos y así poder curarse a sí mismo.'), " +
                "('Tara','5','1','8','¡Las cartas le han dicho a Tara que se avecinan tiempos difíciles! Pero no te preocupes, porque cuenta con una fantástica selección de artículos mágicos que pueden ayudarte.'), " +
                "('Genio','5','1','6','La gente ve a Genio y piensa que no es más que un tipo bajito disfrazado. Lo que no ve es que su personalidad está dividida en dos: una parte en su cuerpo y la otra en su tetera.'), " +
                "('Max','5','1','8','Max se propulsa con su bebida energética y corre a toda velocidad a ayudar a quien lo necesite. Es cierto que siempre tiene tanta prisa que no le da tiempo a hacer mucho, ¡pero la intención es lo que cuenta!'), " +
                "('Señor_P','5','1','6','La gerencia del hotel lleva a Señor P de cabeza. No para de gritar a sus empleados para que trabajen más rápido, ¡pero esa no es forma de motivar al equipo!'), " +
                "('Sprout','5','1','7','A primera vista, Sprout parece un robot de jardinería adorable que ayuda a Rosa en el biodomo, ¡pero su función principal es incubar una semilla rosa de lo más extraña!'), " +
                "('Byron','5','1','7','Byron vende medicamentos extremadamente fuertes de eficacia probada. ¡Ni se te ocurra llamarle engañabobos!'), " +
                "('Squeak','5','1','8','Squeak es una criatura cargada de energia que adora a su creador, Ruffs. Es la alergia de huerta, aunque siempre deja todo perdido de babas'), " +
                "('Lou','5','1','6','Lou es un robot vendedor de helados, aunque nunca haya vendido uno. Probablemente esto se deba a que vive en el pico de una montaña nevada.'), " +
                "('Ruffs','5','1','5','Ruffs dispara un doble láser que rebota en los muros. Su súper son unos suministros aéreos que dañan a los rivales que se encuentren en la zona de entrega y depositan un potenciador para que lo utilice tu equipo. '), " +
                "('Buzz','5','1','13','Buzz trabaja como socorrista en el parque acuático de los Velocirrápidos y es un maniático de las reglas, Le encanta poder ejercer la poca autoridad que tiene sobre los toboganes y piscinas.'), " +
                "('Fang','5','1','9','Fang ha visto tantas películas de kung-fu que prácticamente vive dentro de una. Es encantador, rebosa confianza en sí mismo ¡y nunca usa las manos si puede dar una buena patada!'), " +
                "('Eve','5','1','5','Eve solo tiene un propósito en la vida: cuidar y proteger a sus adorables bebés. Hará lo que haga falta para encontrarles un buen hogar..., preferiblemente entre el pelaje de Ruffs.'), " +
                "('Janet','5','1','7','!Janet hará lo que sea para alcanzar el estrellato! además, sus habilidades interpretativas son muy útiles a la hora de rescatar a su hermana, Bonnie, cuando esta se mete en problemas'), " +
                "('Otis','5','1','5','Otis es un joven artista callejero en ciernes cuya técnica consiste en disparar tinta sobre los muros. Nadie conoce su verdadera identidad, ¡Lo que lo hace todavía más misterioso!'), " +
                "('Buster','5','1','4','Buster se llevó un proyector del cien en el que trabaja para poder usarlo por ahi como arma y atrezo. No es nada profesional, pero el piensa que le queda genial.'), " +
                "('Gray','5','1','5','Gray se presenta como un personaje de una vieja película de cine mudo. Se lo toma muy en serio, aunque a veces se le olvida mantener la boca cerrada cuando dispara balas con los dedos.'), " +
                "('R-T','5','1','4','La función principal de R-T es ser un puesto de información, pero también es el encargado de controlar toda la actividad que se lleve a cabo en Starr Park. Por razones de seguridad, claro está.'), " +
                "('Willow','5','1','2','Willow Trabaja como gondolera en el pantano del amor, donde se asegura de que las parejas de enamorados pasen la velada más espeluznante de sus vidas.'), " +
                "('Doug','5','1','3','Los perritos calientes de Doug alargan el tiempo de vida de tus amigos.'), " +
                "('Chuck','5','1','4','En el pasado fue un maestro brillante, Pero Chuck, el maquinista, cambio las sinfonías de los auditorios por las cacofonías de la estación fantasma con la esperanza de descubrir el sonido que revolucionara el mundo de la música'), " +
                "('Charlie','5','1','5','Nuestra amiga y vecina Charlie al público del circo tiene impresionado. Con sus dotes tejedoras de auténtica artista esta arácnida es la reina de la pista, pero el espectáculo llego a su fin, ¡cuidado! ¡Es hora de desvalijar el gentío embelsado!'), " +
                "('Mico','5','1','3','Con todas esas anécdotas sobre famosos, lo normal es pensar que Mico es una estrella del espectáculo, pero en realidad es el técnico de Sonido de Brawlywood. Irascible, impredecible y sin sentido del humor: hagas lo que hagas, nunca te rías delante de él.'), " +
                "('Melodie','5','1','1','Cuando se trata del karaoke, Melodie no tiene piedad. Gracias a su apariencia de estrella del pop y a unas cuerdas vocales monstruosas, esta brawler nunca pasa desapercibida.'), " +
                "('Spike','6','1','10','Aunque la gente piensa que Spike es el compañero adorable de Colt y Shelly en el rancho, nadie puede imaginar cuán traumático es su espinoso pasado...'), " +
                "('Crow','6','1','9','Crow no confía en nadie y está rodeado de un aura de misterio. Lo único que se sabe de él es que frecuenta la cafetería con Bull y Bibi.'), " +
                "('Leon','6','1','12','Leon es más bien un lobo solitario, así que su habilidad de volverse invisible le va al pelo. Su hermana, Nita, es la única con la que se muestra tal y como es.'), " +
                "('Sandy','6','1','7','En las contadas ocasiones en que Sandy despierta de su profundo sueño, suele ayudar a su hermana Tara en la tienda. Aunque no aguanta mucho tiempo en pie...'), " +
                "('Amber','6','1','4','¡Esto está que arde! Amber siente plena confianza en su habilidad para controlar el fuego, pero es un poco torpe. ¿Su lema? Aunque me queme el pelo, ¡el espectáculo es lo primero!'), " +
                "('Meg','6','1','6','Meg enfundada en su traje meca, se encarga entre bambalinas de solucionar los problemas de verdad mientras Max y Surge se dedican a entretener al público.'), " +
                "('Surge','6','1','8','Surge, además de expender bebidas, ¡también es el alma de la fiesta! Es todo un máquina en la pista de baile ¡y su música carga al público de energía!'), " +
                "('Chester','6','1','5','Chester se burlará de quien sea hasta sacarlo de sus casillas, ¡y cuanto más, mejor! Y si ese alguien es Mandy, ¡no parará hasta hacerla rabiar!'), " +
                "('Cordelius','6','1','6','Cordelius el jardinero y guardián del bosque encantado. Le chiflan los champiñones y es hostil con los desconocidos.'), " +
                "('Kit','6','1','4','Cansado de firmar autógrafos en los Estudios Starr de Animación, Kit sueña con volver a ser alguien en la industria. Alguien reconocido por su talento más que por su apariencia...'), " +
                "('Lily','5','1','1','El ansia de conocimiento de Lily, una atuéntica aficionada a la brujería, la llevó al bosque Encantado, donde un incidente con una luciérnaga y una planta carnivora cambiaria su vida para siempre...')";

            st.executeUpdate(insertaBrawlers);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}