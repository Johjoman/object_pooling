import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class Application {

    private boolean doTick = false;

    @FXML
    private Pane root;

    @FXML
    public void initialize() {
        root.setFocusTraversable(true);
    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.SPACE) {
            System.out.println("Spawning Objects");
        }
    }
}
