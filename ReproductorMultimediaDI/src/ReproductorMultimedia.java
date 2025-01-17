import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class ReproductorMultimedia extends Application {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    @Override
    public void start(Stage primaryStage) {
        // Crear el diseño base
        BorderPane root = new BorderPane();

        // Sección superior: barra de menú
        MenuBar menuBar = crearBarraDeMenu(primaryStage);
        root.setTop(menuBar);

        // Sección lateral izquierda: panel de edición
        ToolBar panelEdicion = crearPanelDeEdicion();
        root.setLeft(panelEdicion);

        // Sección lateral derecha: panel de biblioteca
        ToolBar panelBiblioteca = crearPanelDeBiblioteca();
        root.setRight(panelBiblioteca);

        // Sección central: reproductor multimedia
        mediaView = new MediaView();
        root.setCenter(mediaView);

        // Sección inferior: controles de reproducción
        ToolBar controlesReproduccion = crearControlesDeReproduccion(primaryStage);
        root.setBottom(controlesReproduccion);

        // Configurar y mostrar la escena
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Reproductor Multimedia");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar crearBarraDeMenu(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();

        Menu menuArchivo = new Menu("Archivo");
        MenuItem abrirArchivo = new MenuItem("Abrir Archivo");
        abrirArchivo.setOnAction(e -> abrirArchivo(primaryStage));
        menuArchivo.getItems().add(abrirArchivo);

        Menu menuBiblioteca = new Menu("Biblioteca");
        Menu menuVer = new Menu("Ver");
        Menu menuAcerca = new Menu("Acerca");

        menuBar.getMenus().addAll(menuArchivo, menuBiblioteca, menuVer, menuAcerca);

        return menuBar;
    }

    private ToolBar crearPanelDeEdicion() {
        ToolBar toolBar = new ToolBar();
        Button btnCambiarTamano = new Button("Cambiar Tamaño");
        Button btnCambiarVelocidad = new Button("Cambiar Velocidad");

        toolBar.getItems().addAll(btnCambiarTamano, btnCambiarVelocidad);

        return toolBar;
    }

    private ToolBar crearPanelDeBiblioteca() {
        ToolBar toolBar = new ToolBar();
        Label lblBiblioteca = new Label("Biblioteca Multimedia");
        toolBar.getItems().add(lblBiblioteca);

        return toolBar;
    }

    private ToolBar crearControlesDeReproduccion(Stage primaryStage) {
        ToolBar toolBar = new ToolBar();

        Button btnPlay = new Button("Play");
        Button btnPause = new Button("Pause");
        Button btnStop = new Button("Stop");
        Button btnAbrirArchivo = new Button("Abrir Archivo");

        btnPlay.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.play();
            }
        });

        btnPause.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        btnStop.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });

        btnAbrirArchivo.setOnAction(e -> abrirArchivo(primaryStage));

        toolBar.getItems().addAll(btnPlay, btnPause, btnStop, btnAbrirArchivo);

        return toolBar;
    }

    private void abrirArchivo(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos Multimedia", "*.mp4", "*.mp3", "*.wav")
        );
        File archivo = fileChooser.showOpenDialog(primaryStage);

        if (archivo != null) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            Media media = new Media(archivo.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
