package br.com.smartstudyplan.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import br.com.smartstudyplan.provider.table.CalendarSubjectTable;
import br.com.smartstudyplan.provider.table.SubjectTable;
import br.com.smartstudyplan.provider.table.TableInfo;
import br.com.smartstudyplan.util.SLog;

/**
 * Esta classe é responsável por gerar o banco de dados. Chamado pelo Android na primeira vez que
 * abre o aplicativo, ou quando o aplicativo é atualizado.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";

    private static final String DATABASE_NAME    = "smartstudyplan.db";
    private static final int    DATABASE_VERSION = 1;

    /**
     * Chamado pelo Android para criar o banco de dados.
     *
     * @param context o contexto da aplicação
     */
    DBHelper( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    /**
     * Cria as tabelas no banco de dados.
     *
     * @param db o banco de dados a ser preenchido
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        SLog.d(TAG, "Create database");

        db.execSQL( getCreationSQL( SubjectTable.values() ) );
        db.execSQL( getCreationSQL( CalendarSubjectTable.values() ) );
    }

    /**
     * Atualiza as tabelas no banco de dados.
     *
     * @param db o banco de dados a ser atualizado
     * @param oldVersion a antiga versão do app
     * @param newVersion a nova versão do app
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Método auxiliar para transformar uma lista de enum em uma tabela de banco de dados.
     *
     * @param tables as colunas das tabelas com os nomes e tipos
     * @return a query SQL para a criação das tabelas
     */
    private static String getCreationSQL( TableInfo[] tables ) {
        String tableConstraint = tables[0].getTableConstraint();
        StringBuilder creation = new StringBuilder();
        for ( TableInfo table : tables ) {
            creation.append( table.getColumnName() );
            creation.append( " " );
            creation.append( table.getColumnType() );
            creation.append( ", " );
        }

        if( TextUtils.isEmpty(tableConstraint) ){
            creation.deleteCharAt( creation.lastIndexOf( "," ) ).append( ")" );
        }
        else{
            creation.append( tableConstraint );
            creation.append( " )" );
        }

        return ( "CREATE TABLE " + tables[0].getTableName() + " ( " + creation.toString() );
    }
}
