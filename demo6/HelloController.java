package com.example.demo6;

import com.example.demo6.Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.Socket;

public class HelloController {
    Socket socket = new Socket("localhost", 1112);
    Client client = new Client(socket, 1);

    Long destinationID = Long.valueOf(2);

    @FXML private TextField chooseID;

    @FXML private TextField enterDestination;

    @FXML
    private Pane convoPane;

    @FXML
    private Label convobar;

    @FXML
    private TextField messageInput;

    @FXML
    private TextArea textArea;

    public HelloController() throws IOException {
    }

    public void receiveMessage(Long ID, String message){
        client.listenForMessage();
        textArea.appendText(ID + ": " + message + "\n");
    }

    public void addInformation(String information){
        textArea.appendText(information + "\n");
    }


    @FXML
    void broadcast(ActionEvent event) {
        client.send(destinationID, client.getMyGivenID() + ": " + messageInput.getText());
        textArea.appendText(client.getMyGivenID() + ": " + messageInput.getText() + "\n");

    }

    @FXML
    void choose(ActionEvent event){
        client.setMyGivenID(Long.valueOf(chooseID.getText()));
        System.out.println("Set client ID to " + client.getMyGivenID());
    }

    @FXML
    void enterDest(ActionEvent event){
        destinationID = Long.valueOf(enterDestination.getText());
        System.out.println("Set destination ID to " + destinationID);
    }
}
