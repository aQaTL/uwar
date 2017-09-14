package pl.bugajsky;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import pl.bugajsky.entities.Base;
import pl.bugajsky.entities.Player;

/**
 * Created by mariuszbugajski on 12.03.2017.
 */
public class GameUI extends Actor {
    private BitmapFont uiFont;

    private String level;
    private String score;
    private String life;
    private String base;
    private String turnPhaseInfo;
    private String gift;

    public GameUI() {
        uiFont = new BitmapFont();
        uiFont.setColor(Color.WHITE);
        turnPhaseInfo = "";
        gift = "";
    }

    public void updateStats(Player player, Base base, Turn turn) {
        level = "Level: " + turn.getLevel();
        life = "HP: " + player.getHp();
        score = Integer.toString(player.getScore());
        this.base = "Base HP: " + base.getHp();
        turnPhaseInfo = turn.getPhase() == Turn.Phase.ATTACK ? "Atak" : "Przerwa od ataku";
        gift = player.getGiftType() != -1 && player.getGiftTime() > 0 ?
                String.format("%.2f", player.getGiftTime()) :
                "";
    }

    public void setTurnPhaseInfo(String turnPhaseInfo) {
        this.turnPhaseInfo = turnPhaseInfo;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        uiFont.draw(batch, level, 5, 475);
        uiFont.draw(batch, score, 300, 475);
        uiFont.draw(batch, life, 570, 475);
        uiFont.draw(batch, base, 570, 450);
        uiFont.draw(batch, turnPhaseInfo, 150, 20);
        uiFont.draw(batch, gift, 500, 425);
    }

}
