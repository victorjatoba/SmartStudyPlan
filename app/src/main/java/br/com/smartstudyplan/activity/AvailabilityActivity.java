package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.adapter.AvailabilityAdapter;
import br.com.smartstudyplan.bean.Availability.Available;
import br.com.smartstudyplan.bean.DefaultAvailability;
import br.com.smartstudyplan.bean.Step;
import br.com.smartstudyplan.manager.StudyPlanManager;

/**
 * Esta tela é responsável por mostra a lista de disponibilidades padrões para que o usuário possa
 * selecionar e editar na próxima tela.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_availability )
public class AvailabilityActivity extends AppCompatActivity {
    
    private static final int NONE       = Available.NONE.getValue();
    private static final int FULL_TIME  = Available.FULL_TIME.getValue();

    private static final int LIST_SIZE = 21;

    @ViewById ListView  availabilityList;
    @ViewById ImageView availabilityHelpButton;

    @Bean AvailabilityAdapter adapter;

    @Bean StudyPlanManager manager;

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

        manager.saveStep(Step.STEP_AVAILABILITY );

        availabilityList.setAdapter(adapter);

        getDefaultAvailability();
    }

    /**
     * Prepara a lista de perfis de Disponibilidade Padrões.
     */
    @Background
    void getDefaultAvailability(){
        List<DefaultAvailability> defaultAvailabilities = new ArrayList<>();

        DefaultAvailability availability = new DefaultAvailability();
        availability.setName(getString(R.string.profile_worker));
        availability.setDescription(getString(R.string.profile_worker_description));

        List<Integer> avList = new ArrayList<>(LIST_SIZE);
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //dom
        avList.add( NONE ); avList.add( NONE ); avList.add( FULL_TIME ); //seg
        avList.add( NONE ); avList.add( NONE ); avList.add( FULL_TIME ); //ter
        avList.add( NONE ); avList.add( NONE ); avList.add( FULL_TIME ); //qua
        avList.add( NONE ); avList.add( NONE ); avList.add( FULL_TIME ); //qui
        avList.add( NONE ); avList.add( NONE ); avList.add( FULL_TIME ); //sex
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //sab

        availability.setAllValues( avList );

        defaultAvailabilities.add( availability );

        availability = new DefaultAvailability();
        availability.setName(getString(R.string.profile_worker_student));
        availability.setDescription(getString(R.string.profile_worker_student_description));

        avList = new ArrayList<>(LIST_SIZE);
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //dom
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //seg
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //ter
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //qua
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //qui
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //sex
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //sab

        availability.setAllValues( avList );

        defaultAvailabilities.add( availability );

        availability = new DefaultAvailability();
        availability.setName(getString(R.string.profile_student_morning));
        availability.setDescription(getString(R.string.profile_student_morning_description));

        avList = new ArrayList<>(LIST_SIZE);
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //dom
        avList.add( NONE ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //seg
        avList.add( NONE ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //ter
        avList.add( NONE ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //qua
        avList.add( NONE ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //qui
        avList.add( NONE ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //sex
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //sab

        availability.setAllValues( avList );

        defaultAvailabilities.add( availability );

        availability = new DefaultAvailability();
        availability.setName(getString(R.string.profile_student_afternoon));
        availability.setDescription(getString(R.string.profile_student_afternoon_description));

        avList = new ArrayList<>(LIST_SIZE);
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //dom
        avList.add( FULL_TIME ); avList.add( NONE ); avList.add( FULL_TIME ); //seg
        avList.add( FULL_TIME ); avList.add( NONE ); avList.add( FULL_TIME ); //ter
        avList.add( FULL_TIME ); avList.add( NONE ); avList.add( FULL_TIME ); //qua
        avList.add( FULL_TIME ); avList.add( NONE ); avList.add( FULL_TIME ); //qui
        avList.add( FULL_TIME ); avList.add( NONE ); avList.add( FULL_TIME ); //sex
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //sab

        availability.setAllValues( avList );

        defaultAvailabilities.add( availability );

        availability = new DefaultAvailability();
        availability.setName(getString(R.string.profile_student_night));
        availability.setDescription(getString(R.string.profile_student_night_description));

        avList = new ArrayList<>(LIST_SIZE);
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //dom
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( NONE ); //seg
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( NONE ); //ter
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( NONE ); //qua
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( NONE ); //qui
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( NONE ); //sex
        avList.add( NONE ); avList.add( NONE ); avList.add( NONE ); //sab

        availability.setAllValues( avList );

        defaultAvailabilities.add( availability );


        availability = new DefaultAvailability();
        availability.setName(getString(R.string.profile_customize));
        availability.setDescription(getString(R.string.profile_customize_description));

        avList = new ArrayList<>(LIST_SIZE);
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //dom
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //seg
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //ter
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //qua
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //qui
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //sex
        avList.add( FULL_TIME ); avList.add( FULL_TIME ); avList.add( FULL_TIME ); //sab

        availability.setAllValues( avList );

        defaultAvailabilities.add( availability );

        showList( defaultAvailabilities );
    }

    /**
     * Faz a lista de disponibilidades padrões aparecer na tela.
     *
     * @param list a lista de disponibilidades a ser mostrada na tela.
     */
    @UiThread
    void showList( List<DefaultAvailability> list ){
        adapter.setContent( list );
    }

    /**
     * Abre a próxima tela do aplicativo com os detalhes de disponibilidade do perfil selecionado.
     *
     * @param availability a disponibilidade do perfil
     */
    @ItemClick
    void availabilityListItemClicked( DefaultAvailability availability ){
        if( ( availability.getName().equals(getString(R.string.profile_customize)) == false ) ||
            ( manager.getAvailabilities() == null ) ){
            manager.saveAvailabilities( availability );
        }

        CustomAvailabilityActivity_.intent(AvailabilityActivity.this).start();
        finish();
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Chama a tela de ajuda (dicas).
     */
    @Click
    void availabilityHelpButton(){
        HelpActivity_.intent(AvailabilityActivity.this )
                .title(getString(R.string.help_availability_title))
                .text(getString(R.string.help_availability_text))
                .start();
    }

    /**
     * Retorna para tela de matérias ao clicar no botão voltar do Android.
     */
    @Override
    public void onBackPressed() {
        SubjectActivity_.intent( AvailabilityActivity.this ).start();
        finish();
        overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
