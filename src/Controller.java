import Logger.Logger;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import poolObject.PoolObject;

import java.util.ArrayList;

public class Controller {
    @FXML
    private Pane root;

    private boolean usePooling;
    private ObjectPool<PoolObject> pool;
    private ArrayList<PoolObject> objects = new ArrayList<>();

    private FixedRateTimer timer;

    public void setUsePooling(boolean usePooling) {
        this.usePooling = usePooling;
        if (usePooling) {
            pool = ObjectPool.getInstance(PoolObject.class);
        }
    }

    @FXML
    public void initialize() {
        root.setFocusTraversable(true);
        setupFixedRateTimer();
    }

    private void setupFixedRateTimer() {
        timer = new FixedRateTimer();
        timer.start();
    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.SPACE) {
            Logger.getInstance().log("Space bar pressed.", Logger.LogLevel.INFO);
            instantiateNewObject();

        }
    }

    private void instantiateNewObject() {
        PoolObject object;
        if (usePooling) {
            object = pool.get();
        } else {
            object = new PoolObject();
        }
        root.getChildren().add(object.getImage());
        objects.add(object);
        Logger.getInstance().log("New object instantiated and added to scene.", Logger.LogLevel.INFO);
    }


    private void tick() {
        ArrayList<PoolObject> temp = new ArrayList<>();
        for (PoolObject object : objects) {
            object.step();
            if (object.isDead) {
                destroy(object);
            } else {
                temp.add(object);
            }
        }
        objects = temp;
    }


    private void destroy(PoolObject object) {
        root.getChildren().remove(object.getImage());
        if (usePooling) {
            pool.release(object);
            Logger.getInstance().log("Object marked as dead and returned to pool.", Logger.LogLevel.INFO);
        }
    }

    private class FixedRateTimer extends AnimationTimer {
        private static final long NANOS_PER_FRAME = 1_000_000_000 / 100;
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {
            if ((now - lastUpdate) >= NANOS_PER_FRAME) {
                lastUpdate = now;
                Controller.this.tick();
            }
        }
    }
}
