package br.com.smartstudyplan.bean;

import android.content.ContentValues;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.provider.table.SubjectTable;

/**
 * Esta classe encapsula as informações referentes à matéria.
 */
public class Subject {
    private long    id;
    private String  name;
    private int     icon;
    private int     weight;
    private int     difficult;

    public Subject(){ }

    public Subject( ec.app.aspga.bean.Subject subject ){
        id   = subject.getId();
        name = subject.getName();
    }

    public static Subject clone( Subject subject ){
        return new Subject( subject.getId(), subject.getName(), subject.getIcon(),
                subject.getWeight(), subject.getDifficult() );
    }

    public Subject(long id, String name, int icon, int weight, int difficult){
        this.setId(id);
        this.setName(name);
        this.setIcon(icon);
        this.setWeight(weight);
        this.setDifficult(difficult);
    }

    public long getId(){
        return id;
    }

    public void setId( long id ){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getWeight() {
        return weight;
    }

    /**
     * Define o peso da matéria, verificando se está dentro da faixa permitida: 0 mínimo e 100 máximo.
     *
     * @param weight o peso da matéria
     */
    public void setWeight(int weight) {
        if(weight < 0) {
            weight = 0;
        } else if(weight > 100) {
            weight = 100;
        }
        this.weight = weight;
    }

    public int getDifficult() {
        return difficult;
    }

    /**
     * Define a dificuldade da matéria, verificando se está dentro da faixa permitida: 0 mínimo e
     * 100 máximo.
     *
     * @param difficult a dificuldade
     */
    public void setDifficult(int difficult) {
        if(difficult < 0) {
            difficult = 0;
        } else if(difficult > 100) {
            difficult = 100;
        }
        this.difficult = difficult;
    }

    /**
     * Gera o arquivo com as informações desta classe para ser salvo no banco de dados.
     *
     * @return o <code>ContentValues</code> com as informações
     */
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(SubjectTable.NAME.getColumnName(),       name);
        values.put(SubjectTable.ICON.getColumnName(),       icon);
        values.put(SubjectTable.WEIGHT.getColumnName(),     weight);
        values.put(SubjectTable.DIFFICULT.getColumnName(),  difficult);

        return values;
    }

    public ec.app.aspga.bean.Subject getAspgaSubject(){
        ec.app.aspga.bean.Subject subject = new ec.app.aspga.bean.Subject();
        subject.setId( (int)id );
        subject.setName( name );
        subject.setDifficulty( (byte)( ( difficult + weight ) / 2 ) );

        return subject;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", weight=" + weight +
                ", difficult=" + difficult +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        return name.equals(subject.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Lista de ícones possíveis para a matéria.
     */
    public static final Integer[] iconIds = {
            R.drawable.ic_1, R.drawable.ic_2, R.drawable.ic_3, R.drawable.ic_4, R.drawable.ic_5,
            R.drawable.ic_6, R.drawable.ic_7, R.drawable.ic_8, R.drawable.ic_9, R.drawable.ic_10,
            R.drawable.ic_11, R.drawable.ic_12, R.drawable.ic_13, R.drawable.ic_14, R.drawable.ic_15,
            R.drawable.ic_16, R.drawable.ic_17, R.drawable.ic_18, R.drawable.ic_19, R.drawable.ic_20,
            R.drawable.ic_21, R.drawable.ic_22, R.drawable.ic_23, R.drawable.ic_24, R.drawable.ic_25,
            R.drawable.ic_26, R.drawable.ic_27, R.drawable.ic_28, R.drawable.ic_29, R.drawable.ic_30,
            R.drawable.ic_31, R.drawable.ic_32, R.drawable.ic_33, R.drawable.ic_34, R.drawable.ic_35,
            R.drawable.ic_36, R.drawable.ic_37, R.drawable.ic_38, R.drawable.ic_39, R.drawable.ic_40,
            R.drawable.ic_41, R.drawable.ic_42, R.drawable.ic_43, R.drawable.ic_44, R.drawable.ic_45,
            R.drawable.ic_46, R.drawable.ic_47, R.drawable.ic_48, R.drawable.ic_49, R.drawable.ic_50
    };
}
