package br.com.smartstudyplan.provider.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.provider.StudyPlanProvider;
import br.com.smartstudyplan.provider.table.SubjectTable;
import br.com.smartstudyplan.util.SLog;

/**
 * Esta classe é responsável por realizar as operações do CRUD no content provider, referente ao
 * Subject.
 */
@EBean( scope = EBean.Scope.Singleton )
public class SubjectDAO {
    private static final String TAG = "SubjectDAO";

    @RootContext Context mContext;

    private ContentResolver mContentResolver;

    private final List<Subject> mSubjects;

    public SubjectDAO(){
        mSubjects = new ArrayList<>();
    }

    /**
     * Primeiro método a ser chamado após a instância da classe. Preenche a lista com
     * <code>Subject</code> retornado do content provider.
     */
    @AfterInject
    void init(){
        mContentResolver = mContext.getContentResolver();

        final List<Subject> subjects = findAll();
        if( subjects != null ){
            synchronized ( mSubjects ) {
                mSubjects.clear();
                mSubjects.addAll( subjects );
            }
        }
    }

    /**
     * Busca a lista completa de <code>Subject</code>.
     *
     * @return a lista de <code>Subject</code>
     */
    public List<Subject> getAll(){
        return mSubjects;
    }

    /**
     * Busca um <code>Subject</code> a partir do seu ID.
     *
     * @param id o ID do <code>Subject</code> que se busca
     * @return o <code>Subject</code> buscado
     */
    public Subject getById( long id ){
        synchronized (mSubjects){
            if( mSubjects.isEmpty() == false ){
                for (Subject subject : mSubjects){
                    if( subject.getIcon() == id ){
                        return subject;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Busca um <code>Subject</code> a partir do seu nome.
     *
     * @param name o nome do <code>Subject</code> que se busca
     * @return o <code>Subject</code> buscado
     */
    public Subject getByName( String name ){
        synchronized (mSubjects){
            if( mSubjects.isEmpty() == false ){
                for (Subject subject : mSubjects){
                    if( subject.getName().equals( name )){
                        return subject;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Salva um novo <code>Subject</code> no content provider.
     *
     * @param subject o <code>Subject</code> a ser salvo
     * @return <code>true</code> se feito com sucesso, <code>false</code> caso contrário
     */
    public boolean add( Subject subject ){
        if( subject.getId() > 0 ){
            return update( subject );
        }

        Uri newUri = mContentResolver
                .insert(StudyPlanProvider.CONTENT_URI_SUBJECT, subject.getContentValues());

        int id = 0;
        if( newUri != null ){
            id = Integer.valueOf(newUri.getLastPathSegment());
        }

        if( id > 0 ){
            subject.setId( id );
            mSubjects.add( subject );

            return true;
        }

        return false;
    }

    /**
     * Salva uma lista de <code>Subject</code> no content provider.
     *
     * @param subjectList a lista de <code>Subject</code> a ser salva
     * @return <code>true</code> se feito com sucesso, <code>false</code> caso contrário
     */
    public boolean addAll( List<Subject> subjectList ){
        boolean result = true;
        for( Subject subject : subjectList ){
            if( subject.getId() == 0 ){
                result &= add( subject );
            }
            else{
                result &= update( subject );
            }
        }

        return result;
    }

    /**
     * Atualiza um <code>Subject</code> no content provider.
     *
     * @param subject o <code>Subject</code> a ser atualizado
     * @return <code>true</code> se feito com sucesso, <code>false</code> caso contrário
     */
    public boolean update( Subject subject ){
        int rows = mContentResolver.update(StudyPlanProvider.CONTENT_URI_SUBJECT,
                subject.getContentValues(),
                SubjectTable.WHERE_SUBJECT_ID,
                new String[]{ String.valueOf( subject.getId() ) } );

        if( rows > 0 ){
            synchronized ( mSubjects ){
                int position = 0;
                for( int i = 0; i < mSubjects.size(); i++ ){
                    if( mSubjects.get(i).getId() == subject.getId() ){
                        position = i;
                        break;
                    }
                }

                mSubjects.remove( position );
                mSubjects.add( position, subject );

                return true;
            }
        }

        return false;
    }

    /**
     * Busca todos os <code>Subject</code> do content provider.
     *
     * @return a lista de <code>Subject</code>
     */
    private List<Subject> findAll() {
        List<Subject> items = null;
        final Cursor cursor = mContentResolver.query( StudyPlanProvider.CONTENT_URI_SUBJECT,
                SubjectTable.SQL_SELECT, null, null, null );

        if ( cursor != null ) {
            if ( cursor.moveToFirst() ) {

                items = new ArrayList<>( cursor.getCount() );

                SLog.d( TAG, "Loading all audits" );

                int idIndex        = cursor.getColumnIndex( SubjectTable._ID.getColumnName() );
                int nameIndex      = cursor.getColumnIndex( SubjectTable.NAME.getColumnName() );
                int iconIndex      = cursor.getColumnIndex(SubjectTable.ICON.getColumnName());
                int weightIndex    = cursor.getColumnIndex(SubjectTable.WEIGHT.getColumnName());
                int difficultIndex = cursor.getColumnIndex(SubjectTable.DIFFICULT.getColumnName());

                do{
                    final Subject subject = new Subject();

                    subject.setId( cursor.getLong( idIndex ) );
                    subject.setName( cursor.getString( nameIndex ) );
                    subject.setIcon(cursor.getInt(iconIndex));
                    subject.setWeight(cursor.getInt(weightIndex));
                    subject.setDifficult(cursor.getInt(difficultIndex));

                    items.add( subject );
                }
                while ( cursor.moveToNext() ) ;

            }
            cursor.close();
        }

        return items;
    }

    /**
     * Remove um <code>Subject</code> do content provider.
     *
     * @param subject o <code>Subject</code> a ser removido
     * @return <code>true</code> se feito com sucesso, <code>false</code> caso contrário
     */
    public boolean remove( Subject subject ){
        if( subject.getId() > 0 ){
            int deletedRows = mContentResolver.delete( StudyPlanProvider.CONTENT_URI_SUBJECT,
                    SubjectTable.WHERE_SUBJECT_ID,
                    new String[]{ String.valueOf( subject.getId() ) } );

            boolean isDeleted = deletedRows > 0;

            if( isDeleted ){
                synchronized (mSubjects){
                    mSubjects.remove(subject);
                }
            }

            return isDeleted;
        }

        return false;
    }

    /**
     * Remove todos os <code>Subject</code> do content provider.
     */
    public void removeAll() {
        int value = mContentResolver.delete( StudyPlanProvider.CONTENT_URI_SUBJECT, null, null );
        mSubjects.clear();
        SLog.d(TAG, "Deleted " + value + " subjects.");
    }

    /**
     * Este método é chamado quando fecha o aplicativo.
     */
    public void finish(){
        mSubjects.clear();
    }
}
