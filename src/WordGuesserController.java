import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WordGuesserController implements Initializable {

    @FXML
    private Button makeGuessBtn;

    @FXML
    private GridPane gridPane;

    private String word = "watch";

    private int counter = 0;

    boolean guessed = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int textFieldId = 0;

        //Generate textFields for every gridPane cell
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                TextField textField = new TextField();
                textField.setAlignment(Pos.CENTER);
                int finalTextFieldId = textFieldId;
                textField.setPromptText(String.valueOf(finalTextFieldId));

                if (i != counter) {
                    textField.setEditable(false);
                }
                //add input listener to textField
                textField.textProperty().addListener(((observableValue, oldValue, newValue) -> validateInput(textField, oldValue, newValue, finalTextFieldId)));

                gridPane.add(textField, j, i);
                textFieldId++;
            }
        }
    }


    /**
     * This method serves to ensure that user input is only 1 letter in textField
     * If input is valid, change focus to the next textField
     *
     * @param textField        individual textField object
     * @param oldValue         previous value of textField
     * @param newValue         new value of textField
     * @param finalTextFieldId textField ID in parent gridPane (needed for proper tabulation)
     */
    private void validateInput(TextField textField, String oldValue, String newValue, int finalTextFieldId) {
        if (!newValue.matches("[A-Za-z]") && !newValue.isBlank() || textField.getText().length() > 1) {
            textField.setText(oldValue);
        } else if (!newValue.isBlank()){
            System.out.println((finalTextFieldId+1) % word.length());
            if (finalTextFieldId < 29)
                gridPane.getChildren().get(finalTextFieldId + 1).requestFocus();
            if ((finalTextFieldId+1) % word.length() == 0){
                makeGuessBtn.requestFocus();
            }
        }
    }


    public void makeGuess() {
        if (!guessed && counter < 6){

                int lettersGuessed = 0;

                //for each letter in current line


                for (int i = word.length() * counter; i < word.length() * counter + word.length(); i++) {
                    Node node = gridPane.getChildren().get(i);
                    if (node instanceof TextField) {
                        TextField textField = (TextField) node;
                        String letter = textField.getText();
                        if (!letter.isBlank() && word.contains(letter)) {
                            textField.setStyle("-fx-background-color: yellow;");
                            if (letter.equalsIgnoreCase(String.valueOf(word.charAt(i - counter * word.length())))) {
                                textField.setStyle("-fx-background-color: green;");
                                lettersGuessed++;
                            }
                        } else if (!letter.isBlank()){
                            textField.setStyle("-fx-background-color: gray;");
                        }
                    }
                }

                counter++;

                if (lettersGuessed == word.length()) {
                    guessed = true;
                }


                if (counter<word.length()) {
                    Node node = gridPane.getChildren().get(counter*word.length());
                    if (node instanceof TextField) {
                        node.requestFocus();
                    }
                }


                for (int i = word.length() * counter; i < word.length() * counter + word.length(); i++) {
                    Node node = gridPane.getChildren().get(i);
                    if (node instanceof TextField) {
                        TextField textField = (TextField) node;
                        textField.setEditable(true);
                    }
                }
            }

            System.out.println(counter);
            System.out.println(guessed);
        }


    public void getNewWord(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new Object() {
        }.getClass().getResource("WordGuesserView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
