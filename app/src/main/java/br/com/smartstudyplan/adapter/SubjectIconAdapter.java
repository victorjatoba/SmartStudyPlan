package br.com.smartstudyplan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.dialog.SubjectIconListener;

/**
 * Este adapter é chamado pela tela SubjectAddDialog e é responsável por gerar a lista de ícones
 * a ser mostrado na tela, a partir da lista de ícones informada.
 */
public class SubjectIconAdapter extends BaseAdapter {

    private SubjectIconListener mListener;

    private final LayoutInflater li;

    public SubjectIconAdapter(Context context, SubjectIconListener listener){
        mListener = listener;
        li = LayoutInflater.from(context);
    }

    public int getCount() {
        return Subject.iconIds.length;
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null ){
            convertView = li.inflate( R.layout.item_subject_icon, null );
            holder = new ViewHolder();

            holder.icon = (ImageView) convertView.findViewById( R.id.icon );

            convertView.setTag( holder );
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.icon.setImageResource(Subject.iconIds[position]);

        convertView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setSelected(position);
                notifyDataSetChanged();
            }
        });

        if( mListener.getSelected() == position ){
            convertView.setBackgroundResource( R.drawable.icon_selector );
        }
        else{
            convertView.setBackgroundColor( Color.TRANSPARENT );
        }

        return convertView;
    }

    private class ViewHolder {
       public ImageView icon;
    }

}
