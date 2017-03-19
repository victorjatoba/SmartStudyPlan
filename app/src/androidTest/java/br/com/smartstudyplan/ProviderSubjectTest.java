package br.com.smartstudyplan;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.provider.StudyPlanProvider;
import br.com.smartstudyplan.provider.table.SubjectTable;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ProviderSubjectTest extends ProviderTestCase2<StudyPlanProvider> {

    private MockContentResolver mMockContentResolver;

    private Subject mSubject;

    private final Uri mContentUri = StudyPlanProvider.CONTENT_URI_SUBJECT;

    public ProviderSubjectTest() {
        super(StudyPlanProvider.class, StudyPlanProvider.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockContentResolver = getMockContentResolver();

        mSubject = new Subject();
        mSubject.setIcon(5);
        mSubject.setDifficult(50);
        mSubject.setWeight(79);
        mSubject.setName("Matematica");

        Uri uri = mMockContentResolver.insert(mContentUri, mSubject.getContentValues());
        long id = ContentUris.parseId(uri);
        assertEquals("insert_id", 1L, id);
        mSubject.setId(id);
    }

    //Test get
    public void testQuerySubject() {

        Cursor cursor = mMockContentResolver.query(mContentUri,
                SubjectTable.SQL_SELECT,
                SubjectTable.WHERE_SUBJECT_ID,
                new String[]{String.valueOf(mSubject.getId())},
                null);

        assertNotNull("verify_query_cursor", cursor);
        assertEquals("count_cursor", 1, cursor.getCount());
        assertTrue("cursor_move_to_first", cursor.moveToFirst());
        assertEquals("cursor_verify_name", "Matematica", cursor.getString(cursor.getColumnIndex(SubjectTable.NAME.getColumnName())));
        assertEquals("cursor_verify_icon", mSubject.getIcon(), cursor.getInt(cursor.getColumnIndex(SubjectTable.ICON.getColumnName())));
        assertEquals("cursor_verify_difficult", mSubject.getDifficult(), cursor.getInt(cursor.getColumnIndex(SubjectTable.DIFFICULT.getColumnName())));
        assertEquals("cursor_verify_weight", mSubject.getWeight(), cursor.getInt(cursor.getColumnIndex(SubjectTable.WEIGHT.getColumnName())));

    }

    //Test update
    public void testUpdateSubject() {
        mSubject.setName("Historia");
        mSubject.setIcon(2);
        mSubject.setWeight(33);
        mSubject.setDifficult(29);

        int valueUpdate = mMockContentResolver.update(mContentUri,
                mSubject.getContentValues(),
                SubjectTable.WHERE_SUBJECT_ID,
                new String[]{String.valueOf(mSubject.getId())});

        assertEquals("verify_update_count", 1, valueUpdate);

        Cursor cursorUpdate = mMockContentResolver.query(mContentUri,
                SubjectTable.SQL_SELECT,
                SubjectTable.WHERE_SUBJECT_ID,
                new String[]{String.valueOf(mSubject.getId())},
                null);

        assertNotNull("verify_updated_cursor", cursorUpdate);
        assertEquals("count_updated_cursor", 1, cursorUpdate.getCount());
        assertTrue("cursor_updated_move_to_first", cursorUpdate.moveToFirst());
        assertEquals("cursor_updated_verify_name", "Historia", cursorUpdate.getString(cursorUpdate.getColumnIndex(SubjectTable.NAME.getColumnName())));
        assertEquals("cursor_updated_verify_icon", mSubject.getIcon(), cursorUpdate.getInt(cursorUpdate.getColumnIndex(SubjectTable.ICON.getColumnName())));
        assertEquals("cursor_updated_verify_difficult", mSubject.getDifficult(), cursorUpdate.getInt(cursorUpdate.getColumnIndex(SubjectTable.DIFFICULT.getColumnName())));
        assertEquals("cursor_updated_verify_weight", mSubject.getWeight(), cursorUpdate.getInt(cursorUpdate.getColumnIndex(SubjectTable.WEIGHT.getColumnName())));

    }

    //Test delete
    public void testDeleteSubject() {
        int valueRemove = mMockContentResolver.delete(mContentUri,
                SubjectTable.WHERE_SUBJECT_ID,
                new String[]{ String.valueOf(mSubject.getId()) } );

        assertEquals("verify_remove_count", 1, valueRemove);

        Cursor cursorRemove = mMockContentResolver.query(mContentUri,
                SubjectTable.SQL_SELECT,
                SubjectTable.WHERE_SUBJECT_ID,
                new String[]{ String.valueOf( mSubject.getId() ) },
                null);

        assertNotNull("verify_removed_cursor", cursorRemove);
        assertEquals("count_removed_cursor", 0,cursorRemove.getCount());
    }

}