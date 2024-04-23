package position;

public class Position {
    private int x,y;

    private int direction;

    public final static int COLLISION_RADIUS = 1;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this(0, 0);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCoordinate(int x, int y){
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Position calculateNewPositionWithStep(Position targetPosition, int stepSize) {
        double dx = targetPosition.x - x;
        double dy = targetPosition.y - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance <= stepSize){
            return targetPosition;
        }
        double normX = dx / distance;
        double normY = dy / distance;

        return new Position((int)(x + normX * stepSize), (int)(y + normY * stepSize));
    }

    public Position calculateNewPositionWithAngle(int stepSize) {
        double newX = stepSize * Math.cos(Math.toRadians(direction));
        double newY = stepSize * Math.sin(Math.toRadians(direction));
        return new Position((int)(x + newX), (int)(y + newY));
    }

    public boolean isInBounds(Position topLeftBounds, Position bottomRightBounds) {
        return x > topLeftBounds.x && x < bottomRightBounds.x && y < bottomRightBounds.y && y > topLeftBounds.y;
    }
}
