package br.com.smartstudyplan.bean;

/**
 * Esta classe encapsula as informações referentes a facilidade de aprendizado.
 */
public class EaseOfLearning {
    private boolean easeOfLearningMorning, easeOfLearningAfternoon, easeOfLearningNight;

    public EaseOfLearning() { }

    public EaseOfLearning(boolean easeOfLearningMorning, boolean easeOfLearningAfternoon, boolean easeOfLearningNight) {
        this.setEaseOfLearningMorning(easeOfLearningMorning);
        this.setEaseOfLearningAfternoon(easeOfLearningAfternoon);
        this.setEaseOfLearningNight(easeOfLearningNight);
    }

    public boolean isEaseOfLearningMorning() {
        return easeOfLearningMorning;
    }

    public void setEaseOfLearningMorning(boolean easeOfLearningMorning) {
        this.easeOfLearningMorning = easeOfLearningMorning;
    }

    public boolean isEaseOfLearningAfternoon() {
        return easeOfLearningAfternoon;
    }

    public void setEaseOfLearningAfternoon(boolean easeOfLearningAfternoon) {
        this.easeOfLearningAfternoon = easeOfLearningAfternoon;
    }

    public boolean isEaseOfLearningNight() {
        return easeOfLearningNight;
    }

    public void setEaseOfLearningNight(boolean easeOfLearningNight) {
        this.easeOfLearningNight = easeOfLearningNight;
    }
}
