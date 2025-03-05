package tn.esprit.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

public class SignatureApp extends Application {

    private Path signaturePath = new Path();

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: white;");

        signaturePath.setStroke(Color.BLACK);
        signaturePath.setStrokeWidth(2);
        signaturePath.setStrokeLineCap(StrokeLineCap.ROUND);

        // Gérer le dessin à la souris
        pane.setOnMousePressed(this::handleMousePressed);
        pane.setOnMouseDragged(this::handleMouseDragged);

        pane.getChildren().add(signaturePath);

        Scene scene = new Scene(pane, 400, 200);
        primaryStage.setTitle("Signature Manuelle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleMousePressed(MouseEvent event) {
        signaturePath.getElements().clear();
        signaturePath.getElements().add(new javafx.scene.shape.MoveTo(event.getX(), event.getY()));
    }

    private void handleMouseDragged(MouseEvent event) {
        signaturePath.getElements().add(new javafx.scene.shape.LineTo(event.getX(), event.getY()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
