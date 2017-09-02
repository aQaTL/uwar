package pl.bugajsky.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import pl.bugajsky.Direction;

/**
 * Created by mariuszbugajski on 25.02.2017.
 */

public class Player extends Circle {

    private int hp;
    private float speed;
    private Direction direction;
    private int score;
    private float giftTime;
    private int runTime;
    private int giftType;
    private float speedRun;
    private float moveTime;
    private int step;

    private Vector2 position;
    private Vector2 newPosition;
    private Vector2 vector;
    private Vector2 velocity;
    private Vector2 movement;

    private Texture texture;
    private Pixmap pixmap;
    private Sprite sprite;

    public Player(float x, float y) {
        super(x, y, 10);
        hp = 1;
//        hp = 50;
        score = 0;
        speed = 250;
        speedRun = 250 + 50;
        direction = Direction.NORTH;
        giftTime = 1;
        runTime = 5;
        giftType = -1;
        position = new Vector2(x, y);
        newPosition = new Vector2();
        vector = new Vector2();
        velocity = new Vector2();
        movement = new Vector2();
        pixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fillCircle(10, 10, 10);
        texture = new Texture(pixmap);
        pixmap.dispose();
        moveTime = 0;
        step = 1;
        sprite = new Sprite();
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Vector2 newPosition) {
        this.newPosition = newPosition;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getMovement() {
        return movement;
    }

    public void setMovement(Vector2 movement) {
        this.movement = movement;
    }

    public float getGiftTime() {
        return giftTime;
    }

    public void setGiftTime(float giftTime) {
        this.giftTime = giftTime;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public float getSpeedRun() {
        return speedRun;
    }

    public void setSpeedRun(float speedRun) {
        this.speedRun = speedRun;
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

    public void goMove(Vector2 nowaPozycja, int direction, float dt) {
        vector.set(nowaPozycja).sub(position).nor();
        velocity.set(vector).scl(speed);
        movement.set(velocity).scl(dt);
        if (position.dst2(nowaPozycja) > movement.len2()) {
            position.add(movement);
        } else {
            position.set(nowaPozycja);
        }
        setX(position.x);
        setY(position.y);
    }

    public void runMove(Vector2 nowaPozycja, int direction, float dt) {
        vector.set(nowaPozycja).sub(position).nor();
        velocity.set(vector).scl(speedRun);
        movement.set(velocity).scl(dt);
        if (position.dst2(nowaPozycja) > movement.len2()) {
            position.add(movement);
        } else {
            position.set(nowaPozycja);
        }
        setX(position.x);
        setY(position.y);
    }

    public void goMoveToLeft(float dt) {
        newPosition.set(position.x - 100, position.y);
        goMove(newPosition, 0, dt);
        setDirection(Direction.WEST);
    }

    public void goMoveToRight(float dt) {
        newPosition.set(position.x + 100, position.y);
        goMove(newPosition, 2, dt);
        setDirection(Direction.EAST);
    }

    public void goMoveToTop(float dt) {
        newPosition.set(position.x, position.y + 100);
        goMove(newPosition, 1, dt);
        setDirection(Direction.NORTH);
    }

    public void goMoveToBottom(float dt) {
        newPosition.set(position.x, position.y - 100);
        goMove(newPosition, 3, dt);
        setDirection(Direction.SOUTH);
    }

    public void goMoveToTopRight(float dt) {
        newPosition.set(position.x - 100, position.y - 100);
        goMove(newPosition, 3, dt);
        setDirection(Direction.SOUTH);
    }

    public void runMoveToLeft(float dt) {
        newPosition.set(position.x - 100, position.y);
        runMove(newPosition, 0, dt);
    }

    public void runMoveToRight(float dt) {
        newPosition.set(position.x + 100, position.y);
        runMove(newPosition, 2, dt);
    }

    public void runMoveToTop(float dt) {
        newPosition.set(position.x, position.y + 100);
        runMove(newPosition, 1, dt);
    }

    public void runMoveToBottom(float dt) {
        newPosition.set(position.x, position.y - 100);
        runMove(newPosition, 3, dt);
    }

    public void draw(SpriteBatch batch, TextureAtlas region, float angle) {
        sprite.set(region.createSprite("" + getStep()));
        sprite.setPosition(getPosition().x, getPosition().y);
        sprite.rotate(angle);
        sprite.draw(batch);
    }

    public float getAngle() {
        switch (this.getDirection()) {
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
}
