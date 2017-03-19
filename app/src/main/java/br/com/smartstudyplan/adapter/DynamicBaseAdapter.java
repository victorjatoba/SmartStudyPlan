package br.com.smartstudyplan.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que faz um wrapper do BaseAdapter e adiciona uma lista genérica de objetos. Facilita adicionar
 * elementos na lista a qualquer momento.
 */
@EBean
public abstract class DynamicBaseAdapter extends BaseAdapter {

    @RootContext
    Context mContext;

    private final List<Object> mList;

    public DynamicBaseAdapter( ){
        mList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return ( mList.size() >= position ) ? mList.get( position ) : null;
    }

    @Override
    public long getItemId( int position ) {
        return position;
    }

    /**
     * Recebe a lista de elementos genéricos e solicita a lista para ser redesenhado.
     *
     * @param list Lista de elementos genéricos.
     */
    public void setContent( List<?> list ){
        mList.clear();

        if( ( list != null ) && ( list.isEmpty() == false ) ){
            mList.addAll( list );
        }

        notifyDataSetChanged();
    }

    public List<Object> getContentList(){
        return mList;
    }

}
