package br.com.smartstudyplan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.DefaultAvailability;

/**
 * Esta classe é responsável por encapsular uma view da lista de disponibilidades.
 */
@EViewGroup( R.layout.list_item_availability )
public class AvailabilityItemView extends RelativeLayout {

    @ViewById TextView  name;
    @ViewById TextView  description;

    public AvailabilityItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AvailabilityItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvailabilityItemView(Context context) {
        super(context);
    }

    /**
     * Recebe as informações a serem exibidas na <code>View</code>.
     *
     * @param availability o obejeto que contém as informações
     */
    public void bindView( DefaultAvailability availability ){
        clear();

        name.setText( availability.getName() );
        description.setText( availability.getDescription() );
    }

    /**
     * Define a <code>View</code> para o estado inicial.
     */
    private void clear(){
        name.setText( "" );
        description.setText( "" );
    }
}
