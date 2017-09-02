package pl.bugajsky;

/**
 * Created by mariuszbugajski on 02.05.2017.
 */
public class PlayerStats {
    private int level;
    private int score;
    private int enemies;

    public PlayerStats() {
        level = 1;
        score = 0;
        enemies = 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getEnemies() {
        return enemies;
    }

    public void setEnemies(int enemies) {
        this.enemies = enemies;
    }
    
}
