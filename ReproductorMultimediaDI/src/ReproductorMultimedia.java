import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ReproductorMultimedia extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear el diseño base
        BorderPane root = new BorderPane();

        // Sección superior: barra de menú
        MenuBar menuBar = crearBarraDeMenu();
        root.setTop(menuBar);

        // Sección lateral izquierda: panel de edición
        ToolBar panelEdicion = crearPanelDeEdicion();
        root.setLeft(panelEdicion);

        // Sección lateral derecha: panel de biblioteca
        ToolBar panelBiblioteca = crearPanelDeBiblioteca();
        root.setRight(panelBiblioteca);

        // Sección central: reproductor multimedia (placeholder por ahora)
        Label placeholderReproductor = new Label("Reproductor Multimedia");
        placeholderReproductor.setStyle("-fx-alignment: center; -fx-font-size: 16px;");
        root.setCenter(placeholderReproductor);

        // Sección inferior: controles de reproducción
        ToolBar controlesReproduccion = crearControlesDeReproduccion();
        root.setBottom(controlesReproduccion);

        // Configurar y mostrar la escena
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Reproductor Multimedia");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar crearBarraDeMenu() {
        MenuBar menuBar = new MenuBar();

        Menu menuArchivo = new Menu("Archivo");
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

    private ToolBar crearControlesDeReproduccion() {
        ToolBar toolBar = new ToolBar();
        Button btnPlay = new Button("Play");
        Button btnPause = new Button("Pause");
        Button btnStop = new Button("Stop");

        toolBar.getItems().addAll(btnPlay, btnPause, btnStop);

        return toolBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
