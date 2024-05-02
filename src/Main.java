import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class Main extends Application {

    private boolean usePooling = false;

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        Button btnWithPooling = new Button("With Object Pooling");
        Button btnWithoutPooling = new Button("Without Object Pooling");

        btnWithPooling.setOnAction(e -> {
            usePooling = true;
            launchApp(stage);
        });

        btnWithoutPooling.setOnAction(e -> {
            usePooling = false;
            launchApp(stage);
        });

        root.getChildren().addAll(btnWithPooling, btnWithoutPooling);
        Scene scene = new Scene(root, 300, 100);
        stage.setTitle("Object Pooling Example");
        stage.setScene(scene);
        stage.show();
    }

    private void launchApp(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("fxml.fxml")));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.setUsePooling(usePooling);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
