package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.adapter.CalendarSubjectEditSpinnerAdapter;
import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.bean.Availability.Period;
import br.com.smartstudyplan.bean.Availability.Weekday;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.StudyPlan;
import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.dialog.CalendarPeriodSubjectAddDialog;
import br.com.smartstudyplan.manager.StudyPlanManager;
import br.com.smartstudyplan.util.SLog;
import br.com.smartstudyplan.view.CalendarSubjectAddItemView;
import br.com.smartstudyplan.view.CalendarSubjectAddItemView_;
import br.com.smartstudyplan.view.CalendarSubjectEditItemView;
import br.com.smartstudyplan.view.CalendarSubjectEditItemView_;

/**
 * Esta tela é exibida ao selecionar a opção de editar plano de estudo, após o plano de estudo ser
 * criado. Ela mostra um calendário no formato semanal onde o usuário pode editar as matérias com
 * as cargas horárias em cada dia e período.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_calendar_subject_edit )
@OptionsMenu( R.menu.menu_calendar_subject_edit )
public class CalendarSubjectEditActivity extends AppCompatActivity {
    private static final String TAG = "CalendarSubjectEditActivity";

    @ViewById ScrollView scroll;

    @ViewById LinearLayout sunday;
    @ViewById LinearLayout monday;
    @ViewById LinearLayout tuesday;
    @ViewById LinearLayout wednesday;
    @ViewById LinearLayout thursday;
    @ViewById LinearLayout friday;
    @ViewById LinearLayout saturday;

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

    @Bean CalendarSubjectEditSpinnerAdapter subjectAdapter;

    private StudyPlan mStudyPlan;

    private List<Subject> mSubjects;

    private Weekday mSelectedWeekday;
    private Period mSelectedPeriod;
    private CalendarSubject mSelectedSubject;
    private CalendarSubjectEditItemView mSelectedView;

    private LinearLayout mSelectedDayPeriod;

    private LinearLayout[] mViews;

    private ActionMode mActionMode;

    private float mDensity;

    private boolean isUpdated;

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

        mDensity = getResources().getDisplayMetrics().density;

        mViews = new LinearLayout[]{
                sundayMorning, sundayAfternoon, sundayNight,
                mondayMorning, mondayAfternoon, mondayNight,
                tuesdayMorning, tuesdayAfternoon, tuesdayNight,
                wednesdayMorning, wednesdayAfternoon, wednesdayNight,
                thursdayMorning, thursdayAfternoon, thursdayNight,
                fridayMorning, fridayAfternoon, fridayNight,
                saturdayMorning, saturdayAfternoon, saturdayNight
        };

        mSubjects = manager.getSubjects();

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
     * Mostra o plano de estudo na tela. Responsável por montar o layout. Caso um período não possua
     * matéria adicionada, será exibido uma view para adicionar matérias naquele perídodo.
     */
    @UiThread
    void showResult(){
        LinearLayout view;
        for(int i=0; i < Weekday.values().length; i++){
            for (int k=0; k < Period.values().length; k++){
                view = mViews[(i * 3) + (k)];
                Weekday weekday = Availability.Weekday.getWeekday(i);
                Period period = Availability.Period.getPeriod(k);
                float totalTime = 0;
                int numberOfSubjects = mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(weekday, period ).size();
                if( numberOfSubjects > 0 ){
                    List<CalendarSubject> items = mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod( weekday, period );
                    if( (items != null) && !items.isEmpty() ){
                        for(int position=0; position < items.size(); position++){
                            totalTime += items.get(position).getDuration();
                            addSubjectToPeriod( view, weekday, period, items.get(position), (position == 0) );
                        }
                    }
                }
                if( totalTime < 5 && numberOfSubjects < mSubjects.size() ){
                    addExtraToPeriod( view, weekday, period, 5 - totalTime );
                }
            }
        }

        scrollToDayPosition();
    }

    /**
     * Cria cada item do plano de estudo que será exibido para o usuário.
     *
     * @param layout <code>ViewGroup</code> do período em que será adicionado a matéria
     * @param weekday dia da semana
     * @param period período
     * @param subject a matéria que será pego os dados para criar o item
     * @param isLastItem Informa se é o último elemento do período
     */
    private void addSubjectToPeriod( final LinearLayout layout, final Weekday weekday,
                                     final Period period, final CalendarSubject subject,
                                     boolean isLastItem ){
        CalendarSubjectEditItemView itemView
                = CalendarSubjectEditItemView_.build(CalendarSubjectEditActivity.this);

        itemView.setOnClickListener(view -> {
            mActionMode = startSupportActionMode( mActionModeCallBack );
            if (mActionMode != null) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }
                mActionMode.setTitle(R.string.subject_edit_menu);
            }

            mSelectedWeekday   = weekday;
            mSelectedPeriod    = period;
            mSelectedDayPeriod = layout;
            mSelectedSubject   = subject;
            mSelectedView      = (CalendarSubjectEditItemView)view;

            mSelectedView.setEditSelected(true);
        });

        itemView.bindView(subject, mDensity, isLastItem);

        layout.addView( itemView );
    }

    /**
     * Adiciona uma view com o tempo disponível para aquele período.
     *
     * @param layout <code>ViewGroup</code> do período em que será adicionado a matéria
     * @param weekday dia da semana
     * @param period período
     * @param availableTime Quantidade de tempo extra disponível para o período
     */
    private void addExtraToPeriod( final LinearLayout layout, final Weekday weekday,
                                   final Period period, final float availableTime ){
        CalendarSubjectAddItemView itemView
                = CalendarSubjectAddItemView_.build(CalendarSubjectEditActivity.this);

        itemView.setOnClickListener(view -> {
            mSelectedWeekday = weekday;
            mSelectedPeriod = period;
            mSelectedDayPeriod = layout;
            mSelectedSubject = null;
            subjectAdapter.setContent(prepareAdapterContent());
            showDialog(mSelectedSubject, R.string.subject_add);
        });

        itemView.bindView(mDensity, availableTime);

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
            new Handler().post(() -> {
                if( view == null ){
                    scrollView.smoothScrollTo(0, 0);
                }
                else{
                    scrollView.smoothScrollTo(0, view.getBottom());
                }
            });
        }
    }

    /**
     * Atualiza o layout após ter adicionado ou removido uma matéria em um período.
     *
     * @param subjects Lista de todas as matérias
     * @param weekday Dia da semana que foi alterado
     * @param period Período que foi alterado
     * @param selectedView <code>View</code> que foi selecionada
     */
    @UiThread
    void updatePeriod(List<CalendarSubject> subjects, Weekday weekday, Period period, LinearLayout selectedView) {
        isUpdated = true;
        selectedView.removeAllViews();
        float totalTime = 0;
        for (CalendarSubject calendarSubject : subjects){
            totalTime += calendarSubject.getDuration();
            addSubjectToPeriod(selectedView, weekday, period, calendarSubject, false);
        }
        if( totalTime < 5 && subjects.size() < mSubjects.size() ) {
            addExtraToPeriod( selectedView, weekday, period, 5 - totalTime );
        }
    }

    /**
     * Chama a tela de ajuda (dicas).
     */
    @OptionsItem
    void help(){
        HelpActivity_.intent( CalendarSubjectEditActivity.this )
                .title(getString(R.string.help_study_plan_edit_title))
                .text(getString(R.string.help_study_plan_edit_text))
                .start();
    }

    /**
     * Mostra um dialog para informar que o plano de estudo alterado será salvo ao pressionar em voltar
     * no aparelho.
     */
    @Override
    public void onBackPressed() {
        if( isUpdated ){
            showProgressDialog();
            saveStudyPlanAndClose();
        }
        else{
            super.onBackPressed();
        }
    }

    /**
     * Mostra o dialog de salvamento do plano de estudo.
     */
    @UiThread
    void showProgressDialog(){
        ProgressDialog.show( this, getString(R.string.please_wait), getString(R.string.saving), false );
    }

    /**
     * Salva o plano de estudo e fecha a activity.
     */
    @Background
    void saveStudyPlanAndClose(){
        manager.setStudyPlan(mStudyPlan);
        finish();
    }

    /**
     * Contextual Action Bar utilizado quando seleciona uma matéria.
     */
    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_edit_subject, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            SLog.d( TAG, "onActionItemClicked" );
            switch (menuItem.getItemId()){
                case R.id.edit_subject:
                    subjectAdapter.setContent(prepareAdapterContent());
                    showDialog(mSelectedSubject, R.string.subject_edit);
                    actionMode.finish();
                    break;
                case R.id.delete_subject:
                    if( mSelectedView != null ){
                        mSelectedView.setEditSelected(false);
                        mSelectedView = null;
                    }

                    mStudyPlan
                            .getCalendarSubjectsByWeekdayAndPeriod(mSelectedWeekday, mSelectedPeriod)
                            .remove(mSelectedSubject);

                    updatePeriod(
                            mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(mSelectedWeekday, mSelectedPeriod),
                            mSelectedWeekday,
                            mSelectedPeriod,
                            mSelectedDayPeriod);
                    actionMode.finish();
                    break;
            }
            return false;
        }



        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            if( mSelectedView != null ){
                mSelectedView.setEditSelected(false);
                mSelectedView = null;
            }

            new Handler().postDelayed(() -> {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
            }, 300);

            SLog.d( TAG, "onDestroyActionMode" );
//            adapter.setSelectedSubject( null );
        }
    };

    /**
     * Mostra o dialog com o spinner de selação da matéria e o seekbar do tempo de estudo.
     *
     * @param subject a matéria que foi selecionada para edição ou uma nova matéria
     * @param titleResource o recurso do título do dialog
     */
    private void showDialog(CalendarSubject subject, int titleResource){
        CalendarPeriodSubjectAddDialog.show(
                CalendarSubjectEditActivity.this,
                titleResource,
                subjectAdapter,
                subject,
                mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(mSelectedWeekday, mSelectedPeriod),
                calendarSubjects -> updatePeriod(mStudyPlan
                        .getCalendarSubjectsByWeekdayAndPeriod(mSelectedWeekday, mSelectedPeriod),
                        mSelectedWeekday, mSelectedPeriod, mSelectedDayPeriod));
    }

    /**
     * Gera a lista de matérias a ser exibida no spinner do dialog
     *
     * @return a lista de matérias
     */
    private List<Subject> prepareAdapterContent(){
        List<Subject> subjects = new ArrayList<>();
        subjects.addAll(mSubjects);
        for( CalendarSubject calendarSubject : mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(mSelectedWeekday, mSelectedPeriod) ){
            if(!calendarSubject.equals(mSelectedSubject)){
                subjects.remove(calendarSubject.getSubject());
            }
        }
        return subjects;
    }

}
