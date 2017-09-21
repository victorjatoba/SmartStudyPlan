package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.activity.settings.SettingsActivity_;
import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.StudyPlan;
import br.com.smartstudyplan.manager.StudyPlanManager;
import br.com.smartstudyplan.util.SLog;
import br.com.smartstudyplan.util.ShareImageUtil;
import br.com.smartstudyplan.view.CalendarSubjectItemView;
import br.com.smartstudyplan.view.CalendarSubjectItemView_;
import br.com.smartstudyplan.view.CustomLinearLayout;
import br.com.smartstudyplan.view.CustomScrollView;

/**
 * Esta tela é exibida após o plano de estudo criado, ela mostra um calendário no formato
 * semanal onde o usuário pode visualizar as matérias com as respectivas cargas horárias.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_calendar_subject )
@OptionsMenu( R.menu.menu_calendar_subject )
public class CalendarSubjectActivity extends AppCompatActivity {
    private static final String TAG = "CalendarSubjectActivity";

    private static final int EDIT_REQUEST_CODE = 1001;

    @ViewById CustomScrollView scroll;
    @ViewById RelativeLayout   layout;

    @ViewById CustomLinearLayout sunday;
    @ViewById CustomLinearLayout monday;
    @ViewById CustomLinearLayout tuesday;
    @ViewById CustomLinearLayout wednesday;
    @ViewById CustomLinearLayout thursday;
    @ViewById CustomLinearLayout friday;
    @ViewById CustomLinearLayout saturday;

    @ViewById LinearLayout sundayMorning;
    @ViewById LinearLayout sundayAfternoon;
    @ViewById LinearLayout sundayNight;

    @ViewById LinearLayout mondayMorning;
    @ViewById LinearLayout mondayAfternoon;
    @ViewById LinearLayout mondayNight;

    @ViewById LinearLayout tuesdayMorning;
    @ViewById LinearLayout tuesdayAfternoon;
    @ViewById LinearLayout tuesdayNight;

    @ViewById LinearLayout wednesdayMorning;
    @ViewById LinearLayout wednesdayAfternoon;
    @ViewById LinearLayout wednesdayNight;

    @ViewById LinearLayout thursdayMorning;
    @ViewById LinearLayout thursdayAfternoon;
    @ViewById LinearLayout thursdayNight;

    @ViewById LinearLayout fridayMorning;
    @ViewById LinearLayout fridayAfternoon;
    @ViewById LinearLayout fridayNight;

    @ViewById LinearLayout saturdayMorning;
    @ViewById LinearLayout saturdayAfternoon;
    @ViewById LinearLayout saturdayNight;

    @Bean StudyPlanManager manager;

    private StudyPlan mStudyPlan;

    private LinearLayout[] mPeriodViews;

    private float mDensity;

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

        mDensity = getResources().getDisplayMetrics().density;

        mPeriodViews = new LinearLayout[]{
                sundayMorning, sundayAfternoon, sundayNight,
                mondayMorning, mondayAfternoon, mondayNight,
                tuesdayMorning, tuesdayAfternoon, tuesdayNight,
                wednesdayMorning, wednesdayAfternoon, wednesdayNight,
                thursdayMorning, thursdayAfternoon, thursdayNight,
                fridayMorning, fridayAfternoon, fridayNight,
                saturdayMorning, saturdayAfternoon, saturdayNight
        };

        getStudyPlan();
    }

    /**
     * Busca no banco de dados o plano de estudo gerado.
     */
    @Background
    void getStudyPlan(){
        mStudyPlan = manager.getStudyPlan();
        if( mStudyPlan != null ){
            SLog.d(TAG, "Generate study plan: " + mStudyPlan.toString());
            showResult();
        }
    }

    /**
     * Mostra o plano de estudo na tela. Responsável por montar o layout.
     */
    @UiThread
    void showResult(){
        LinearLayout view;

        for(int i=0; i < Availability.Weekday.values().length; i++){
            for (int k=0; k < Availability.Period.values().length; k++){
                view = mPeriodViews[(i * 3) + (k)];
                if( mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(
                        Availability.Weekday.getWeekday(i),
                        Availability.Period.getPeriod(k) ).size() > 0 ){

                    List<CalendarSubject> items = mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(
                            Availability.Weekday.getWeekday(i), Availability.Period.getPeriod(k) );

                    if( (items != null) && ( items.isEmpty() == false ) ){
                        for(int position=0; position < items.size(); position++){
                            addSubjectToPeriod( view, items.get(position), (position == 0) );
                        }
                    }
                }
            }
        }

        scrollToDayPosition();
    }

    /**
     * Cria cada item do plano de estudo que será exibido para o usuário.
     *
     * @param layout ViewGroup do período em que será adicionado a matéria
     * @param subject A matéria da qual serão pegos os dados para criar o item
     * @param isLastItem Informa se é o último elemento do período
     */
    private void addSubjectToPeriod( LinearLayout layout, CalendarSubject subject, boolean isLastItem ){
        CalendarSubjectItemView itemView
                = CalendarSubjectItemView_.build( CalendarSubjectActivity.this );

        itemView.bindView(subject, mDensity, isLastItem);

        layout.addView( itemView );
    }

    /**
     * Verifica o dia da semana para fazer o scroll na posição correta.
     */
    private void scrollToDayPosition(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK );

        SLog.d( TAG, "Day of week: " + dayOfWeek );

        switch( dayOfWeek ){
            case Calendar.SUNDAY:
                scrollToView(scroll, saturday);
                break;
            case Calendar.MONDAY:
                scrollToView(scroll, null);
                break;
            case Calendar.TUESDAY:
                scrollToView(scroll, monday);
                break;
            case Calendar.WEDNESDAY:
                scrollToView(scroll, tuesday);
                break;
            case Calendar.THURSDAY:
                scrollToView(scroll, wednesday);
                break;
            case Calendar.FRIDAY:
                scrollToView(scroll, thursday);
                break;
            case Calendar.SATURDAY:
                scrollToView(scroll, friday);
                break;
        }
    }

    /**
     * Faz o scroll da tela para a posição correta a partir do dia da semana.
     *
     * @param scrollView o <code>ScrollView</code> contido na tela
     * @param view a <code>View</code> que o scroll deve mostrar
     */
    private void scrollToView(final ScrollView scrollView, final View view) {
        if( view == null ){
            SLog.w( TAG, "The view is null" );
            return ;
        }

        SLog.d( TAG, "view.getBottom(): " + view.getBottom() );

        // View needs a focus
        view.requestFocus();

        // Determine if scroll needs to happen
        final Rect scrollBounds = new Rect();
        scrollView.getHitRect(new Rect());
        if (!view.getLocalVisibleRect(scrollBounds)) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if( view == null ){
                        scrollView.smoothScrollTo(0, 0);
                    }
                    else{
                        scrollView.smoothScrollTo(0, view.getBottom());
                    }
                }
            });
        }
    }

    /**
     * Chama a tela de edição do plano de estudo ao clicar no menu na action bar.
     */
    @OptionsItem
    void edit(){
        CalendarSubjectEditActivity_
                .intent(CalendarSubjectActivity.this)
                .startForResult(EDIT_REQUEST_CODE);
    }

    /**
     * Chamado caso o plano de estudo seja alterado.
     *
     * @param resultCode Retorna se o plano foi alterado ou não.
     */
    @OnActivityResult(EDIT_REQUEST_CODE)
    void onActivityResult(int resultCode) {
        for(LinearLayout layout : mPeriodViews){
            layout.removeAllViews();
        }
        getStudyPlan();
    }

    /**
     * Chama a tela de ajuda (dicas).
     */
    @OptionsItem
    void help(){
        HelpActivity_.intent( CalendarSubjectActivity.this )
                .title(getString(R.string.help_study_plan_title))
                .text(getString(R.string.help_study_plan_text))
                .start();
    }

    /**
     * Chama a tela das matérias para gerar um novo plano de estudo ao selecionar 'Criar novo Plano'
     * na menu Action Bar.
     */
    @OptionsItem
    void createNewPlan(){
        SubjectActivity_.intent( CalendarSubjectActivity.this ).start();
        finish();
        overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right );
    }

    /**
     * Chama a tela de criar plano de estudo com os mesmos parâmetros atuais ao clicar em
     * 'Sugerir novo plano' na menu Action Bar.
     */
    @OptionsItem
    void suggestNewPlan(){
        CreatePlanActivity_.intent( CalendarSubjectActivity.this ).start();
        finish();
        overridePendingTransition( R.anim.screen_fade_in, R.anim.screen_fade_out );
    }

    /**
     * Inicia o processo de compartilhamento do plano de estudo.
     */
    @OptionsItem
    void share(){
        ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage( getString(R.string.please_wait) );
        progressDialog.show();
        generatePlanImage(progressDialog);
    }

    /**
     * Gera a imagem a ser compartilhado.
     *
     * @param progressDialog o dialog de loading a ser exibido enquanto se prepara o conteúdo
     */
    @Background
    void generatePlanImage(ProgressDialog progressDialog){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK );

        SLog.d( TAG, "Day of week: " + dayOfWeek );

        CustomLinearLayout dayView = null;

        switch( dayOfWeek ){
            case Calendar.SUNDAY:
                dayView = sunday;
                break;
            case Calendar.MONDAY:
                dayView = monday;
                break;
            case Calendar.TUESDAY:
                dayView = tuesday;
                break;
            case Calendar.WEDNESDAY:
                dayView = wednesday;
                break;
            case Calendar.THURSDAY:
                dayView = thursday;
                break;
            case Calendar.FRIDAY:
                dayView = friday;
                break;
            case Calendar.SATURDAY:
                dayView = saturday;
                break;
        }

        Bitmap bitmap = ShareImageUtil.getBitmapByCustomView( CalendarSubjectActivity.this,
                dayView );

        if( bitmap == null ){
            return ;
        }

        File file = ShareImageUtil.saveBitmapOnExternalStorage( CalendarSubjectActivity.this,
                bitmap, "study_plan_day" );

        if( file == null ){
            return ;
        }

        shareStudyPlan(progressDialog, file);
    }

    /**
     * Mostra a lista de aplicativos que permitem o compartilhamento de imagem gerado do
     * plano de estudo.
     *
     * @param progressDialog o dialog de loading a ser exibido enquanto se prepara o conteúdo
     * @param file o arquivo com o conteúdo a ser compartilhado
     */

    @UiThread
    void shareStudyPlan(ProgressDialog progressDialog, File file ){
        if( file != null ){
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("image/png");
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) );

            // For a file in shared storage.  For data in private storage, use a ContentProvider.
            Uri uri = Uri.fromFile(file);
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);

            progressDialog.dismiss();

            startActivity(Intent.createChooser(sendIntent, getString(R.string.menu_share)));
        }
        else{
            //TODO error
        }
    }

    /**
     * Chama a tela de Configurações.
     */
    @OptionsItem
    void settings(){
        SettingsActivity_.intent(CalendarSubjectActivity.this).start();
        overridePendingTransition( R.anim.screen_fade_in, R.anim.screen_fade_out );
    }

}
