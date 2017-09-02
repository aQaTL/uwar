package pl.bugajsky;

/**
 * Created by mariuszbugajski on 02.05.2017.
 */
public class Turn {
    private boolean typ;
    private boolean boss;
    private float time;
    private float bossTime;
    private float attackTimeout;
    private float breakTimeout;
    private int monstersLimit;
    private boolean bossAdded;

    public Turn(boolean typ, boolean boss, float time) {
        this.typ = typ;
        this.boss = boss;
        this.time = time;
        bossAdded = false;
        monstersLimit = 10;
        bossTime = 0;
        attackTimeout = 60;
        breakTimeout = 10;
    }

    public boolean isTyp() {
        return typ;
    }

    public void setTyp(boolean typ) {
        this.typ = typ;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getMonstersLimit() {
        return monstersLimit;
    }

    public void setMonstersLimit(int monstersLimit) {
        this.monstersLimit = monstersLimit;
    }

    public boolean isBossAdded() {
        return bossAdded;
    }

    public void setBossAdded(boolean bossAdded) {
        this.bossAdded = bossAdded;
    }

    public float getBossTime() {
        return bossTime;
    }

    public void setBossTime(float bossTime) {
        this.bossTime = bossTime;
    }

    public float getAttackTimeout() {
        return attackTimeout;
    }

    public void setAttackTimeout(float attackTimeout) {
        this.attackTimeout = attackTimeout;
    }

    public float getBreakTimeout() {
        return breakTimeout;
    }

    public void setBreakTimeout(float breakTimeout) {
        this.breakTimeout = breakTimeout;
    }

    public void updateAttackTimeout() {
        attackTimeout += 5;
    }

    public void changeTimeBreak() {
        breakTimeout += 2;
    }
}
