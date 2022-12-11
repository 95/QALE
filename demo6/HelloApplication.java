package com.example.demo6;

import com.example.demo6.Client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 533, 400);
        stage.setTitle("QALE Encrypted Messenger");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1112);
        Client client = new Client(socket, 1);
        client.listenForMessage();
        launch();

    }
}