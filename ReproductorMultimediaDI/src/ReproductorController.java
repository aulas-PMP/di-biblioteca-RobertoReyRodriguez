import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReproductorController {

    @FXML
    private MediaView mediaView;

    @FXML
    private ListView<String> listaArchivos;

    private MediaPlayer mediaPlayer;
    private List<File> archivosEnBiblioteca = new ArrayList<>();

    @FXML
    private void abrirArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos Multimedia", "*.mp4", "*.mp3", "*.wav")
        );
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
                if (archivo.isFile() && (archivo.getName().endsWith(".mp4") || archivo.getName().endsWith(".mp3") || archivo.getName().endsWith(".wav"))) {
                    archivosEnBiblioteca.add(archivo);
                    listaArchivos.getItems().add(archivo.getName());
                }
            }

            if (archivosEnBiblioteca.isEmpty()) {
                listaArchivos.setPlaceholder(new javafx.scene.control.Label("No hay archivos multimedia en este directorio"));
            }
        }
    }

    @FXML
    private void cambiarTamanoMediaView() {
        if (mediaView != null) {
            mediaView.setFitWidth(800);
            mediaView.setFitHeight(600);
        }
    }

    @FXML
    private void cambiarVelocidadReproduccion() {
        if (mediaPlayer != null) {
            double velocidadActual = mediaPlayer.getRate();
            mediaPlayer.setRate(velocidadActual < 2.0 ? velocidadActual + 0.5 : 0.5);
        }
    }

    @FXML
    private void playMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    private void pauseMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @FXML
    private void mostrarAcercaDe() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Acerca de Reproductor Multimedia");
        alerta.setHeaderText("Información del Reproductor Multimedia");
        alerta.setContentText("Reproductor Multimedia v1.0\nDesarrollado por [Tu Nombre]\n\u00A9 2025");
        alerta.showAndWait();
    }

    private void reproducirArchivo(File archivo) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        Media media = new Media(archivo.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }

    @FXML
    private void reproducirArchivoSeleccionado() {
        int indice = listaArchivos.getSelectionModel().getSelectedIndex();
        if (indice >= 0) {
            File archivoSeleccionado = archivosEnBiblioteca.get(indice);
            reproducirArchivo(archivoSeleccionado);
        }
    }
}
