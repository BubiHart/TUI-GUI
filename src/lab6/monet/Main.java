package lab6.monet;

import com.mybank.data.DataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        retrieveDataFromFile();
        Parent root = FXMLLoader.load(getClass().getResource("MonetDemo.fxml"));
        primaryStage.setTitle("Simple Monet demo");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    private void retrieveDataFromFile() {
        DataSource dataSource = new DataSource("data/test.dat");
        try {
            dataSource.loadData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No file, data loading failed");
            alert.showAndWait();
        }
    }
}
