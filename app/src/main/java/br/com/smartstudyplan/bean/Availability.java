package br.com.smartstudyplan.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ec.app.aspga.bean.PeriodAvailable;

/**
 * Esta classe encapsula as informações referentes a disponibilidade de tempo para estudar em um
 * período semanal.
 */
public class Availability {

    private HashMap<Integer, List<Integer>> availabilityValues;

    public Availability(){
        availabilityValues = new HashMap<>(7);

        availabilityValues.put( Weekday.SUNDAY.getValue(),    new ArrayList<>(Collections.nCopies(3, 0)) );
        availabilityValues.put( Weekday.MONDAY.getValue(),    new ArrayList<>(Collections.nCopies(3, 0)) );
        availabilityValues.put( Weekday.TUESDAY.getValue(),   new ArrayList<>(Collections.nCopies(3, 0)) );
        availabilityValues.put( Weekday.WEDNESDAY.getValue(), new ArrayList<>(Collections.nCopies(3, 0)) );
        availabilityValues.put( Weekday.THURSDAY.getValue(),  new ArrayList<>(Collections.nCopies(3, 0)) );
        availabilityValues.put( Weekday.FRIDAY.getValue(),    new ArrayList<>(Collections.nCopies(3, 0)) );
        availabilityValues.put( Weekday.SATURDAY.getValue(),  new ArrayList<>(Collections.nCopies(3, 0)) );
    }

    /**
     * Este enum é responsável por manter um padrão para os dias da semana.
     */
    public enum Weekday {
        SUNDAY(0), MONDAY(1), TUESDAY(2), WEDNESDAY(3),
        THURSDAY(4), FRIDAY(5), SATURDAY(6);

        private final int value;

        Weekday( int value ){
            this.value = value;
        }

        public int getValue(){
            return value;
        }

        public static Weekday getWeekday( int value ){
            for( Weekday weekday : Weekday.values() ){
                if( weekday.getValue() == value ){
                    return weekday;
                }
            }

            return null;
        }
    }

    /**
     * Este enum é responsável por manter um padrão por períodos no dia.
     */
    public enum Period{
        MORNING(0), AFTERNOON(1), NIGHT(2);

        private final int value;

        Period( int value ){
            this.value = value;
        }

        public int getValue(){
            return value;
        }

        public static Period getPeriod( int value ){
            for( Period period : Period.values() ){
                if( period.getValue() == value ){
                    return period;
                }
            }

            return null;
        }
    }

    /**
     * Este enum é responsável por manter um padrão para disponibilidade de estudar no período.
     */
    public enum Available {
        NONE(0), PART_TIME(1), FULL_TIME(2);

        private final int value;

        Available( int value ){
            this.value = value;
        }

        public int getValue(){
            return value;
        }

        public static Available getAvailable( int value ){
            for( Available available : Available.values() ){
                if( available.getValue() == value ){
                    return available;
                }
            }

            return null;
        }
    }

    public void setValue( Weekday weekday, Period period, Available available ){
        availabilityValues
                .get( weekday.getValue() )
                .set( period.getValue(), available.getValue() );
    }

    public void setValues( HashMap<Integer, List<Integer>> values ){
        availabilityValues = values;
    }

    /**
     * Recebe uma lista de <code>Integer</code> e transforma no <code>HashMap</code> utilizado no
     * código.
     *
     * @param allValues a lista de <code>Integer</code>
     */
    public void setAllValues( List<Integer> allValues ){
        int i = 0;
        for( Weekday weekday : Weekday.values() ){
            for( Period period : Period.values() ){
                this.setValue(weekday, period, Available.getAvailable(allValues.get(i)));
                i++;
            }
        }
    }

    public Available getAvailable( Weekday weekday, Period period ){
        int value = availabilityValues.get( weekday.getValue() ).get( period.getValue() );
        return Available.getAvailable( value );
    }

    public HashMap<Integer, List<Integer>> getValues(){
        return availabilityValues;
    }

    public List<Integer> getValuesList(){
        List<Integer> values = new ArrayList<>();
        for( int i = 0; i < Weekday.values().length; i++ ){
            for( int a = 0; a < Period.values().length; a++ ){
                values.add( availabilityValues.get( i ).get(a) );
            }
        }

        return values;
    }

    public PeriodAvailable getAvailabilities(){
        PeriodAvailable periodAvailable = new PeriodAvailable();
        ec.app.aspga.bean.Period[] periods = new ec.app.aspga.bean.Period[Weekday.values().length];

        ec.app.aspga.bean.Period period;
        for( int i = 0; i < Weekday.values().length; i++ ){
            period = new ec.app.aspga.bean.Period();
            period.setMorning(getCorrectlyAvailability( availabilityValues.get( i ).get( Period.MORNING.getValue() ) ));
            period.setAfternoon(getCorrectlyAvailability( availabilityValues.get( i ).get( Period.AFTERNOON.getValue() ) ));
            period.setNight(getCorrectlyAvailability( availabilityValues.get( i ).get( Period.NIGHT.getValue() ) ));

            periods[ i ] = period;
        }

        periodAvailable.setStudyCycle( periods );

        return periodAvailable;
    }

    private char getCorrectlyAvailability( int localAvailability ){
        if( localAvailability == Availability.Available.PART_TIME.getValue() ){
            return 'S';
        }
        else if( localAvailability == Availability.Available.FULL_TIME.getValue() ){
            return 'B';
        }

        return 'N';
    }

    @Override
    public String toString() {
        return "Availability: " + availabilityValues.toString();
    }
}
