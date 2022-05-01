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
import java.util.*;

public class WordGuesserController implements Initializable {

    @FXML
    private Button makeGuessBtn;

    @FXML
    private GridPane gridPane;

    private String word;

    private int attemptCounter = 0;

    private boolean guessed = false;

    private int maxCounter = 6;

    private WordList wordList = new WordList();

    private String[] words = wordList.getWords();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        word = getWord();
        System.out.println(word);
        int textFieldId = 0;

        //Generate textFields for every gridPane cell
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                TextField textField = new TextField();
                textField.setAlignment(Pos.CENTER);
                int finalTextFieldId = textFieldId;
                //textField.setPromptText(String.valueOf(finalTextFieldId));

                if (i != attemptCounter) {
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
        } else if (!newValue.isBlank()) {
            if (finalTextFieldId < maxCounter * word.length() && finalTextFieldId<29)
                gridPane.getChildren().get(finalTextFieldId + 1).requestFocus();
            if ((finalTextFieldId + 1) % word.length() == 0) {
                makeGuessBtn.requestFocus();
            }
        }
    }


    public void makeGuess() {
        TreeMap<Integer, String> currentGuessTM = new TreeMap<>();

        if (!guessed && attemptCounter < maxCounter) {
            //for each letter in current line add it to HashMap
            for (int i = word.length() * attemptCounter; i < word.length() * attemptCounter + word.length(); i++) {
                Node node = gridPane.getChildren().get(i);
                if (node instanceof TextField) {
                    TextField textField = (TextField) node;
                    String letter = textField.getText().toLowerCase();
                    currentGuessTM.put(i, letter);
                }
            }


            //combine char array to string
            StringBuilder sb = new StringBuilder();
            for (Integer i : currentGuessTM.keySet()) {
                System.out.println(currentGuessTM);
                sb.append(currentGuessTM.get(i));
            }
            String currentGuess = sb.toString();

            //if word is in word list proceed to the game logic
            if (Arrays.asList(words).contains(currentGuess)) {

                for (Integer i : currentGuessTM.keySet()) {
                    Node node = gridPane.getChildren().get(i);
                    if (node instanceof TextField) {
                        TextField textField = (TextField) node;
                        if (word.contains(currentGuessTM.get(i))) {
                            textField.setStyle("-fx-background-color: yellow;");
                            if (String.valueOf(word.charAt(i - attemptCounter * word.length())).equalsIgnoreCase(currentGuessTM.get(i))) {
                                textField.setStyle("-fx-background-color: green;");
                            }
                        } else textField.setStyle("-fx-background-color: gray;");
                    }
                    if (currentGuess.equals(word)){
                        guessed = true;
                    }
                    if (!guessed && attemptCounter<maxCounter){
                            for (int j=i+1; j<i + word.length() + 1 && j<maxCounter*word.length(); j++) {
                            Node nextNode = gridPane.getChildren().get(j);
                            if (nextNode instanceof TextField) {
                                ((TextField) nextNode).setEditable(true);
                            }
                            if (j % word.length() == 0){
                                nextNode.requestFocus();
                            }
                        }
                    }
                }
                attemptCounter++;
            } else System.out.println("Not in a word list!");
        }
        System.out.println(attemptCounter);
    }


    private String getWord() {
        int r = (int) (Math.random() * words.length);
        return words[r];
    }

    public void resetScene(ActionEvent event) throws IOException {
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
