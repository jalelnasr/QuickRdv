package tn.esprit.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.File;

public class PDFViewer extends Application {
    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        File file = new File("/C:/Users/LENOVO/OneDrive/Documents/ordonnance.pdf");
        webView.getEngine().load(file.toURI().toString());

        BorderPane root = new BorderPane();
        root.setCenter(webView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Visualisation de l'Ordonnance");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
