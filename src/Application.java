import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import poolObject.PoolObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Application {

    private boolean doTick = false;

    private final int MAX_TICKS_TO_SPAWN = 30;

    private int ticksToSpawn = 0;

    @FXML
    private Pane root;

    private ArrayList<PoolObject> objects = new ArrayList<>();

    @FXML
    public void initialize() {
        root.setFocusTraversable(true);
    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.SPACE) {
            if (ticksToSpawn <= 0) {
                root.setStyle("-fx-background-color: #FF0000;");
                instantiate(new PoolObject());
                ticksToSpawn = MAX_TICKS_TO_SPAWN;
            }
            tick();
            ticksToSpawn--;
        }
    }

    private void tick() {
        ArrayList<PoolObject> temp = new ArrayList<>();
        for (PoolObject object : objects){
            object.step();
            if (object.stepsToLive <= 0) {
                destroy(object);
            } else {
                temp.add(object);
            }
        }
        objects = temp;
    }

    public void instantiate(PoolObject object) {
        try {
            TimeUnit.SECONDS.sleep(3);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        root.getChildren().add(object.getImage());
        objects.add(object);
    }

    public void destroy(PoolObject object) {
        root.getChildren().remove(object.getImage());
    }
}
