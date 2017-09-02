package pl.bugajsky.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import pl.bugajsky.Direction;

import java.util.Random;

import static pl.bugajsky.Direction.*;

/**
 * Created by mariuszbugajski on 02.03.2017.
 */

public class Monster extends Rectangle implements Drawable {

    private int hp;
    private int score;
    private double speed;
    private Texture texture;
    private Pixmap pixmap;
    private Direction moveDirection;
    private int moveQuantity;
    private Random r;
    private boolean boss;
    private float moveTime;
    private int step;

    private TextureAtlas atlas;
    private Sprite currSprite;

    public Monster(int x, int y, int lvl, TextureAtlas atlas) {
        super(x, y, 20, 20);

        this.atlas = atlas;

        r = new Random();
        hp = r.nextInt(lvl) + 1;
        speed = r.nextInt(lvl * 5) + 1;
        score = r.nextInt(lvl) + 1;
        boss = false;
        moveDirection = Direction.fromIndex(r.nextInt(4));
        moveQuantity = r.nextInt(5) + 1;
        pixmap = new Pixmap(30, 30, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fillRectangle(0, 0, 20, 20);
        texture = new Texture(pixmap);
        pixmap.dispose();
        moveTime = 0;
        step = 1;

        currSprite = atlas.createSprite(Integer.toString(getStep()));
    }

    public Monster(int x, int y, int hp, int speed, int score, TextureAtlas atlas) {
        super(x, y, 20, 20);

        this.atlas = atlas;

        r = new Random();
        this.hp = hp;
        this.speed = speed;
        this.score = score;
        boss = true;
        moveDirection = Direction.fromIndex(r.nextInt(4));
        moveQuantity = r.nextInt(5) + 1;
        pixmap = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fillRectangle(0, 0, 50, 50);
        texture = new Texture(pixmap);
        pixmap.dispose();
        moveTime = 0;
        step = 1;

        currSprite = atlas.createSprite(Integer.toString(getStep()));
    }

    public Texture getTexture() {
        return texture;
    }

    public int getHp() {
        return hp;
    }

    public double getSpeed() {
        return speed;
    }

    public Direction getMoveDirection() {
        return moveDirection;
    }

    public int getMoveQuantity() {
        return moveQuantity;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMoveDirection(Direction moveDirection) {
        this.moveDirection = moveDirection;
    }

    public void setMoveQuantity(int moveQuantity) {
        this.moveQuantity = moveQuantity;
    }

    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

    public void moveToLeft() {
        if (moveDirection == WEST) {
            if (x > 0)
                x -= speed;
        }
    }

    public void moveToRight() {
        if (moveDirection == EAST) {
            if (x < 5000)
                x += speed;
        }
    }

    public void moveToTop() {
        if (moveDirection == NORTH) {
            if (y < 5000)
                y += speed;
        }
    }

    public void moveToBottom() {
        if (moveDirection == SOUTH) {
            if (y > 0)
                y -= speed;
        }
    }

    //    generowanie nowych ruchów
    public void generateMove() {
        if (moveQuantity == 0) {
            moveDirection = Direction.fromIndex(r.nextInt(4));
            moveQuantity = r.nextInt(5) + 100;
            System.out.println(moveQuantity);
        }
    }

    //    generowanie nowych ruchów na bazie położenia bohatera
    public void generateMove(Player player) {
        if (moveQuantity == 0) {
            boolean direction = r.nextBoolean();
            moveDirection = generateDirectionTowardsPlayer(player);
            moveQuantity = r.nextInt(20) + 5;
        }
    }

    public Direction generateDirectionTowardsPlayer(Player player) {
        boolean direction = r.nextBoolean();
        if (!direction) {
            if (x > player.getPosition().x) {
                return WEST;
            } else {
                return EAST;
            }
        } else {
            if (y > player.getPosition().y) {
                return SOUTH;
            } else {
                return NORTH;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(float moveTime) {
        this.moveTime = moveTime;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void stepAnimation(float time) {
        setMoveTime(getMoveTime() + time);
        if (getMoveTime() > 0.15) {
            setStep(getStep() + 1);
            setMoveTime(0);

            if (getStep() > 2)
                setStep(0);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        currSprite = atlas.createSprite(Integer.toString(getStep()));
        currSprite.setPosition(x, y);
        currSprite.rotate(moveDirection.toAngle());
        currSprite.draw(batch);
    }

}
