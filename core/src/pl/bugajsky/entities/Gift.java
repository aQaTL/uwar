package pl.bugajsky.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import pl.bugajsky.GameUI;
import pl.bugajsky.Shot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by mariuszbugajski on 16.04.2017.
 */
public class Gift extends Rectangle {
    private int type;
    private Texture texture;
    private Sprite sprite;
    private float time;

    public Gift(int x, int y) {
        super(x, y, 32, 32);

        texture = new Texture("gift.png");
        sprite = new Sprite(texture);
        sprite.setX(x);
        sprite.setY(y);

        Random r = new Random();
        type = r.nextInt(7);

        time = 20;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public void performAnimation() {
        getSprite().rotate(-2);
        setX(sprite.getX());
        setY(sprite.getY());
    }

    public void getGift(Player player, LinkedList<Monster> monsters, LinkedList<Shot> shootsPlayer, LinkedList<Shot> shootsMonster, GameUI gameUI) {
        if (getType() == 0) {
            player.setHp(player.getHp() + 10);
            gameUI.setTurnPhaseInfo("Dodano 10pkt zycia");
        } else if (getType() == 1) {
            player.setHp(player.getHp() - 10);
            gameUI.setTurnPhaseInfo("Odjeto 10pkt zycia");
        } else if (getType() == 2) {
            player.setMoveVelocity(player.getMoveVelocity() + 50);
            gameUI.setTurnPhaseInfo("Zwiekszono szybkosc gracza");
        } else if (getType() == 3) {
            player.setMoveVelocity(player.getMoveVelocity() - 50);
            gameUI.setTurnPhaseInfo("Zmniejszono szybkosc gracza");
        } else if (getType() == 4) {
            for (Iterator<Shot> it = shootsMonster.iterator(); it.hasNext(); ) {
                Shot shot = it.next();
                it.remove();
                gameUI.setTurnPhaseInfo("Usunieto wszystkie strzaly wrogow");
            }
        } else if (getType() == 5) {
            for (Monster monster : monsters) {
                monster.setSpeed(monster.getSpeed() + 1);
                gameUI.setTurnPhaseInfo("Zwiekszono szybkosc wrogow");
            }
        } else if (getType() == 6) {
            for (Monster monster : monsters) {
                monster.setSpeed(monster.getSpeed() - 1);
                gameUI.setTurnPhaseInfo("Zmniejszono szybkosc wroga");
            }
        }
    }

    public void updateTime() {
        time -= Gdx.graphics.getDeltaTime();
    }
}
