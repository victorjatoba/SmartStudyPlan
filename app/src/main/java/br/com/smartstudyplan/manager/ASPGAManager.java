package br.com.smartstudyplan.manager;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.StudyPlan;
import ec.EvolutionState;
import ec.Evolve;
import ec.Individual;
import ec.app.aspga.DayPlanGene;
import ec.app.aspga.DayPlanGeneVectorIndividual;
import ec.app.aspga.bean.AspgaContext;
import ec.app.aspga.bean.SubjectWorkload;
import ec.simple.SimpleStatistics;
import ec.util.Output;
import ec.util.ParameterDatabase;
import ec.vector.Gene;

/**
 * Esta classe é responsável por gerenciar a comunicação entre o aplicativo e o ASPGA.
 */
public class ASPGAManager {

    /**
     * Este método é responsável por chamar a biblioteca ASPGA, que gerará o plano de estudo ideal
     * a partir dos parâmetros recebidos.
     *
     * @param context o <code>Context</code> do aplicativo que está fazendo chamada a esse método
     * @return o <code>StudyPlan</code> "ideal", ou <code>null</code>, caso tenha ocorrido algum
     * erro na geração
     */
    public static StudyPlan createPlanWithECJ(Context context){
        try {
            InputStream configStream            = context.getAssets().open("aspga.params");
            ParameterDatabase parameterDatabase = new ParameterDatabase(configStream);

            Output output = Evolve.buildOutput();
            output.getLog(1).silent = true;

            EvolutionState evolutionState = Evolve.initialize(parameterDatabase, 0, output);
            evolutionState.run(EvolutionState.C_STARTED_FRESH);

            // Pega o melhor Individual da run
            Individual[] inds = ((SimpleStatistics)evolutionState.statistics).getBestSoFar();

            StudyPlan studyPlan = new StudyPlan();

            for (Individual ind : inds){
                // Aqui imprime o melhor
//                ind.printIndividualForHumans(evolutionState,0);
                // Transforma o Individual em DayPlanGeneVectorIndividual
                DayPlanGeneVectorIndividual dp = (DayPlanGeneVectorIndividual)ind;

                //Pega os genes do DayPlanGeneVectorIndividual
                Gene[] genes = ((Gene[])dp.getGenome());

                DayPlanGene[] dpgs = new DayPlanGene[genes.length];
                //Transforma os genes em DayPlanGene
                for (int i = 0; i < genes.length; i++){
                    dpgs[i] = (DayPlanGene)genes[i];
                }

                // Aqui deve ser montado o objeto do StudyPlan propriamente dito, com as matérias e duração (workload/2)
                CalendarSubject calendarSubject;
                for(int j = 0; j  < dpgs.length; j++) {

                    for (SubjectWorkload sw : dpgs[j].getMorning()) {
                        calendarSubject = new CalendarSubject( sw );
                        studyPlan.addCalendarSubject(Availability.Weekday.getWeekday(j), Availability.Period.MORNING, calendarSubject );
                    }

                    for (SubjectWorkload sw : dpgs[j].getAfternoon()) {
                        calendarSubject = new CalendarSubject( sw );
                        studyPlan.addCalendarSubject(Availability.Weekday.getWeekday(j), Availability.Period.AFTERNOON, calendarSubject );
                    }

                    for (SubjectWorkload sw : dpgs[j].getNight()) {
                        calendarSubject = new CalendarSubject( sw );
                        studyPlan.addCalendarSubject(Availability.Weekday.getWeekday(j), Availability.Period.NIGHT, calendarSubject );
                    }
                }
            }

            return studyPlan;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
