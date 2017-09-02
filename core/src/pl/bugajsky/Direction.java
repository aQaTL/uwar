package pl.bugajsky;

/**
 * @author Maciej on 2017-09-02.
 */
public enum Direction {
    NORTH(1), SOUTH(3), EAST(2), WEST(0),
    NORTH_EAST(4), NORTH_WEST(5),
    SOUTH_EAST(6), SOUTH_WEST(7);

    int num;

    Direction(int num) {
        this.num = num;
    }
}
