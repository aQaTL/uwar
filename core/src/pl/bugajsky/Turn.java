package pl.bugajsky;

import com.badlogic.gdx.Gdx;


/**
 * Created by mariuszbugajski on 02.05.2017.
 */
public class Turn {
    private Phase phase;
    private int level;

    private float phaseTime;
    private int monstersLimit;
    private boolean bossAdded;

    public Turn() {
        phase = Phase.ATTACK;
        phaseTime = phase.getPhaseDuration();

        level = 1;
        monstersLimit = 10;
    }

    public void update() {
        if (!isBossLevel()) {
            updatePhaseTime();
        }
    }

    private void updatePhaseTime() {
        phaseTime -= Gdx.graphics.getDeltaTime();
        if (phaseTime < 0) {
            nextPhase();
        }
    }

    private void nextPhase() {
        phase = phase == Phase.ATTACK ? phase : Phase.REGENERATION;
        phase.increasePhaseDuration();
        phaseTime = phase.getPhaseDuration();

        level++;
        monstersLimit *= 1.5;
    }

    public void bossKilled() {
        nextPhase();
        setBossAdded(false);
    }

    public boolean isBossLevel() {
        return level % 5 == 0;
    }

    public boolean isBossAdded() {
        return bossAdded;
    }

    public void setBossAdded(boolean bossAdded) {
        this.bossAdded = bossAdded;
    }

    public int getMonstersLimit() {
        return monstersLimit;
    }

    public int getLevel() {
        return level;
    }

    public Phase getPhase() {
        return phase;
    }

    enum Phase {
        ATTACK(60, 5), REGENERATION(10, 2);

        private int phaseDuration;
        private final int durationIncrementValue;

        Phase(int phaseDuration, int durationIncrementValue) {
            this.phaseDuration = phaseDuration;
            this.durationIncrementValue = durationIncrementValue;
        }

        public int getPhaseDuration() {
            return phaseDuration;
        }

        public void increasePhaseDuration() {
            phaseDuration += durationIncrementValue;
        }
    }
}

