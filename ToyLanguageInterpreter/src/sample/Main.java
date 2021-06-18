package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.controllers.SelectProgramController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ToyLanguageInterpreter");

        FXMLLoader runProgramFXMLLoader = new FXMLLoader();
        runProgramFXMLLoader.setLocation(getClass().getResource("fxmls/runProgram.fxml"));
        Parent runProgramSceneRoot = runProgramFXMLLoader.load();
        Scene runProgramScene = new Scene(runProgramSceneRoot);

        FXMLLoader selectProgramFXMLLoader = new FXMLLoader();
        selectProgramFXMLLoader.setLocation(getClass().getResource("fxmls/selectProgram.fxml"));
        Parent selectProgramSceneRoot = selectProgramFXMLLoader.load();
        Scene selectProgramScene = new Scene(selectProgramSceneRoot);

        SelectProgramController selectProgramController = selectProgramFXMLLoader.getController();
        selectProgramController.setRunProgramController(runProgramFXMLLoader.getController());

        primaryStage.setScene(selectProgramScene);
        Stage secondaryStage = new Stage();
        secondaryStage.setScene(runProgramScene);
        primaryStage.show();
        secondaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
