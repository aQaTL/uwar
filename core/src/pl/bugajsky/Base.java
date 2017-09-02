package pl.bugajsky;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by mariuszbugajski on 07.04.2017.
 */
public class Base extends Rectangle{

    private int level;
    private int hp;
    private Pixmap pixmap;
    private Texture texture;

    public Base(){
        super(2400,2400, 200,200);
        level = 1;
        hp = 50;
        texture = new Texture("home.png");
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
