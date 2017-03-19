package br.com.smartstudyplan.adapter;

import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;

import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.view.CalendarSubjectEditSpinnerItemView;
import br.com.smartstudyplan.view.CalendarSubjectEditSpinnerItemView_;

/**
 * Este adapter é chamado pelo spinner e é responsável por gerar a lista de matérias cadastradas
 * com ícone e nome.
 */
@EBean
public class CalendarSubjectEditSpinnerAdapter extends DynamicBaseAdapter {

    /**
     * Este método é chamado pelo Spinner para buscar as matérias a serem exibidas nele.
     *
     * @param position A posição do item na lista.
     * @param convertView A <code>View</code> anterior que pode ser reutilizada, se possível.
     * @param parent A <code>ViewGroup</code> em que a <code>View</code> vai estar inserida.
     * @return A <code>View</code> que vai ser exibida na posição especificada.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarSubjectEditSpinnerItemView calendarSubjectEditSpinnerItemView;
        if( convertView == null ){
            calendarSubjectEditSpinnerItemView = CalendarSubjectEditSpinnerItemView_.build(mContext);
        }
        else{
            calendarSubjectEditSpinnerItemView = (CalendarSubjectEditSpinnerItemView_) convertView;
        }

        Subject subject = (Subject) getItem( position );

        calendarSubjectEditSpinnerItemView.bindView(subject);

        return calendarSubjectEditSpinnerItemView;
    }
}
