package br.com.smartstudyplan.provider.table;

/**
 * Esta interface é responsável por padronizar os métodos das tabelas.
 */
public interface TableInfo  {

    public String getColumnName();

    public String getColumnType();

    public String getTableName();

    public String getTableConstraint();

}
