package it.ispw.booknook;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import it.ispw.booknook.logic.SupportedView;
import it.ispw.booknook.logic.boundary.secondary_view.StartView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/it/ispw/booknook/mainView/login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("BookNook");
        stage.setScene(scene);
        root.requestFocus();
        stage.show();
    }

    public static void main(String[] args) {
        ArcGISRuntimeEnvironment.setInstallDirectory("C:\\Users\\Simonetta\\Desktop\\ISPW\\.arcgis\\100.15.0");
        String k = "AAPK26b105d18af3457a9265837b9abf295b5q_qht8-El1IQE4HeVuW13Owo8VQXOzKJwPJ9Bu4e_S9EO7YYER8Jn20YUSNe2lN";
        ArcGISRuntimeEnvironment.setApiKey(k);
        //lancia interfaccia grafica o con linea di comando
        if (SupportedView.valueOf(args[0]) == SupportedView.JAVAFX) {
           launch();
        }
        else {
            StartView cl = new StartView();
            cl.startCLView();
        }
    }

}