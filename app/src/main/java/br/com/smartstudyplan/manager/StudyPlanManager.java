package br.com.smartstudyplan.manager;

import android.text.TextUtils;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.EaseOfLearning;
import br.com.smartstudyplan.bean.StudyPlan;
import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.preferences.UserPreferences_;
import br.com.smartstudyplan.provider.dao.CalendarSubjectDAO;
import br.com.smartstudyplan.provider.dao.SubjectDAO;
import br.com.smartstudyplan.util.SLog;
import ec.app.aspga.bean.Period;
import ec.app.aspga.bean.PeriodAvailable;

/**
 * Esta classe é responsável por gerenciar as informações a serem salvas no banco de dados e em
 * preferences do usuário.
 */
@EBean( scope = EBean.Scope.Singleton )
public class StudyPlanManager {
    private static final String TAG = "StudyPlanManager";

    @Bean SubjectDAO         subjectDAO;
    @Bean CalendarSubjectDAO calendarSubjectDAO;

    @Pref UserPreferences_ userPreferences;

    /**
     * Verifica se a matéria está correta para ser salva no banco de dados.
     *
     * @param subject a matéria a ser salva
     * @return <code>true</code> se for inserida com sucesso, <code>false</code> se não tiver
     * sucesso
     */
    public boolean addSubject( Subject subject ){
        //TODO possível erro, talvez o subject seja sempre null ....
        return ((subject != null) || TextUtils.isEmpty(subject.getName()) == false) && subjectDAO.add(subject);
    }

    /**
     * Verifica se as matérias estão corretas para serem salvas no banco de dados.
     *
     * @param subjectList a lista de matérias a serem salvas
     * @return <code>true</code> se forem inseridas com sucesso, <code>false</code> se não tiver
     * sucesso
     */
    public boolean addAllSubject( List<Subject> subjectList ){
        return (subjectList != null) && (subjectList.isEmpty() == false) && subjectDAO.addAll(subjectList);

    }

    /**
     * Pega do banco de dados a lista de todas as matérias.
     *
     * @return a lista com todas as matérias salvas
     */
    public List<Subject> getSubjects(){
        return subjectDAO.getAll();
    }


    /**
     * Pega do banco de dados um <code>array</code> com todas as matérias no formato do ASPGA.
     *
     * @return o <code>array</code> de matérias
     */
    public ec.app.aspga.bean.Subject[] getAspgaSubjects(){
        List<Subject> tempSubjects = getSubjects();
        if( ( tempSubjects != null ) && ( tempSubjects.isEmpty() == false ) ){
            ec.app.aspga.bean.Subject[] mSubjects = new ec.app.aspga.bean.Subject[tempSubjects.size()];
            for( int i = 0; i < tempSubjects.size() ; i++ ){
                mSubjects[i] = tempSubjects.get(i).getAspgaSubject();
            }
            return mSubjects;
        }
        return null;
    }

    /**
     * Pega do banco de dados a matéria a partir do seu ID.
     *
     * @param id o ID da matéria
     * @return a matéria correspondente ao ID passado ou <code>null</code> se ela não existir
     */
    public Subject getSubjectById( long id ){
        if( id <= 0 ){
            return null;
        }
        return subjectDAO.getById( id );
    }

    /**
     * Pega do banco de dados a matéria a partir do seu nome.
     *
     * @param name o nome da matéria
     * @return a matéria correspondente ao nome passado
     */
    public Subject getSubjectByName( String name ){
        if( TextUtils.isEmpty( name ) ){
            return null;
        }

        return subjectDAO.getByName( name );
    }

    /**
     * Chama o banco de dados para remover a matéria.
     *
     * @param subject a matéria a ser removida
     * @return <code>true</code> se ela for removida com sucesso, <code>false</code> se não tiver
     * sucesso
     */
    public boolean removeSubject( Subject subject ){
        return subjectDAO.remove( subject );
    }

    /**
     * Chama o banco de dados para remover todas as matérias.
     */
    public void clearSubjects(){
        subjectDAO.removeAll();
    }

    /**
     * Pega do preferences a lista de todas as disponibilidades.
     *
     * @return as disponibilidades
     */
    public Availability getAvailabilities(){
        String availabilities = userPreferences.availabilities().get();

        if( TextUtils.isEmpty( availabilities ) == false ){
            String[] values = availabilities.split( "," );

            List<Integer> availabilitiesList = new ArrayList<>();
            for( String value :  values ){
                availabilitiesList.add( Integer.valueOf( value ) );
            }

            SLog.d(TAG, "Get availabilities: " + availabilitiesList.toString());

            Availability availability = new Availability();
            availability.setAllValues( availabilitiesList );

            return availability;
        }

        return null;
    }

    /**
     * Pega o tempo disponível para estudo no formato do ASPGA.
     *
     * @return o tempo disponível para estudo
     */
    public PeriodAvailable getAspgaPeriodAvailable(){
        Availability availability = getAvailabilities();

        if( availability != null ){
            return availability.getAvailabilities();
        }

        return null;
    }

    /**
     * Pega a facilidade de aprendizado no formato do ASPGA.
     *
     * @return a facilidade de aprendizado
     */
    public PeriodAvailable getAspgaPeriodIntelectual(){
        PeriodAvailable intelectualAvailable = new PeriodAvailable();
        Period[] intelectuals = new Period[7];
        Period period;
        EaseOfLearning easeOfLearning = this.getEaseOfLearning();
        for( int i = 0; i < 7; i++ ){
            period = new Period();
            period.setMorning( getCorrectlyIntelectual( easeOfLearning.isEaseOfLearningMorning() ) );
            period.setAfternoon( getCorrectlyIntelectual( easeOfLearning.isEaseOfLearningAfternoon() ) );
            period.setNight( getCorrectlyIntelectual( easeOfLearning.isEaseOfLearningNight() ) );

            intelectuals[i] = period;
        }

        intelectualAvailable.setStudyCycle( intelectuals );

        return intelectualAvailable;
    }

    private char getCorrectlyIntelectual( boolean localIntelectual ){
        if( localIntelectual ){
            return 'G';
        }

        return 'B';
    }

    /**
     * Salva as disponibilidades em preferences.
     *
     * @param availability as disponibilidades a serem salvas
     */
    public void saveAvailabilities( Availability availability ){
        if( availability != null ){
            SLog.d(TAG, "Save availabilities: " + availability.getValuesList().toString() );

            List<Integer> values = availability.getValuesList();

            StringBuilder stb = new StringBuilder();
            for( int i = 0; i < values.size(); i++ ){
                stb.append( values.get( i ) );
                if( i < ( values.size() - 1 ) ){
                    stb.append( "," );
                }
            }

            userPreferences.availabilities().put( stb.toString() );
        }
    }

    /**
     * Pega de <code>SharedPreferences</code> a última tela acessado pelo usuário.
     *
     * @return o passo correspondente à tela conforme classe <code>Step</code>
     */
    public int getStep(){
        return userPreferences.step().get();
    }

    /**
     * Salva em <code>SharedPreferences</code> a última tela acessado pelo usuário.
     *
     * @param step o passo correspondente à tela conforme classe <code>Step</code>
     */
    public void saveStep( int step ){
        userPreferences.step().put( step );
    }

    /**
     * Pega do <code>SharedPreferences</code> a facilidade de aprendizado escolhida pelo usuário
     * nos períodos.
     *
     * @return o <code>EaseOfLearning</code> contendo os períodos nos quais o usuário tem
     * facilidade de aprendizado.
     */
    public EaseOfLearning getEaseOfLearning(){
        return new EaseOfLearning(
                        userPreferences.easeOfLearningMorning().get(),
                        userPreferences.easeOfLearningAfternoon().get(),
                        userPreferences.easeOfLearningNight().get() );
    }

    /**
     * Salva em <code>SharedPreferences</code> a facilidade de aprendizado escolhida pelo
     * usuário nos períodos.
     *
     * @param easeOfLearning a facilidade de aprendizado dos três períodos do dia
     */
    public void saveEaseOfLearning(EaseOfLearning easeOfLearning) {
        userPreferences.easeOfLearningMorning().put(easeOfLearning.isEaseOfLearningMorning());
        userPreferences.easeOfLearningAfternoon().put(easeOfLearning.isEaseOfLearningAfternoon());
        userPreferences.easeOfLearningNight().put(easeOfLearning.isEaseOfLearningNight());
    }

    /**
     * Remove todas os dados dentro do aplicativo.
     */
    public void resetInfos(){
        subjectDAO.removeAll();
        userPreferences.clear();
    }

    /**
     * Salva o plano de estudo.
     *
     * @param studyPlan
     */
    public void setStudyPlan( StudyPlan studyPlan ){
        calendarSubjectDAO.setStudyPlan( studyPlan );
    }

    /**
     * Retorna o plano de estudo salvo.
     *
     * @return o plano de estudo
     */
    public StudyPlan getStudyPlan(){
        StudyPlan studyPlan = calendarSubjectDAO.getStudyPlan();
        StudyPlan studyPlan2 = new StudyPlan();

        List<Subject> subjects = getSubjects();

        List<CalendarSubject> calendarSubjects;
        Subject subject = null;
        for(Availability.Weekday weekday : Availability.Weekday.values()){
            for(Availability.Period period : Availability.Period.values()){
                if( studyPlan.getCalendarSubjectsByWeekdayAndPeriod(weekday, period).size() > 0 ){
                    calendarSubjects = new ArrayList<>();
                    for( CalendarSubject calendarSubject : studyPlan.getCalendarSubjectsByWeekdayAndPeriod(weekday, period) ){
                        for( Subject subject1 : subjects ){
                            if( calendarSubject.getSubject().getId() == subject1.getId() ){
                                subject = Subject.clone( subject1 );
                                break;
                            }
                        }
                        SLog.d( TAG, "Subject id: " + calendarSubject.getSubject().getId() );
                        if( subject != null ){
                            SLog.d( TAG, "Add subject: " + subject.toString() + " in calendar subject." );
                            calendarSubject.setSubject( subject );
                        }
                        else{
                            SLog.e( TAG, "Subject is null" );
                        }

                        calendarSubjects.add( calendarSubject );
                    }
                    studyPlan2.setCalendarSubjectsByWeekdayAndPeriod(weekday, period, calendarSubjects);
                }
            }
        }

        return studyPlan2;
    }

}
