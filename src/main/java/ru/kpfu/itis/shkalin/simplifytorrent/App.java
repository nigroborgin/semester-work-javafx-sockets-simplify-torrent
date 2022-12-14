package ru.kpfu.itis.shkalin.simplifytorrent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;


public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

//        primaryStage.setMinWidth(700);
        primaryStage.getIcons().add(new Image("/ru/kpfu/itis/shkalin/simplifytorrent/icon.png"));
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/ru/kpfu/itis/shkalin/simplifytorrent/views/main-page.fxml");
        loader.setLocation(xmlUrl);

        Parent root = loader.load();

        primaryStage.setTitle("Simplify Torrent Client");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
