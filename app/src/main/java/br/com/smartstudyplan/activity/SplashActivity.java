package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.Step;
import br.com.smartstudyplan.manager.StudyPlanManager;
import br.com.smartstudyplan.preferences.UserPreferences_;

/**
 * Esta classe é a primeira tela chamada quando o aplicativo inicia e é responsável por mostrar a
 * marca do aplicativo. Ela é mostrada apenas por alguns segundos (@see SPLASH_SHOW_DURATION).
 * Também é responsável por direcionar para a próxima tela corretamente, seguindo as regras:
 *
 * 1. Caso seja o primeiro acesso, vai direcionar para a tela de lista de matérias;
 *
 * 2. Caso o usuário já tenha acessado o aplicativo anteriormente, mas não tenha gerado o plano de
 * estudo, vai direcionar para a última tela acessada;
 *
 * 3. Caso o usuário já tenha gerado o plano de estudo, vai direcionar para a tela de
 * calendário do plano de estudo;
 */
/**
 *
 */
@SuppressLint("Registered")
@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {

    /**
     * Define o tempo em que a tela ficará aberta.
     */
    private final int SPLASH_SHOW_DURATION = 2500;

    @Bean StudyPlanManager mManager;

    @Pref UserPreferences_ preferences;

    @ViewById ImageView logo;

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterInject
    void init(){
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                redirectToCorrectlyActivity();
            }
        }, SPLASH_SHOW_DURATION);
    }

    /**
     * Direciona para a tela correta.
     */
    void redirectToCorrectlyActivity(){
        switch (preferences.step().get()){
            case Step.STEP_INIT:
                PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext())
                        .edit().putBoolean("notification", true)
                        .apply();
            case Step.STEP_SUBJECT:
                SubjectActivity_.intent(SplashActivity.this).start();
                break;
            case Step.STEP_AVAILABILITY:
                AvailabilityActivity_.intent(SplashActivity.this).start();
                break;
            case Step.STEP_CUSTOM_AVAILABILITY:
                CustomAvailabilityActivity_.intent(SplashActivity.this).start();
                break;
            case Step.STEP_EASY_LEARNING:
                EaseOfLearningActivity_.intent(SplashActivity.this).start();
                break;
            case Step.STEP_GENERATE_COMPLETED:
                CalendarSubjectActivity_.intent(SplashActivity.this).start();
                break;
            default:
                SubjectActivity_.intent(SplashActivity.this).start();
                break;
        }

        this.finish();
    }
}
