package poolObject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import position.Position;

public class PoolObject {
    public Position position;

    private ImageView image;
    public int stepSize;

    public PoolObject() {
        position = new Position(900, 500);
        position.setDirection(180);
        stepSize = 10;
        image = new ImageView(new Image("file:Fish.png"));
        image.setPreserveRatio(true);
        image.setFitWidth(50.0);
        image.setFitHeight(50.0);
    }

    public void step() {
        position = position.calculateNewPositionWithAngle(stepSize);
        image.setLayoutX(position.getX());
        image.setLayoutY(position.getY());
    }

    public ImageView getImage() {
        return image;
    }
}
