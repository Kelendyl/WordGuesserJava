import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class StatisticsController implements Initializable {

    @FXML
    private PieChart attemptsPieChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try {
            ArrayList<Integer> guessCounts = DBUtility.getAttemptStatistics();
            for (int i = 1; i<7; i++){
                PieChart.Data data = new PieChart.Data(String.valueOf(i), Collections.frequency(guessCounts, i));
                if (i == 1){
                    data.nameProperty().bind(Bindings.concat(data.getName(), " Guess"));
                } else {
                    data.nameProperty().bind(Bindings.concat(data.getName(), " Guesses"));
                }
                pieChartData.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        attemptsPieChart.setData(pieChartData);
    }
}
