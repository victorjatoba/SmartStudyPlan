package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.EaseOfLearning;
import br.com.smartstudyplan.bean.Step;
import br.com.smartstudyplan.manager.StudyPlanManager;
import br.com.smartstudyplan.preferences.UserPreferences_;

/**
 * Esta classe é a tela que mostra as opções de facilidade de aprendizado por período.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_ease_of_learning)
public class EaseOfLearningActivity extends AppCompatActivity {

    @ViewById Button easeOfLearningContinueButton;
    @ViewById CheckBox checkboxMorning;
    @ViewById CheckBox checkboxAfternoon;
    @ViewById CheckBox checkboxNight;

    @ViewById LinearLayout morningContainer;
    @ViewById LinearLayout afternoonContainer;
    @ViewById LinearLayout nightContainer;

    @Bean StudyPlanManager manager;

    @Pref UserPreferences_ preferences;

    private EaseOfLearning mEaseOfLearning;

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        setSupportActionBar(toolbar);

        manager.saveStep(Step.STEP_EASY_LEARNING );

        mEaseOfLearning = manager.getEaseOfLearning();

        checkboxMorning.setChecked( mEaseOfLearning.isEaseOfLearningMorning() );
        checkboxAfternoon.setChecked( mEaseOfLearning.isEaseOfLearningAfternoon() );
        checkboxNight.setChecked( mEaseOfLearning.isEaseOfLearningNight() );
    }

    /**
     * Chama a tela para criar o plano de estudo propriamente dito.
     */
    @Click
    void easeOfLearningContinueButton(){
            saveEaseLearning();

            CreatePlanActivity_.intent(this).start();
            finish();
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.screen_no_animation);
    }

    /**
     * Salva as preferências de facilidade de aprendizado do usuário.
     */
    private void saveEaseLearning(){
        mEaseOfLearning.setEaseOfLearningMorning(checkboxMorning.isChecked());
        mEaseOfLearning.setEaseOfLearningAfternoon(checkboxAfternoon.isChecked());
        mEaseOfLearning.setEaseOfLearningNight(checkboxNight.isChecked());

        manager.saveEaseOfLearning(mEaseOfLearning);
    }

    /**
     * Chama a tela de ajuda (dicas).
     */
    @Click
    void easeOfLearningHelpButton(){
        HelpActivity_.intent( EaseOfLearningActivity.this )
                .title(getString(R.string.help_ease_of_learning_title))
                .text(getString(R.string.help_ease_of_learning_text))
                .start();
    }

    /**
     * Retorna para a tela de editar disponibilidade ao clicar no botão voltar do Android.
     */
    @Override
    public void onBackPressed() {
        CustomAvailabilityActivity_.intent( EaseOfLearningActivity.this ).start();
        finish();
        overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right);
    }

    /**
     * Alterna a seleção da facilidade no período da manhã caso o usuário clique na área de manhã
     */
    @Click
    void morningContainerClicked(){
        checkboxMorning.setChecked( ! checkboxMorning.isChecked() );
    }

    /**
     * Alterna a seleção da facilidade no período da tarde caso o usuário clique na área de tarde
     */
    @Click
    void afternoonContainerClicked(){
        checkboxAfternoon.setChecked( ! checkboxAfternoon.isChecked() );
    }

    /**
     * Alterna a seleção da facilidade no período da noite caso o usuário clique na área de noite
     */
    @Click
    void nightContainerClicked(){
        checkboxNight.setChecked( ! checkboxNight.isChecked() );
    }

}
