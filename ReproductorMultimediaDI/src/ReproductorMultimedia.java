import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReproductorMultimedia extends Application {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private ListView<String> listaArchivos;
    private List<File> archivosEnBiblioteca;

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
        listaArchivos = new ListView<>();
        archivosEnBiblioteca = new ArrayList<>();
        listaArchivos.setPlaceholder(new Label("No hay archivos disponibles"));
        listaArchivos.setOnMouseClicked(event -> reproducirArchivoSeleccionado());
        root.setRight(listaArchivos);

        // Sección central: reproductor multimedia
        mediaView = new MediaView();
        mediaView.setPreserveRatio(true);
        root.setCenter(mediaView);

        // Sección inferior: controles de reproducción
        ToolBar controlesReproduccion = crearControlesDeReproduccion(primaryStage);
        root.setBottom(controlesReproduccion);

        // Configurar y mostrar la escena
        Scene scene = new Scene(root, 800, 600);
        mediaView.fitWidthProperty().bind(scene.widthProperty());
        mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(100)); // Reservar espacio para controles

        primaryStage.setTitle("Reproductor Multimedia");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar crearBarraDeMenu(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();

        Menu menuArchivo = new Menu("Archivo");
        MenuItem abrirDirectorio = new MenuItem("Abrir Directorio");
        abrirDirectorio.setOnAction(e -> abrirDirectorio(primaryStage));
        menuArchivo.getItems().add(abrirDirectorio);

        Menu menuBiblioteca = new Menu("Biblioteca");
        Menu menuVer = new Menu("Ver");
        Menu menuAcerca = new Menu("Acerca");

        MenuItem acercaDe = new MenuItem("Acerca de...");
        acercaDe.setOnAction(e -> mostrarAcercaDe());
        menuAcerca.getItems().add(acercaDe);

        menuBar.getMenus().addAll(menuArchivo, menuBiblioteca, menuVer, menuAcerca);

        return menuBar;
    }

    private ToolBar crearPanelDeEdicion() {
        ToolBar toolBar = new ToolBar();
        Button btnCambiarTamano = new Button("Cambiar Tamaño");
        Button btnCambiarVelocidad = new Button("Cambiar Velocidad");

        btnCambiarTamano.setOnAction(e -> cambiarTamanoMediaView());
        btnCambiarVelocidad.setOnAction(e -> cambiarVelocidadReproduccion());

        toolBar.getItems().addAll(btnCambiarTamano, btnCambiarVelocidad);

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

    private void abrirDirectorio(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directorio = directoryChooser.showDialog(primaryStage);

        if (directorio != null && directorio.isDirectory()) {
            archivosEnBiblioteca.clear();
            listaArchivos.getItems().clear();

            for (File archivo : directorio.listFiles()) {
                if (archivo.isFile() && (archivo.getName().endsWith(".mp4") || archivo.getName().endsWith(".mp3") || archivo.getName().endsWith(".wav"))) {
                    archivosEnBiblioteca.add(archivo);
                    listaArchivos.getItems().add(archivo.getName());
                }
            }

            if (archivosEnBiblioteca.isEmpty()) {
                listaArchivos.setPlaceholder(new Label("No hay archivos multimedia en este directorio"));
            }
        }
    }

    private void reproducirArchivoSeleccionado() {
        int indice = listaArchivos.getSelectionModel().getSelectedIndex();
        if (indice >= 0) {
            File archivoSeleccionado = archivosEnBiblioteca.get(indice);

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            Media media = new Media(archivoSeleccionado.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        }
    }

    private void cambiarTamanoMediaView() {
        if (mediaPlayer != null && mediaPlayer.getMedia() != null) {
            double videoWidth = mediaPlayer.getMedia().getWidth();
            double videoHeight = mediaPlayer.getMedia().getHeight();

            if (videoWidth > 0 && videoHeight > 0) {
                mediaView.setFitWidth(videoWidth);
                mediaView.setFitHeight(videoHeight);
            } else {
                mediaView.setFitWidth(800); // Valores por defecto si no se pueden obtener dimensiones
                mediaView.setFitHeight(450);
            }
        }
    }

    private void cambiarVelocidadReproduccion() {
        if (mediaPlayer != null) {
            double velocidadActual = mediaPlayer.getRate();
            mediaPlayer.setRate(velocidadActual < 2.0 ? velocidadActual + 0.5 : 0.5); // Cambiar entre 0.5x y 2x
        }
    }

    private void mostrarAcercaDe() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Acerca de Reproductor Multimedia");
        alerta.setHeaderText("Información del Reproductor Multimedia");
        alerta.setContentText("Reproductor Multimedia v1.0\nDesarrollado por ");
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
