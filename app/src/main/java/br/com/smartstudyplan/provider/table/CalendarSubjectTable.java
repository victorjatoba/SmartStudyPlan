package br.com.smartstudyplan.provider.table;

/**
 * Este enum contém as informações referentes a tabela de banco de dados de CalendarSubject.
 */
public enum CalendarSubjectTable implements TableInfo {
    _ID(        "_id", "INTEGER PRIMARY KEY AUTOINCREMENT" ),
    SUBJECT_ID( "subject_id", "INTEGER NOT NULL" ),
    WEEKDAY(    "weekday", 	  "INTEGER NOT NULL" ),
    PERIOD(     "period", 	  "INTEGER NOT NULL" ),
    TIME(       "time",       "INTEGER NOT NULL" );

    public static final String TABLE = "calendar_subject";

    private final String mColumnName;
    private final String mColumnType;

    private CalendarSubjectTable(String columnName, String type) {
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

    public static final String WHERE_ID = CalendarSubjectTable._ID.getColumnName() + " = ?";
    public static final String WHERE_WEEKDAY_AND_PERIOD
            = CalendarSubjectTable.WEEKDAY.getColumnName() + " = ? AND " +
              CalendarSubjectTable.PERIOD.getColumnName() + " = ?";
    public static final String WHERE_SUBJECT_ID = CalendarSubjectTable.SUBJECT_ID.getColumnName() + " = ?";

    public static final String[] SQL_SELECT = new String[]{
            CalendarSubjectTable._ID.mColumnName,
            CalendarSubjectTable.SUBJECT_ID.mColumnName,
            CalendarSubjectTable.WEEKDAY.mColumnName,
            CalendarSubjectTable.PERIOD.mColumnName,
            CalendarSubjectTable.TIME.mColumnName
    };
}
