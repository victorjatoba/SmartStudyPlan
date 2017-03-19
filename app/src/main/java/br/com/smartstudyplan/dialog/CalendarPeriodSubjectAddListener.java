package br.com.smartstudyplan.dialog;

import java.util.List;

import br.com.smartstudyplan.bean.CalendarSubject;

/**
 * Interface auxiliar para o dialog <code>SubjectAddDialog</code>.
 */
public interface CalendarPeriodSubjectAddListener {
	public void returnValues(List<CalendarSubject> calendarSubjectList);
}
