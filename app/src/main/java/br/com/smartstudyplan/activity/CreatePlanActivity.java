package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.Step;
import br.com.smartstudyplan.bean.StudyPlan;
import br.com.smartstudyplan.manager.ASPGAManager;
import br.com.smartstudyplan.manager.StudyPlanManager;
import br.com.smartstudyplan.notification.BootReceiver;
import br.com.smartstudyplan.util.SLog;
import br.com.smartstudyplan.view.CreatePlanTextItemView;
import br.com.smartstudyplan.view.CreatePlanTextItemView_;
import ec.app.aspga.bean.AspgaContext;
import ec.app.aspga.bean.Student;

/**
 * Esta tela é responsável por gerar o plano de estudo.
 */
@SuppressLint("Registered")
@EActivity(R.layout.activity_create_plan)
public class CreatePlanActivity extends AppCompatActivity {
    private static final String TAG = "CreatePlanActivity";

    /**
     * Determina um id único para a thread responsável por coletar as informações fornecidas pelo
     * usuário e gerar o plano de estudo. O plano só pode ser gerado após coletar todas as
     * informações.
     */
    private static final String SERIAL_GET_VALUES = "serial";

    private static final int DELAY            = 8500;
    private static final int MAX_RANDOM_VALUE = 2500;

    private static final int SUBJECT_TEXT_POSITION       = 0;
    private static final int AVAILABILITY_TEXT_POSITION  = 1;
    private static final int EASE_OF_LEARN_TEXT_POSITION = 2;
    private static final int CREATE_PLAN_POSITION        = 3;
    private static final int PLAN_CREATED_POSITION       = 4;

    private int mActualPosition = SUBJECT_TEXT_POSITION;

    @Bean StudyPlanManager studyPlanManager;

    private AspgaContext mAspgaContext;

    @ViewById LinearLayout layoutContainer;
    @ViewById Button       continueBtn;
    @ViewById LinearLayout finishedLayout;

    private Random mRandom;

    private CreatePlanTextItemView mCreatePlanView;

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

        updateFinishLayout( false );

        initAspgaContext();

        mRandom = new Random();

        layoutContainer.removeAllViews();
        mActualPosition = SUBJECT_TEXT_POSITION;

        showNextMessage( mActualPosition );

        getSubjects();
    }

    /**
     * Inicia o ASPGA Context adicionando um estudande padrão.
     */
    private void initAspgaContext(){
        mAspgaContext = AspgaContext.getInstance();

        Student student = new Student();
        student.setName("Default");
        student.setHoursToLeisure(0);

        mAspgaContext.setStudent(student);
    }

    /**
     * Controla a exibição dos textos na ordem correta.
     *
     * @param value a ordem do texto a ser exibido
     */
    private void showNextMessage(int value){
        int initialTime    = DELAY + mRandom.nextInt(MAX_RANDOM_VALUE);

        switch( value ){
            case SUBJECT_TEXT_POSITION:
                View view = createTextView(R.string.verify_subjects);
                view.setVisibility(View.VISIBLE);
                ((CreatePlanTextItemView)view).start(initialTime);

                mActualPosition += 1;
                showNextMessage(mActualPosition);
                break;
            case AVAILABILITY_TEXT_POSITION:
                updateView(createTextView(R.string.verify_availability), initialTime, true);
                break;
            case EASE_OF_LEARN_TEXT_POSITION:
                updateView(createTextView(R.string.verify_ease_of_learn), initialTime, true);
                break;
            case CREATE_PLAN_POSITION:
                mCreatePlanView = (CreatePlanTextItemView) createTextView(R.string.create_plan);
                updateView(mCreatePlanView, initialTime, false);
                break;
            case PLAN_CREATED_POSITION:
                break;
        }
    }

    /**
     * Cria um elemento de texto que será exibido na tela.
     *
     * @param textResource o texto do elemento
     * @return o elemento <code>View</code> criado
     */
    private View createTextView( int textResource ){
        CreatePlanTextItemView view = CreatePlanTextItemView_.build(CreatePlanActivity.this);
        view.bindView(textResource);
        view.setVisibility(View.GONE);

        layoutContainer.addView( view );
        return view;
    }

    /**
     * Atualiza a tela mostrando para o usuário o andamento do processo de geração do plano de
     * estudo.
     *
     * @param view A <code>View</code> que irá ser atualizada
     * @param time O tempo até a exibição da próximo passo do processo.
     * @param isUpdateView Define se a view deve ser atualizada.
     */
    private void updateView( final View view, final int time, final boolean isUpdateView ){
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if( isUpdateView && ( view instanceof CreatePlanTextItemView ) ){
                    ((CreatePlanTextItemView)view).start(time);
                }
                view.setVisibility(View.VISIBLE);
                mActualPosition += 1;
                showNextMessage(mActualPosition);
            }
        }, time );
    }

    /**
     * Coleta do banco de dados as matérias adicionadas pelo usuário.
     */
    @Background( serial = SERIAL_GET_VALUES )
    void getSubjects(){
        mAspgaContext.setSubjects(studyPlanManager.getAspgaSubjects());
        getAvailabilities();
    }

    /**
     * Coleta do banco de dados as disponibilidades por período fornecidos pelo usuário.
     */
    @Background( serial = SERIAL_GET_VALUES )
    void getAvailabilities(){
        mAspgaContext.setDayPeriodAvailable(studyPlanManager.getAspgaPeriodAvailable());
        getIntelectual();
    }

    /**
     * Coleta do banco de dados a facilidade de aprendizado por turno fornecido pelo usuário.
     */
    @Background( serial = SERIAL_GET_VALUES )
    void getIntelectual(){
        mAspgaContext.setIntelectualAvailable(studyPlanManager.getAspgaPeriodIntelectual());
        createPlan();
    }

    /**
     * Gera o plano de estudo utilizando o código ASPGA. Após a geração o plano de estudo é salvo
     * no banco de dados.
     */
    @Background( serial = SERIAL_GET_VALUES )
    void createPlan(){
        long start = System.currentTimeMillis();

        Trace myTrace = FirebasePerformance.getInstance().newTrace("create_plan");
        myTrace.start();

        StudyPlan studyPlan = ASPGAManager.createPlanWithECJ(this);
        if( studyPlan != null ){
            studyPlanManager.setStudyPlan(studyPlan);
            long end = System.currentTimeMillis();

            NumberFormat formatter = new DecimalFormat("#0.00000");
            SLog.d(TAG, "Execution time is " + formatter.format((end - start) / 1000d) + " seconds");

            onFinishGenerate();

            myTrace.stop();
        } else {
            showDialog();
            SLog.e(TAG, "Plano de estudo não gerado");

            myTrace.stop();
        }
    }

    /**
     * Mostra um <code>AlertDialog</code> para o usuário em caso de erro na geração do plano de
     * estudo.
     */
    @UiThread
    void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.create_plan_error);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                init();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EaseOfLearningActivity_.intent( CreatePlanActivity.this ).start();
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.screen_fade_in, R.anim.screen_fade_out);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Exibe a tela de geração de plano de estudo concluído.
     */
    @UiThread
    void onFinishGenerate(){
        if( mCreatePlanView != null ){
            mCreatePlanView.showComplete();
        }

        studyPlanManager.saveStep(Step.STEP_GENERATE_COMPLETED);

        Intent intent = new Intent();
        intent.setAction(BootReceiver.CREATED_PLAN_RECEIVER);

        sendBroadcast(intent);

        updateFinishLayout( true );
    }

    /**
     * Controla a exibição do layout de conclusão do plano de estudo.
     *
     * @param isVisible <code>true</code> caso a tela é para ser exibida, <code>false</code> caso
     *                  contrário.
     */
    private void updateFinishLayout(boolean isVisible){
        continueBtn.setEnabled( isVisible );
        continueBtn.setClickable( isVisible );
        continueBtn.setVisibility( isVisible ? View.VISIBLE : View.GONE );
        finishedLayout.setVisibility( isVisible ? View.VISIBLE : View.GONE );
    }

    /**
     * Vai para a próxima tela, calendário de plano de estudo.
     */
    @Click
    void continueBtnClicked(){
        CalendarSubjectActivity_.intent( CreatePlanActivity.this ).start();
        finish();
        overridePendingTransition(R.anim.screen_fade_in, R.anim.screen_fade_out);
    }

    /**
     * Não permite retornar para a tela anterior, pois a geração do plano de estudo está em andamento.
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
