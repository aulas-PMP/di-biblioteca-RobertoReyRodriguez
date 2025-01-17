import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReproductorMultimedia extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el diseño desde el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("ReproductorMultimedia.fxml"));

        // Crear la escena con el CSS externo
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("ReproductorMultimedia.css").toExternalForm());

        // Configurar y mostrar la ventana principal
        primaryStage.setTitle("Reproductor Multimedia");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
