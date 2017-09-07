package br.com.smartstudyplan.adapter;

import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.view.SubjectItemView;
import br.com.smartstudyplan.view.SubjectItemView_;

/**
 * Este adapter é chamado pela tela SubjectActivity e é responsável por gerar a lista de matérias
 * a ser mostrado na tela, a partir da lista de matérias informada.
 */
@EBean
public class SubjectAdapter extends DynamicBaseAdapter {

    private Subject selectedSubject;

    /**
     * Informa à lista qual elemento foi selecionado.
     *
     * @param subject Elemento que foi selecionado.
     */
    public void setSelectedSubject( Subject subject ) {
        selectedSubject = subject;
        notifyDataSetChanged();
    }

    public Subject getSelected() {
        return selectedSubject;
    }

    /**
     * Este método é chamado pelo ListView para buscar as views a serem exibidas na tela.
     *
     * @param position A posição do item na lista.
     * @param convertView A <code>View</code> anterior que pode ser reutilizada, se possível.
     * @param parent A <code>ViewGroup</code> em que a <code>View</code> vai estar inserida.
     * @return A <code>View</code> que vai ser exibida na posição especificada.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SubjectItemView subjectItemView;
        if( convertView == null ){
            subjectItemView = SubjectItemView_.build(mContext);
        }
        else{
            subjectItemView = (SubjectItemView_) convertView;
        }

        Subject subject = (Subject) getItem( position );

        subjectItemView.bindView(subject, position, subject.equals( selectedSubject ) );

        return subjectItemView;
    }
}
