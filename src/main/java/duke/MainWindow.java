package duke;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Duke duke;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/Duke.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setDuke(Duke d) {
        duke = d;

        sendMessage(duke.getUi().greet());

        try {
            duke.getStorage().load(duke.getTaskList());
        } catch (FileNotFoundException e) {
            sendMessage("OOPS!!! Can't access task data.");
        } catch (IOException e) {
            sendMessage("OOPS!!! Something went wrong... Tasks not saved.");
        }
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        duke.getParser().setScanner(new Scanner(userInput.getText()));
        String reply = duke.getParser().executeCommand(duke.getTaskList());
        if (reply.equals("bye")) {
            reply = duke.getUi().bye();
        }
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userInput.getText(), userImage),
                DialogBox.getDukeDialog(reply, dukeImage)
        );
        userInput.clear();

        try {
            duke.getStorage().save(duke.getTaskList());
        } catch (IOException e) {
            sendMessage("OOPS!!! Something went wrong... Tasks not saved.");
        }
    }

    private void sendMessage(String text) {
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(text, dukeImage)
        );
    }
}
