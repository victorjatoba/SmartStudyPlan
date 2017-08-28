package br.com.smartstudyplan.dialog;

import br.com.smartstudyplan.bean.Availability;

/**
 * Interface auxiliar para o dialog <code>AvailabilityDialog</code>.
 */
public interface AvailabilityDialogListener {
    public void returnValues( Availability.Available available );
}
