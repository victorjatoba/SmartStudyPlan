package br.com.smartstudyplan.adapter;

import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.DefaultAvailability;
import br.com.smartstudyplan.view.AvailabilityItemView;
import br.com.smartstudyplan.view.AvailabilityItemView_;

/**
 * Este adapter é chamado pela tela <code>AvailabilityActivity</code> e é responsável por gerar a lista de itens
 * a ser mostrada na tela, a partir da lista de disponibilidades informada.
 */
@EBean
public class AvailabilityAdapter extends DynamicBaseAdapter {

    /**
     * Este método é chamdado pelo <code>ListView</code> para buscar as views a serem exibidos na
     * tela.
     *
     * @param position A posição do item na lista.
     * @param convertView A <code>View</code> anterior que pode ser reutilizada, se possível.
     * @param parent A <code>ViewGroup</code> em que a <code>View</code> vai estar inserida.
     * @return A <code>View</code> que vai ser exibida na posição especificada.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AvailabilityItemView itemView;
        if( convertView == null ){
            itemView = AvailabilityItemView_.build(mContext);
        }
        else{
            itemView = (AvailabilityItemView) convertView;
        }

        DefaultAvailability availability = (DefaultAvailability) getItem( position );

        itemView.bindView( availability );

        if( ( position % 2 ) == 0 ){
            itemView.setBackgroundResource( R.drawable.bg_item_white );
        }
        else{
            itemView.setBackgroundResource( R.drawable.bg_item_gray );
        }

        return itemView;
    }
}
