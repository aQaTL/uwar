package pl.bugajsky;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by mariuszbugajski on 20.03.2017.
 */
public class Shot extends Rectangle {
    private int strength;
    private float velocity, diagonalVelocity;
    private Direction direction;

    private Texture texture;
    private Pixmap pixmap;

    public Shot(float x, float y, int strength, Direction direction) {
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.direction = direction;
        this.velocity = 800;
        this.diagonalVelocity = velocity - 300; //slightly slower
        pixmap = new Pixmap(10, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, 10, 20);
        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    public int getStrength() {
        return strength;
    }

    public void updatePosition() {
        switch (direction) {
            case WEST:
                this.x -= velocity * Gdx.graphics.getDeltaTime();
                break;
            case NORTH:
                this.y += velocity * Gdx.graphics.getDeltaTime();
                break;
            case EAST:
                this.x += velocity * Gdx.graphics.getDeltaTime();
                break;
            case SOUTH:
                this.y -= velocity * Gdx.graphics.getDeltaTime();
                break;
            case NORTH_EAST:
                this.x += diagonalVelocity * Gdx.graphics.getDeltaTime();
                this.y += diagonalVelocity * Gdx.graphics.getDeltaTime();
                break;
            case NORTH_WEST:
                this.x -= diagonalVelocity * Gdx.graphics.getDeltaTime();
                this.y += diagonalVelocity * Gdx.graphics.getDeltaTime();
                break;
            case SOUTH_EAST:
                this.x += diagonalVelocity * Gdx.graphics.getDeltaTime();
                this.y -= diagonalVelocity * Gdx.graphics.getDeltaTime();
                break;
            case SOUTH_WEST:
                this.x -= diagonalVelocity * Gdx.graphics.getDeltaTime();
                this.y -= diagonalVelocity * Gdx.graphics.getDeltaTime();
                break;
            default:
                System.err.printf("Invalid direction: %s\n", direction);
        }
    }
}
