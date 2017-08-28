package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.bean.Availability.Available;
import br.com.smartstudyplan.bean.Availability.Period;
import br.com.smartstudyplan.bean.Availability.Weekday;
import br.com.smartstudyplan.bean.Step;
import br.com.smartstudyplan.dialog.AvailabilityDialog;
import br.com.smartstudyplan.dialog.AvailabilityDialogListener;
import br.com.smartstudyplan.manager.StudyPlanManager;
import br.com.smartstudyplan.util.SLog;

/**
 * Esta tela mostra para o usuário as horas disponíveis para o usuário estudar, em um calendario
 * semanal. O usuário poderá alterar essa carga horário clicando no dia/período, e escolhendo entre
 * indisponível, pouco disponível e disponível.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_custom_availability )
public class CustomAvailabilityActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CustomAvailabilityActivity";
    /**
     * Quantidade mínima de disponibilidade necessária para gerar um plano de estudo.
     */
    private static final int MIN_AVAILABILITY_VALUE = 2;

    @ViewById ImageView sundayMorning;
    @ViewById ImageView sundayAfternoon;
    @ViewById ImageView sundayNight;

    @ViewById ImageView mondayMorning;
    @ViewById ImageView mondayAfternoon;
    @ViewById ImageView mondayNight;

    @ViewById ImageView tuesdayMorning;
    @ViewById ImageView tuesdayAfternoon;
    @ViewById ImageView tuesdayNight;

    @ViewById ImageView wednesdayMorning;
    @ViewById ImageView wednesdayAfternoon;
    @ViewById ImageView wednesdayNight;

    @ViewById ImageView thursdayMorning;
    @ViewById ImageView thursdayAfternoon;
    @ViewById ImageView thursdayNight;

    @ViewById ImageView fridayMorning;
    @ViewById ImageView fridayAfternoon;
    @ViewById ImageView fridayNight;

    @ViewById ImageView saturdayMorning;
    @ViewById ImageView saturdayAfternoon;
    @ViewById ImageView saturdayNight;

    @ViewById Button customAvailabilityContinueButton;

    @Bean StudyPlanManager manager;

    private int[] drawables;

    private Weekday   mSelectedWeekday;
    private Period    mSelectedPeriod;
    private Available mSelectedAvailable;

    private Availability mAvailability;

    private ImageView[] mViews;

    /**
     * Primeiro método a ser chamado na Activity. Carrega as cores padrão, buscando a
     * disponibilidade equivalente ao perfil selecionado pelo usuário na tela anterior.
     */
    @AfterViews
    void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        setSupportActionBar(toolbar);

        manager.saveStep(Step.STEP_CUSTOM_AVAILABILITY );

        mViews = new ImageView[]{
                sundayMorning,    sundayAfternoon,    sundayNight,
                mondayMorning,    mondayAfternoon,    mondayNight,
                tuesdayMorning,   tuesdayAfternoon,   tuesdayNight,
                wednesdayMorning, wednesdayAfternoon, wednesdayNight,
                thursdayMorning,  thursdayAfternoon,  thursdayNight,
                fridayMorning,    fridayAfternoon,    fridayNight,
                saturdayMorning,  saturdayAfternoon,  saturdayNight
        };

        drawables = new int[]{
                R.drawable.circle_difficult_5,
                R.drawable.circle_difficult_3,
                R.drawable.circle_difficult_1
        };

        Availability availability = manager.getAvailabilities();

        if( availability != null ){
            mAvailability = availability;

            SLog.d(TAG, availability.toString());

            updateLayout();
        }
        else{
            finish();
        }
    }

    /**
     * Preenche a tela a partir da disponibilidade selecionado pelo
     * usuário, e também atualiza a tela caso o usuário altere a disponibilidade.
     */
    @UiThread
    void updateLayout(){
        int i = 0;
        ImageView view;
        for( Weekday weekday : Weekday.values() ){
            for( Period period : Period.values() ){
                view = mViews[i];
                Available available = mAvailability.getAvailable(weekday, period);
                view.setImageResource( drawables[ available.getValue() ] );

                view.setTag( R.id.weekday_id, weekday );
                view.setTag( R.id.period_id, period );
                view.setTag( R.id.available_id, available );

                view.setOnClickListener( this );

                i++;
            }
        }
    }

    /**
     * Este método é chamado quando o usuário clica em um período do dia. Exibe o
     * dialog para seleção de disponibilidade.
     *
     * @param v a <code>View</code> referente ao período que foi clicado.
     */
    @Override
    public void onClick(View v) {
        mSelectedWeekday    = (Weekday)   v.getTag( R.id.weekday_id );
        mSelectedPeriod     = (Period)    v.getTag( R.id.period_id );
        mSelectedAvailable  = (Available) v.getTag( R.id.available_id );

        if( mSelectedWeekday != null ){
            AvailabilityDialog.show( CustomAvailabilityActivity.this,
                    mSelectedWeekday,
                    mSelectedPeriod,
                    mSelectedAvailable,
                    new AvailabilityDialogListener() {
                        @Override
                        public void returnValues(Available available) {
                            mSelectedAvailable = available;
                            mAvailability.setValue(mSelectedWeekday, mSelectedPeriod, mSelectedAvailable);
                            saveAvailability();
                            updateLayout();
                        }
                    });
        }
    }

    /**
     * Salva as disponibilidades após alterações realizadas pelo usuário.
     */
    @Background
    void saveAvailability(){
        manager.saveAvailabilities( mAvailability );
    }

    /**
     * Abre a próxima tela do aplicativo com as facilidades de aprendizado por período.
     */
    @Click
    void customAvailabilityContinueButton(){
        int sumAvaiabilityPeriod = 0;
        for( Integer value : mAvailability.getValuesList() ){
            sumAvaiabilityPeriod += value.intValue();
        }

        if( sumAvaiabilityPeriod < MIN_AVAILABILITY_VALUE ){
            Toast.makeText( getApplicationContext(),
                    getString(R.string.min_availability),
                    Toast.LENGTH_LONG ).show();

            return ;
        }

        EaseOfLearningActivity_.intent( this ).start();
        finish();
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Chama a tela de ajuda (dicas).
     */
    @Click
    void customAvailabilityHelpButton(){
        HelpActivity_.intent(CustomAvailabilityActivity.this )
                .title(getString(R.string.help_custom_availability_title))
                .text(getHelpContent())
                .start();
    }

    /**
     * Gera o texto que será exibido na tela de ajuda.
     *
     * @return Texto que será exibido na tela de ajuda.
     */
    private String getHelpContent(){
        SLog.d(TAG, "Colors: " + getResources().getColor(R.color.difficult_5) + " | " + getResources().getColor(R.color.difficult_3)
                + " | " + getResources().getColor(R.color.difficult_1));
        return getString(R.string.help_custom_availability_text_intro)
                + Html.fromHtml("<b><font color='#C1272D'>"
                    + getString(R.string.help_custom_availability_text_red)
                    + "</font></b>")
                + getString(R.string.help_custom_availability_text_unavailable)
                + Html.fromHtml("<b><font color='#FFB741'>"
                    + getString(R.string.help_custom_availability_text_yellow)
                    + "</font></b>")
                + getString(R.string.help_custom_availability_text_part_time)
                + Html.fromHtml("<b><font color='#006633'>"
                    + getString(R.string.help_custom_availability_text_green)
                    + "</font></b>")
                + getString(R.string.help_custom_availability_text_available);
    }

    /**
     * Retorna para a tela anterior ao clicar no botão voltar do Android.
     */
    @Override
    public void onBackPressed() {
        AvailabilityActivity_.intent( CustomAvailabilityActivity.this ).start();
        finish();
        overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
