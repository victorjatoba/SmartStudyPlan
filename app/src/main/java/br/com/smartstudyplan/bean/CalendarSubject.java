package br.com.smartstudyplan.bean;

import android.content.ContentValues;

import br.com.smartstudyplan.provider.table.CalendarSubjectTable;
import ec.app.aspga.bean.SubjectWorkload;

/**
 * Esta classe encapsula as informações referentes a matéria em um período do dia.
 */
public class CalendarSubject {
    private long    id;
    private Subject subject;
    private float   duration;

    public CalendarSubject(){ /* no code */ }

    public CalendarSubject( SubjectWorkload subjectWorkload ){
        subject  = new Subject( subjectWorkload.getSubject() );
        duration = (float)subjectWorkload.getWorkload() / 2.0f;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * Gera o arquivo com as informações desta classe para ser salvo no banco de dados.
     *
     * @return o <code>ContentValues</code> com as informações
     */
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put( CalendarSubjectTable.SUBJECT_ID.getColumnName(), subject.getId() );
        values.put( CalendarSubjectTable.TIME.getColumnName(),       duration );

        return values;
    }

    @Override
    public String toString() {
        return "(" + subject.getName() + ", dur: " + duration + ")";
    }
}
