package br.com.smartstudyplan.provider.table;

/**
 * Este enum contém as informações referentes a tabela de banco de dados de Subject.
 */
public enum SubjectTable implements TableInfo {

    _ID( "_id", "INTEGER PRIMARY KEY AUTOINCREMENT" ),
    NAME( "subject_name",           "VARCHAR NOT NULL" ),
    ICON( "subject_icon", 	        "INTEGER NOT NULL" ),
    WEIGHT( "subject_weight", 	    "INTEGER NOT NULL" ),
    DIFFICULT( "subject_difficult", "INTEGER NOT NULL" );

    public static final String TABLE = "subject";

    private final String mColumnName;
    private final String mColumnType;

    private SubjectTable(String columnName, String type) {
        mColumnName = columnName;
        mColumnType = type;
    }

    @Override
    public String getColumnName() {
        return mColumnName;
    }

    @Override
    public String getColumnType() {
        return mColumnType;
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String getTableConstraint() {
        return null;
    }

    public static final String WHERE_SUBJECT_ID = SubjectTable._ID.getColumnName() + " = ?";

    public static final String[] SQL_SELECT = new String[]{
            SubjectTable._ID.mColumnName,
            SubjectTable.NAME.mColumnName,
            SubjectTable.ICON.mColumnName,
            SubjectTable.WEIGHT.mColumnName,
            SubjectTable.DIFFICULT.mColumnName
    };
}
