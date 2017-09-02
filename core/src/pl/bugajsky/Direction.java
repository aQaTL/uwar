package pl.bugajsky;

/**
 * @author Maciej on 2017-09-02.
 */
public enum Direction {
    WEST(0), NORTH(1), EAST(2), SOUTH(3),
    NORTH_EAST(4), NORTH_WEST(5),
    SOUTH_EAST(6), SOUTH_WEST(7);

    int num;

    public static Direction fromIndex(int index) {
        return Direction.values()[index];
    }

    public float toAngle() {
        switch (this) {
            case WEST:
                return 180;
            case EAST:
                return 0;
            case NORTH:
                return 90;
            default:
                return 280;
        }
    }

    Direction(int num) {
        this.num = num;
    }
}
