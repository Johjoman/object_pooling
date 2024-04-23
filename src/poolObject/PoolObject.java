package poolObject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import position.Position;

public class PoolObject {
    public Position position;

    private final ImageView image;
    public int stepSize;

    public int stepsToLive;
    public PoolObject() {
        position = new Position(900, 500);
        position.setDirection(90);
        stepSize = 10;
        image = new ImageView(new Image("poolObject/Fish.png"));
        image.setPreserveRatio(true);
        image.setFitWidth(50.0);
        image.setFitHeight(50.0);
        stepsToLive = 200;
    }

    public void step() {
        stepsToLive--;
        //position = position.calculateNewPositionWithAngle(stepSize);
        position = new Position(position.getX() - 10, position.getY());
        image.setLayoutX(position.getX());
        image.setLayoutY(position.getY());
    }

    public ImageView getImage() {
        return image;
    }
}
