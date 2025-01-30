import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.AudioSpectrumListener;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReproductorController {

    @FXML
    private VBox bibliotecaPanel;

    @FXML
    private Button btnToggleBiblioteca;

    @FXML
    private ListView<String> listaArchivos;

    @FXML
    private StackPane mediaContainer;

    @FXML
    private MediaView mediaView;

    @FXML
    private ImageView audioPlaceholder;

    @FXML
    private Slider progressBar;

    @FXML
    private Canvas audioVisualizer;

    private MediaPlayer mediaPlayer;
    private List<File> archivosEnBiblioteca = new ArrayList<>();
    private boolean bibliotecaVisible = true;

    @FXML
    private void toggleBiblioteca() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), bibliotecaPanel);

        if (bibliotecaVisible) {
            transition.setToX(-bibliotecaPanel.getWidth());  // Oculta la biblioteca
            btnToggleBiblioteca.setText("⏩");  // Cambia el ícono del botón
        } else {
            transition.setToX(0);  // Muestra la biblioteca
            btnToggleBiblioteca.setText("⏪");
        }

        transition.play();
        bibliotecaVisible = !bibliotecaVisible;
    }

    @FXML
    private void abrirArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos Multimedia", "*.mp4", "*.mp3", "*.wav"));
        File archivo = fileChooser.showOpenDialog(mediaView.getScene().getWindow());

        if (archivo != null) {
            reproducirArchivo(archivo);
        }
    }

    @FXML
    private void abrirDirectorio() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directorio = directoryChooser.showDialog(mediaView.getScene().getWindow());

        if (directorio != null && directorio.isDirectory()) {
            archivosEnBiblioteca.clear();
            listaArchivos.getItems().clear();

            for (File archivo : directorio.listFiles()) {
                if (archivo.isFile() && (archivo.getName().endsWith(".mp4") || archivo.getName().endsWith(".mp3")
                        || archivo.getName().endsWith(".wav"))) {
                    archivosEnBiblioteca.add(archivo);
                    listaArchivos.getItems().add(archivo.getName());
                }
            }

            if (archivosEnBiblioteca.isEmpty()) {
                listaArchivos.setPlaceholder(
                        new javafx.scene.control.Label("No hay archivos multimedia en este directorio"));
            }
        }
    }

    @FXML
    private void mostrarAcercaDe() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Acerca de Reproductor Multimedia");
        alerta.setHeaderText("Información del Reproductor Multimedia");
        alerta.setContentText("Reproductor Multimedia v1.0 Desarrollado por Rober Rey.");
        alerta.showAndWait();
    }

    @FXML
    private void cambiarVelocidadReproduccion() {
        if (mediaPlayer != null) {
            double velocidadActual = mediaPlayer.getRate();
            mediaPlayer.setRate(velocidadActual < 2.0 ? velocidadActual + 0.5 : 0.5);
        }
    }

    @FXML
    private void setupAudioVisualizer(MediaPlayer player) {
        audioVisualizer.setVisible(true);
        GraphicsContext gc = audioVisualizer.getGraphicsContext2D();
        player.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            gc.clearRect(0, 0, audioVisualizer.getWidth(), audioVisualizer.getHeight());
            for (int i = 0; i < magnitudes.length; i++) {
                double x = i * (audioVisualizer.getWidth() / magnitudes.length);
                double height = ((60 + magnitudes[i]) * 2);
                gc.fillRect(x, audioVisualizer.getHeight() - height, 5, height);
            }
        });
    }

    @FXML
    private void actualizarBarraProgreso() {
        if (mediaPlayer != null) {
            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                if (mediaPlayer.getTotalDuration() != null && !mediaPlayer.getTotalDuration().isIndefinite()) {
                    double progreso = newTime.toSeconds() / mediaPlayer.getTotalDuration().toSeconds() * 100;
                    progressBar.setValue(progreso);
                }
            });

            progressBar.setOnMouseReleased(event -> {
                if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue() * mediaPlayer.getTotalDuration().toSeconds() / 100));
                }
            });
        }
    }

    private void reproducirArchivo(File archivo) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        Media media = new Media(archivo.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setOnReady(() -> actualizarBarraProgreso());
        mediaPlayer.play();

        if (archivo.getName().endsWith(".mp3") || archivo.getName().endsWith(".wav")) {
            mediaView.setVisible(false);
            audioPlaceholder.setVisible(false);
            audioVisualizer.setVisible(true);
            setupAudioVisualizer(mediaPlayer);
        } else {
            mediaView.setVisible(true);
            audioPlaceholder.setVisible(false);
            audioVisualizer.setVisible(false);
        }
    }
}
