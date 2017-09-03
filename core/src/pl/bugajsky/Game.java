package pl.bugajsky;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import pl.bugajsky.entities.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by mariuszbugajski on 07.04.2017.
 */
public class Game implements Screen {

    final UWar game;

    private SpriteBatch batch;
    private Texture gameArea;

    private Player player;
    private Base playerBase;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;

    private float timeHome;
    private float timeShoot;
    private float timerMonster;
    private float giftTimer;
    private Random r;

    private LinkedList<Shot> playerShots;
    private LinkedList<Shot> monstersShots;
    private LinkedList<Monster> monsters;
    private LinkedList<Gift> gifts;

    private Stage stage;
    private GameUI gameUI;
    private Turn turn;
    private Texture shotTexture;
    private TextureAtlas playerTextureAtlas;
    private TextureAtlas enemyTextureAtlas;

    public Game(final UWar game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());

        batch = new SpriteBatch();
        shotTexture = new Texture("shoot.png");

        playerTextureAtlas = new TextureAtlas(Gdx.files.internal("player.pack"));
        enemyTextureAtlas = new TextureAtlas(Gdx.files.internal("enemy.pack"));

        turn = new Turn(false, false, 60);

        playerShots = new LinkedList<>();
        monstersShots = new LinkedList<>();
        monsters = new LinkedList<>();
        gifts = new LinkedList<>();

        r = new Random();

        playerBase = new Base();

        camera = new OrthographicCamera(5000, 5000);
        camera.zoom = 0.2f;

        player = new Player(
                camera.viewportWidth / 2.0f,
                camera.viewportHeight / 2.0f,
                playerTextureAtlas);

        TiledMap map = new TmxMapLoader().load("map/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        gameUI = new GameUI();
        stage.addActor(gameUI);
        Gdx.input.setInputProcessor(stage);

        //game arena initialization
        Pixmap pixmap = new Pixmap(5000, 5000, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.drawRectangle(0, 0, 5000, 5000);
        gameArea = new Texture(pixmap);
        pixmap.dispose();

        timeHome = 0;

        timeShoot = 1;

        timerMonster = 1;

        giftTimer = 0;
    }


    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.begin();

        batch.draw(gameArea, 0, 0);

        playerBase.draw(batch);

        player.draw(batch);

        for (Shot s : playerShots) {
            batch.draw(shotTexture, s.x, s.y);
        }

        for (Shot s : monstersShots) {
            batch.draw(shotTexture, s.x, s.y);
        }

        drawEntities(monsters, batch);

        for (Gift gift : gifts) {
            gift.getSprite().draw(batch);
        }

        //Zoom value
        //font.draw(batch,camera.zoom + "",2500,2700);

        batch.end();
    }

    private void drawEntities(List<? extends Drawable> entities, SpriteBatch batch) {
        entities.forEach(o -> o.draw(batch));
    }

    private void update(float dt) {
        checkGameEndCondition();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        renderer.setView(camera);

        gameUI.updateStats(player, playerBase);

//		ustawienie kamery tak aby mapa była maksymalnie do krańców ekranu
//		ustawienie kamery z lewej strony i prawej strony
//		operacje dla środka ekranu
        if (player.x > 500 && player.x < 4500) {
            if (player.y > 500 && player.y < 4500) {
                camera.position.set(player.x, player.y, 0);
            } else {
                if (player.y < 500) {
                    camera.position.set(player.x, 500, 0);
                } else {
                    camera.position.set(player.x, 4500, 0);
                }
            }
        }

//		operacje dla krańców ekranu
        if (player.x < 500 || player.x > 4500) {
            if (player.y < 500 || player.y > 4500) {
                if (player.x < 500 && player.y < 500) {
                    camera.position.set(500, 500, 0);
//				}else if(player.x < 500 && player.y > 4500){
//					camera.position.set(500, 5000, 0);
                }
            } else {
                if (player.x < 500) {
                    camera.position.set(500, player.y, 0);
                } else {
                    camera.position.set(4500, player.y, 0);
                }
            }
        }

//        TURA
        if (player.getLevel() % 5 != 0) {
            turn.setTime(turn.getTime() - Gdx.graphics.getDeltaTime());

//        sprawdzenie czy nie skończył się czas ataku
            if (turn.getTime() < 0 && turn.isTyp() == false) {
                turn.setTyp(true);
                turn.updateAttackTimeout();
                turn.setTime(turn.getBreakTimeout());
                gameUI.setInfo("Przerwa od ataku");
//            System.out.println("Koniec czasu ataku");
            }

//        sprawdzenie czy nie skończył się czas odpoczynku
            if (turn.getTime() < 0 && turn.isTyp()) {
                int monstersLimit = (int) (turn.getMonstersLimit() * 1.5);
                turn.setMonstersLimit(monstersLimit);
                turn.setTyp(false);
                turn.changeTimeBreak();
                turn.setTime(turn.getAttackTimeout());
                player.setLevel(player.getLevel() + 1);
                gameUI.setInfo("Atak");
                if (player.getLevel() % 5 == 0) {
                    turn.setBoss(true);
                } else {
                    turn.setBoss(false);
                }
            }
//            System.out.println(playerStats.getLevel());
        } else {

            turn.setBossTime(turn.getBossTime() + Gdx.graphics.getDeltaTime());

            if (turn.getBossTime() > 5) {
                gameUI.setInfo("");
            }

            if (turn.isBoss() && !turn.isBossAdded()) {
                monsters.add(new Monster(
                        r.nextInt(5000),
                        r.nextInt(5000),
                        r.nextInt(25),
                        r.nextInt(20),
                        r.nextInt(100),
                        enemyTextureAtlas));
                turn.setBossAdded(true);
            }

            boolean isBoss = false;
            for (Monster m : monsters) {
                if (m.isBoss() == true) {
                    isBoss = true;
                }
            }

            if (isBoss == false) {
                boolean gift = r.nextBoolean();
                if (gift == false) {
                    player.setHp(player.getHp() + 5);
                } else {
                    playerBase.setHp(playerBase.getHp() + 5);
                }
                turn.setBossTime(0);
                turn.setBoss(false);
                turn.setBossAdded(false);
                turn.setTyp(false);
                turn.setTime(turn.getAttackTimeout());
                player.setLevel(player.getLevel() + 1);
            }
        }


//        Usunięcie info o "Przewa od ataku"
        if (!turn.isTyp() && turn.getTime() < 5) {
            gameUI.setInfo("");
        }

//        Usunięcie info o "Atak"
        if (turn.isTyp() && turn.getTime() < 2) {
            gameUI.setInfo("");
        }


//		dodawanie życia w bazie
        if (player.x > 2400 && player.x < 2600) {
            if (player.y > 2400 && player.y < 2600) {
                if (player.getHp() < 100) {
                    timeHome += Gdx.graphics.getDeltaTime();
                    if (timeHome > 1) {
                        player.setHp(player.getHp() + 1);
                        timeHome = 0;
                    }
                }
            } else {
                timeHome = 0;
            }
        } else {
            timeHome = 0;
        }

//		STRZAŁY
//		Dodawanie strzałów potorów
        for (Monster monster : monsters) {
            int l1 = r.nextInt(100);
            int l2 = r.nextInt(100);
            if (l1 == l2) {
                Direction shotDirection = monster.generateDirectionTowardsPlayer(player);
                monstersShots.add(new Shot(
                        monster.x + monster.getTexture().getWidth() / 2 - 5,
                        monster.y + monster.getTexture().getHeight() / 2 - 5,
                        1,
                        shotDirection));
            }
        }


//		dodanie strzałów
        timeShoot += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

//		Kierowanie strzałem
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                System.out.println("called");
                player.setDirection(Direction.NORTH);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player.setDirection(Direction.SOUTH);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.setDirection(Direction.EAST);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.setDirection(Direction.WEST);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.x < 5000 - 2 * player.radius && Gdx.input.isKeyPressed(Input.Keys.UP)) {
//				player.setDirection(4);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.x < 5000 - 2 * player.radius && Gdx.input.isKeyPressed(Input.Keys.UP)) {
//				player.setDirection(5);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.x < 5000 - 2 * player.radius && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//				player.setDirection(6);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.x < 5000 - 2 * player.radius && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//				player.setDirection(7);
            }

            if (timeShoot > 0.2) {
                playerShots.add(new Shot(
                        player.x + player.getWidth() / 2 - 5,
                        player.y + player.getHeight() / 2 - 5,
                        1,
                        player.getDirection()));
                timeShoot = 0;
            }
        }

        updateShoots(playerShots);
        updateShoots(monstersShots);

//		ograniczenie pola strzałów do mapy gry
        for (Iterator<Shot> it = playerShots.iterator(); it.hasNext(); ) {
            Shot shot = it.next();
            if (shot.y > 5000 || shot.y < 0 || shot.x > 5000 || shot.x < 0) {
                it.remove();
            }
        }

//		ograniczenie pola strzałów potworów do mapy gry
        for (Iterator<Shot> it = monstersShots.iterator(); it.hasNext(); ) {
            Shot shot = it.next();
            if (shot.y > 5000 || shot.y < 0 || shot.x > 5000 || shot.x < 0) {
                it.remove();
            }
        }

//		POTWORY
        timerMonster += Gdx.graphics.getDeltaTime();
        if (timerMonster > 1) {
            if (monsters.size() < turn.getMonstersLimit() && !turn.isTyp())
                monsters.add(new Monster(
                        r.nextInt(5000),
                        r.nextInt(5000),
                        player.getLevel(),
                        enemyTextureAtlas));
            timerMonster = 0;
        }

        for (Monster m : monsters) {
            if (m.getMoveQuantity() > 0) {
                m.moveToBottom();
                m.moveToLeft();
                m.moveToRight();
                m.moveToTop();
                m.setMoveQuantity(m.getMoveQuantity() - 1);
                m.stepAnimation(Gdx.graphics.getDeltaTime());
            } else {
                m.generateMove(player);
                m.moveToBottom();
                m.moveToLeft();
                m.moveToRight();
                m.moveToTop();
                m.setMoveQuantity(m.getMoveQuantity() - 1);
                m.stepAnimation(Gdx.graphics.getDeltaTime());
            }
        }

//		KOLIZJE
//		Strzał - potwór
        for (Iterator<Shot> it = playerShots.iterator(); it.hasNext(); ) {
            Shot shot = it.next();
            for (Iterator<Monster> pot = monsters.iterator(); pot.hasNext(); ) {
                Monster p = pot.next();
                if (shot.overlaps(p)) {
                    p.setHp(p.getHp() - shot.getStrength());
                    if (p.getHp() < 1) {
                        player.setScore(player.getScore() + p.getScore());
                        pot.remove();
                    }
                    it.remove();
                }
            }
        }

//		Potwór bohater/playerBase
        for (Iterator<Monster> pot = monsters.iterator(); pot.hasNext(); ) {
            Monster p = pot.next();
            Rectangle rec = new Rectangle(player.x - player.radius, player.y - player.radius, player.radius * 2, player.radius * 2);
            if (p.overlaps(rec) && p.isBoss() == false) {
                player.setHp(player.getHp() - 1);
                pot.remove();
            }

            if (p.overlaps(rec) && p.isBoss() == true) {
                player.setHp(player.getHp() - p.getHp());
                pot.remove();
            }

            if (p.overlaps(playerBase) && p.isBoss() == false) {
                playerBase.setHp(playerBase.getHp() - 1);
                pot.remove();
            }
        }

//		Strzał potwora - bohatera/bazy
        for (Iterator<Shot> it = monstersShots.iterator(); it.hasNext(); ) {
            Shot shot = it.next();
            Rectangle rec = new Rectangle(player.x - player.radius, player.y - player.radius, player.radius * 2, player.radius * 2);
            if (shot.overlaps(rec)) {
                it.remove();
                player.setHp(player.getHp() - 1);
            }
            if (shot.overlaps(playerBase)) {
                it.remove();
                playerBase.setHp(playerBase.getHp() - 1);
            }
        }

//		sterowanie
//        chodzenia
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) && player.y < 5000 - 2 * player.radius) {
            player.goMoveToTop(Gdx.graphics.getDeltaTime());
            player.stepAnimation(Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) && player.y > 0) {
            player.goMoveToBottom(Gdx.graphics.getDeltaTime());
            player.stepAnimation(Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.x > 0) {
            player.goMoveToLeft(Gdx.graphics.getDeltaTime());
            player.stepAnimation(Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.x < 5000 - 2 * player.radius) {
            player.goMoveToRight(Gdx.graphics.getDeltaTime());
            player.stepAnimation(Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W) && player.x < 5000 - 2 * player.radius) {
//            player.goMoveToTopRight(Gdx.graphics.getDeltaTime());
//            player.stepAnimation(Gdx.graphics.getDeltaTime());
        }

//        Bieganie
//        if(Gdx.input.isKeyPressed(Input.Keys.W) && player.y > 0 && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
//            player.runMoveToTop(Gdx.graphics.getDeltaTime());
//
//        }
//
//        if(Gdx.input.isKeyPressed(Input.Keys.S) && player.y > 0 && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
//            player.runMoveToBottom(Gdx.graphics.getDeltaTime());
//        }
//
//        if(Gdx.input.isKeyPressed(Input.Keys.A) && player.x > 0 && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
//            player.runMoveToLeft(Gdx.graphics.getDeltaTime());
//        }
//
//        if(Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
//            player.runMoveToRight(Gdx.graphics.getDeltaTime());
//        }
//
//
//        if(Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
//            player.x += 20;
//        }

        processInput();

//		Zoom-

        giftsAnimationUpdate();

//        Dodanie czasu od ostatniego prezentu
        giftTimer += Gdx.graphics.getDeltaTime();

//      Dodanie prezentu na mapę
        int g1 = r.nextInt(1000);
        int g2 = r.nextInt(1000);
        if (g1 == g2 || giftTimer > 100) {
            gifts.add(new Gift(r.nextInt(5000), r.nextInt(5000)));
            giftTimer = 0;
        }

//        sprawdzenie czasu obecnego prezentu
        if (player.getGiftTime() < 10 && player.getGiftType() != -1) {
            gameUI.setInfo("");
        }

//        Show bonus time
        if (player.getGiftType() != -1) {
            player.setGiftTime(player.getGiftTime() - Gdx.graphics.getDeltaTime());
            if (player.getGiftType() == 2 || player.getGiftType() == 3 || player.getGiftType() == 5 || player.getGiftType() == 6) {
                gameUI.setGift("Bonus: " + player.getGiftTime());
            }
        }

//        Sprawdzenie czasu obencego prezentu
        if (player.getGiftTime() < 0 && player.getGiftType() != -1) {
            if (player.getGiftType() == 2) {
                player.setMoveVelocity(player.getMoveVelocity() - 50);
                gameUI.setGift("");
            } else if (player.getGiftType() == 3) {
                player.setMoveVelocity(player.getMoveVelocity() + 50);
                gameUI.setGift("");
            } else if (player.getGiftType() == 5) {
                for (Monster monster : monsters) {
                    monster.setSpeed(monster.getSpeed() - 1);
                    gameUI.setGift("");
                }
            } else if (player.getGiftType() == 6) {
                for (Monster monster : monsters) {
                    monster.setSpeed(monster.getSpeed() + 1);
                    gameUI.setGift("");
                }
            }

            System.out.println("Usuń bonus");
            player.setGiftType(-1);
            player.setGiftTime(1);
        }

//      Kolizje Z PREZENTEM
//      Bohater - prezent
        for (Iterator<Gift> it = gifts.iterator(); it.hasNext(); ) {
            Gift gift = it.next();
            Rectangle rec = new Rectangle(player.x - player.radius, player.y - player.radius, player.radius * 2, player.radius * 2);
            if (gift.overlaps(rec)) {
                if (player.getGiftType() == -1) {
//                    if(gift.getType() == 2 || gift.getType() == 3 || gift.getType() == 5 || gift.getType() == 6){
                    player.setGiftTime(15);
                    player.setGiftType(gift.getType());
//                    }
//                    dfkngkdjs
                    it.remove();
                    gift.getGift(player, monsters, playerShots, monstersShots, gameUI);
                }
            }
        }

//        Kolizja potworek - prezent
        for (Iterator<Gift> giftIterator = gifts.iterator(); giftIterator.hasNext(); ) {
            Gift gift = giftIterator.next();
            for (Iterator<Monster> monsterIterator = monsters.iterator(); monsterIterator.hasNext(); ) {
                Monster monster = monsterIterator.next();
                if (gift.overlaps(monster)) {
                    giftIterator.remove();
                    monster.setHp(monster.getHp() - 1);
                }
            }
        }

        removeCollidingObjects(monstersShots, gifts);
        removeCollidingObjects(playerShots, gifts);

        updateGiftsTime();
    }

    private void checkGameEndCondition() {
        if (player.getHp() < 1 || playerBase.getHp() < 1) {
            game.setScreen(new End(game, player));
        }
    }

    private void processInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setDirection(Direction.NORTH);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setDirection(Direction.SOUTH);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setDirection(Direction.EAST);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setDirection(Direction.WEST);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.x < 5000 - 2 * player.radius && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setDirection(Direction.NORTH_EAST);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.x < 5000 - 2 * player.radius && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setDirection(Direction.NORTH_WEST);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.x < 5000 - 2 * player.radius && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setDirection(Direction.SOUTH_EAST);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.x < 5000 - 2 * player.radius && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setDirection(Direction.SOUTH_WEST);
        }

        //Zoom
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            if (camera.zoom >= 0 && camera.zoom <= 0.95) {
                camera.zoom += 0.01;
//				System.out.println("+: " + camera.zoom);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            if (camera.zoom >= 0.1 && camera.zoom <= 1) {
                camera.zoom -= 0.01;
//				System.out.println("-: " + camera.zoom);
            }
        }
    }

    private void giftsAnimationUpdate() {
        for (Gift g : gifts) {
            g.performAnimation();
        }
    }

    private void updateGiftsTime() {
        gifts.forEach(Gift::updateTime);
        gifts.removeIf(gift -> gift.getTime() <= 0);
    }

    private void removeCollidingObjects(List<? extends Rectangle> objectsA, List<? extends Rectangle> objectsB) {
        objectsA.removeIf(object -> objectsB.removeIf(objectB -> object.overlaps(objectB)));
    }

    private void updateShoots(List<Shot> shots) {
        shots.forEach(Shot::updatePosition);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameArea.dispose();
        stage.dispose();
        playerTextureAtlas.dispose();
        enemyTextureAtlas.dispose();
        gameArea.dispose();
    }
}
