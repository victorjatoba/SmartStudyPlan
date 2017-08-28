package br.com.smartstudyplan.preferences;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Esta interface é responsável por gerar um preference do usuário.
 */
@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface UserPreferences {

    /**
     * Este método é responsável por gerar o preferences para armazenar em qual a última tela
     * acessado pelo usuário.
     *
     * @return o passo em que se encontra o usuário
     */
    @DefaultInt(0)
    int step();

    /**
     * Este método é responsável por gerar o preferences para armazenar as disponibilidades.
     *
     * @return as disponibilidades em formato de <code>String</code> separadas por vírgula.
     */
    @DefaultString( "" )
    String availabilities();

    /**
     * Gera preferences para armazenar se há facilidade de aprendizado pela manhã.
     *
     * @return <code>true</code caso tenha facilidade de aprender de manhã, <code>false</code> caso
     *         contrário
     */
    @DefaultBoolean(false)
    boolean easeOfLearningMorning();

    /**
     * Gera preferences para armazenar se há facilidade de aprendizado pela tarde.
     *
     * @return <code>true</code> caso tenha facilidade de aprender de tarde, <code>false</code>
     *         caso contrário
     */
    @DefaultBoolean(false)
    boolean easeOfLearningAfternoon();

    /**
     * Gera preferences para armazenar se há facilidade de aprendizado pela noite.
     *
     * @return <code>true</code> caso tenha facilidade de aprender de noite, <code>false</code>
     *         caso contrário
     */
    @DefaultBoolean(false)
    boolean easeOfLearningNight();

}
