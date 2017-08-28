package br.com.smartstudyplan.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import br.com.smartstudyplan.provider.table.CalendarSubjectTable;
import br.com.smartstudyplan.provider.table.SubjectTable;
import br.com.smartstudyplan.util.SLog;

/**
 * Esta classe é responsável por gerar o content provider. Chamado pelo Android na primeira vez que
 * abre o aplicativo, ou quando o aplicativo é atualizado.
 */
public class StudyPlanProvider extends ContentProvider {
    private static final String TAG = "StudyPlanProvider";

    private static final String SUBJECT_WORD          = "subject";
    private static final String CALENDAR_SUBJECT_WORD = "calendar_subject";

    public static final String AUTHORITY = "br.com.smartstudyplan.provider";

    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final int PRIMARY_KEY_TYPE_INTEGER 	= 0;
    private static final int PRIMARY_KEY_TYPE_VARCHAR 	= 1;

    public static final Uri CONTENT_URI_SUBJECT          = Uri.withAppendedPath( CONTENT_URI, "/" + SUBJECT_WORD);
    public static final Uri CONTENT_URI_CALENDAR_SUBJECT = Uri.withAppendedPath( CONTENT_URI, "/" + CALENDAR_SUBJECT_WORD);

    public static final int SUBJECT           = 1;
    public static final int SUBJECTS          = 2;
    public static final int CALENDAR_SUBJECT  = 3;
    public static final int CALENDAR_SUBJECTS = 4;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher( UriMatcher.NO_MATCH );
        uriMatcher.addURI( AUTHORITY, SUBJECT_WORD + "/#", SUBJECT );
        uriMatcher.addURI( AUTHORITY, SUBJECT_WORD ,   	   SUBJECTS );
        uriMatcher.addURI( AUTHORITY, CALENDAR_SUBJECT_WORD + "/#", CALENDAR_SUBJECT );
        uriMatcher.addURI( AUTHORITY, CALENDAR_SUBJECT_WORD ,   	CALENDAR_SUBJECTS );
    }

    private static final String CONTENT_TYPE_SUBJECT  = "vnd.android.cursor.item/br.com.smartstudyplan.provider." + SUBJECT_WORD;
    private static final String CONTENT_TYPE_SUBJECTS = "vnd.android.cursor.dir/br.com.smartstudyplan.provider."  + SUBJECT_WORD;
    private static final String CONTENT_TYPE_CALENDAR_SUBJECT  = "vnd.android.cursor.item/br.com.smartstudyplan.provider." + CALENDAR_SUBJECT_WORD;
    private static final String CONTENT_TYPE_CALENDAR_SUBJECTS = "vnd.android.cursor.dir/br.com.smartstudyplan.provider."  + CALENDAR_SUBJECT_WORD;

    private static DBHelper mHelper;

    @Override
    public boolean onCreate() {
        SLog.d(TAG, "Create content provider");
        mHelper = new DBHelper( getContext() );
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db             = mHelper.getReadableDatabase();
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();

        String tableName        = "";
        String primaryKeyColumn = "";

        switch (uriMatcher.match(uri)) {
            case SUBJECT:
                primaryKeyColumn = SubjectTable._ID.getColumnName();
            case SUBJECTS:
                tableName = SubjectTable.TABLE;
                break;
            case CALENDAR_SUBJECT:
                primaryKeyColumn = CalendarSubjectTable._ID.getColumnName();
            case CALENDAR_SUBJECTS:
                tableName = CalendarSubjectTable.TABLE;
                break;
        }

        sqlBuilder.setTables(tableName);

        if (primaryKeyColumn != null) {
            if( uri.getPathSegments().size() > 1 ){
                sqlBuilder.appendWhere(primaryKeyColumn + " = " + uri.getPathSegments().get(1));
            }
        }

        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        String result;
        switch ( uriMatcher.match( uri ) ) {
            case SUBJECT:
                result = CONTENT_TYPE_SUBJECT;
                break;
            case SUBJECTS:
                result = CONTENT_TYPE_SUBJECTS;
                break;
            case CALENDAR_SUBJECT:
                result = CONTENT_TYPE_CALENDAR_SUBJECT;
                break;
            case CALENDAR_SUBJECTS:
                result = CONTENT_TYPE_CALENDAR_SUBJECTS;
                break;
            default:
                throw new IllegalArgumentException( "Could not match the URI" );
        }

        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        switch ( uriMatcher.match( uri ) ) {
            case SUBJECT:
            case SUBJECTS:
                tableName = SubjectTable.TABLE;
                break;
            case CALENDAR_SUBJECT:
            case CALENDAR_SUBJECTS:
                tableName = CalendarSubjectTable.TABLE;
                break;
            default:
                throw new IllegalArgumentException( "Could not match the URI" );
        }

        long rowId = db.insert( tableName, "", values );
        if ( rowId > 0 ) {
            Uri newUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange( newUri, null );
            return newUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result;
        String tableName;
        String primaryKeyColumn = null;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SUBJECT:
                primaryKeyColumn = SubjectTable._ID.getColumnName();
            case SUBJECTS:
                tableName = SubjectTable.TABLE;
                break;
            case CALENDAR_SUBJECT:
                primaryKeyColumn = CalendarSubjectTable._ID.getColumnName();
            case CALENDAR_SUBJECTS:
                tableName = CalendarSubjectTable.TABLE;
                break;
            default:
                throw new IllegalArgumentException("Could not match the URI");
        }

        if( primaryKeyColumn != null ){
            StringBuilder whereClause = new StringBuilder();
            whereClause.append( primaryKeyColumn + " = " + uri.getPathSegments().get( 1 ) );

            if ( TextUtils.isEmpty(selection) == false ) {
                whereClause.append( " AND (" + selection + ") " );
            }
            result = db.delete( tableName, whereClause.toString(), selectionArgs );
        }
        else{
            result = db.delete( tableName, selection, selectionArgs );
        }
        getContext().getContentResolver().notifyChange( uri, null );

        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result;
        String tableName;
        String primaryKeyColumn = null;
        int primaryKeyType = PRIMARY_KEY_TYPE_INTEGER;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SUBJECT:
                primaryKeyColumn = SubjectTable._ID.getColumnName();
            case SUBJECTS:
                tableName = SubjectTable.TABLE;
                break;
            case CALENDAR_SUBJECT:
                primaryKeyColumn = CalendarSubjectTable._ID.getColumnName();
            case CALENDAR_SUBJECTS:
                tableName = CalendarSubjectTable.TABLE;
                break;
            default:
                throw new IllegalArgumentException("Could not match the URI");
        }

        if( primaryKeyColumn != null ){
            StringBuilder whereClause = new StringBuilder();
            if( primaryKeyType == PRIMARY_KEY_TYPE_INTEGER ){
                whereClause.append( primaryKeyColumn + " = " + uri.getPathSegments().get( 1 ) );
            }
            else{
                whereClause.append( primaryKeyColumn + " = '" + uri.getPathSegments().get( 1 ) + "'" );
            }

            if ( TextUtils.isEmpty( selection ) == false ) {
                whereClause.append( " AND (" + selection + ") " );
            }
            result = db.update( tableName, values, whereClause.toString(), selectionArgs );
        }
        else{
            result = db.update( tableName, values, selection, selectionArgs );
        }

        getContext().getContentResolver().notifyChange( uri, null );
        return result;
    }

    public Cursor rawQuery( String sql, String[] selectionArgs ){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db.rawQuery( sql, selectionArgs );
    }
}
