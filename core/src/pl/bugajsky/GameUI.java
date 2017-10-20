package pl.bugajsky;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

    private Texture statTexture;
    private Texture infoTexture;

    public GameUI() {
        uiFont = new BitmapFont();
        uiFont.setColor(Color.WHITE);

        turnPhaseInfo = "";
        gift = "";

        statTexture = new Texture("ui/panel.png");
        infoTexture = new Texture("ui/info.png");
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
        batch.draw(
                statTexture,
                Gdx.graphics.getWidth() * 0.92f - statTexture.getWidth() / 2,
                Gdx.graphics.getHeight() * 0.89f - statTexture.getHeight() / 2);

        batch.draw(
                statTexture,
                Gdx.graphics.getWidth() * 0.08f - statTexture.getWidth() / 2,
                Gdx.graphics.getHeight() * 0.89f - statTexture.getHeight() / 2);

        uiFont.draw(batch, level, 5, 475);
        uiFont.draw(batch, score, 300, 475);
        uiFont.draw(batch, life, 570, 475);
        uiFont.draw(batch, base, 570, 450);
        uiFont.draw(batch, turnPhaseInfo, 150, 20);
        uiFont.draw(batch, gift, 500, 425);

        uiFont.draw(batch, level,
                Gdx.graphics.getWidth() * 0.03f - uiFont.getSpaceWidth() / 2,
                Gdx.graphics.getHeight() * 0.98f - uiFont.getLineHeight() / 2);

        uiFont.draw(batch, score,
                Gdx.graphics.getWidth() * 0.035f - uiFont.getSpaceWidth() / 2,
                Gdx.graphics.getHeight() * 0.93f - uiFont.getLineHeight() / 2);

        uiFont.draw(batch, life,
                Gdx.graphics.getWidth() * 0.89f - uiFont.getSpaceWidth() / 2,
                Gdx.graphics.getHeight() * 0.98f - uiFont.getLineHeight() / 2);

        uiFont.draw(batch, base,
                Gdx.graphics.getWidth() * 0.88f - uiFont.getSpaceWidth() / 2,
                Gdx.graphics.getHeight() * 0.93f - uiFont.getLineHeight() / 2);

        uiFont.draw(batch, turnPhaseInfo,
                Gdx.graphics.getWidth() * 0.3f - uiFont.getSpaceWidth(),
                Gdx.graphics.getHeight() * 0.08f - uiFont.getLineHeight() / 2);

        uiFont.draw(batch, gift,
                Gdx.graphics.getWidth() * 0.88f - uiFont.getSpaceWidth() / 2,
                Gdx.graphics.getHeight() * 0.90f - uiFont.getLineHeight() / 2);

        if (!turnPhaseInfo.equals("")) {
            batch.draw(infoTexture, Gdx.graphics.getWidth() / 2 - infoTexture.getWidth() / 2, Gdx.graphics.getHeight() * 0.05f - infoTexture.getHeight() / 2);
        }
    }

}
