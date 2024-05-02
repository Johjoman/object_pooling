package poolObject;

import Logger.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import position.Position;

public class PoolObject implements PoolObjectInterface{
    public Position position;
    private final ImageView image;
    public int stepSize;

    public boolean isDead;
    public PoolObject() {
        Logger.getInstance().log("Pool Object Initialization", Logger.LogLevel.INFO);
        image = new ImageView(new Image("poolObject/Fish.png"));
        simulateDelay();
        initialize();
    }

    public void step() {
        position = new Position(position.getX() - stepSize, position.getY());
        if (position.getX() <= 50) {
            isDead = true;
        }
        image.setLayoutX(position.getX());
        image.setLayoutY(position.getY());
    }

    public ImageView getImage() {
        return image;
    }

    @Override
    public void reset() {
        initialize();
    }

    private void simulateDelay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.getInstance().log("Interrupted during simulated delay: " + e.getMessage(), Logger.LogLevel.CRITICAL);
        }
    }

    private void initialize() {
        isDead = false;
        position = new Position(900, 300);
        position.setDirection(90);
        stepSize = 6;
        image.setScaleX(-1);
        image.setPreserveRatio(true);
        image.setFitWidth(200.0);
        image.setFitHeight(200.0);
    }
}
