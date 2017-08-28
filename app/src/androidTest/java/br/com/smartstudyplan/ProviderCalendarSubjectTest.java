package br.com.smartstudyplan;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.provider.StudyPlanProvider;
import br.com.smartstudyplan.provider.table.CalendarSubjectTable;
import br.com.smartstudyplan.provider.table.SubjectTable;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ProviderCalendarSubjectTest extends ProviderTestCase2<StudyPlanProvider> {

    private MockContentResolver mMockContentResolver;

    private CalendarSubject mCalendarSubject;

    private final Uri mContentUri = StudyPlanProvider.CONTENT_URI_CALENDAR_SUBJECT;

    public ProviderCalendarSubjectTest() {
        super(StudyPlanProvider.class, StudyPlanProvider.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockContentResolver = getMockContentResolver();

        Subject subject = new Subject();
        subject.setId(5);
        subject.setIcon(1);
        subject.setDifficult(50);
        subject.setWeight(79);
        subject.setName("Matematica");

        mCalendarSubject = new CalendarSubject();
        mCalendarSubject.setSubject( subject );
        mCalendarSubject.setDuration( 2.0f );

        ContentValues contentValues = mCalendarSubject.getContentValues();

        contentValues.put( CalendarSubjectTable.WEEKDAY.getColumnName(), Availability.Weekday.MONDAY.getValue());
        contentValues.put( CalendarSubjectTable.PERIOD.getColumnName(), Availability.Period.AFTERNOON.getValue());

        Uri uri = mMockContentResolver.insert(mContentUri, contentValues);
        long id = ContentUris.parseId(uri);
        assertEquals("insert_id", 1L, id);
        mCalendarSubject.setId(id);
    }

    //Test get
    public void testQuerySubject() {

        Cursor cursor = mMockContentResolver.query(mContentUri,
                CalendarSubjectTable.SQL_SELECT,
                CalendarSubjectTable.WHERE_WEEKDAY_AND_PERIOD,
                new String[]{Integer.toString(Availability.Weekday.MONDAY.getValue()),
                        Integer.toString(Availability.Period.AFTERNOON.getValue())},
                null);

        assertNotNull("verify_query_cursor", cursor);
        assertEquals("count_cursor", 1, cursor.getCount());
        assertTrue("cursor_move_to_first", cursor.moveToFirst());
        assertEquals("cursor_verify_subject_id", 5, cursor.getInt(cursor.getColumnIndex(CalendarSubjectTable.SUBJECT_ID.getColumnName())));
        assertEquals("cursor_verify_weekday", Availability.Weekday.MONDAY.getValue(), cursor.getInt(cursor.getColumnIndex(CalendarSubjectTable.WEEKDAY.getColumnName())));
        assertEquals("cursor_verify_period", Availability.Period.AFTERNOON.getValue(), cursor.getInt(cursor.getColumnIndex(CalendarSubjectTable.PERIOD.getColumnName())));
        assertEquals("cursor_verify_duration", 2.0f, cursor.getFloat(cursor.getColumnIndex(CalendarSubjectTable.TIME.getColumnName())));
    }

    //Test update
    public void testUpdateSubject() {
        mCalendarSubject.getSubject().setId(15);
        mCalendarSubject.setDuration(3.5f);

        ContentValues contentValues = mCalendarSubject.getContentValues();

        contentValues.put( CalendarSubjectTable.WEEKDAY.getColumnName(), Availability.Weekday.FRIDAY.getValue());
        contentValues.put( CalendarSubjectTable.PERIOD.getColumnName(), Availability.Period.MORNING.getValue());

//        Uri uri = mMockContentResolver.insert(mContentUri, contentValues);

        int valueUpdate = mMockContentResolver.update(mContentUri,
                contentValues,
                CalendarSubjectTable.WHERE_ID,
                new String[]{String.valueOf(mCalendarSubject.getId())});

        assertEquals("verify_update_count", 1, valueUpdate);

        Cursor cursorUpdate = mMockContentResolver.query(mContentUri,
                CalendarSubjectTable.SQL_SELECT,
                CalendarSubjectTable.WHERE_ID,
                new String[]{String.valueOf(mCalendarSubject.getId())},
                null);

        assertNotNull("verify_updated_cursor", cursorUpdate);
        assertEquals("count_updated_cursor", 1, cursorUpdate.getCount());
        assertTrue("cursor_updated_move_to_first", cursorUpdate.moveToFirst());
        assertEquals("cursor_verify_subject_id", 15, cursorUpdate.getInt(cursorUpdate.getColumnIndex(CalendarSubjectTable.SUBJECT_ID.getColumnName())));
        assertEquals("cursor_verify_weekday", Availability.Weekday.FRIDAY.getValue(), cursorUpdate.getInt(cursorUpdate.getColumnIndex(CalendarSubjectTable.WEEKDAY.getColumnName())));
        assertEquals("cursor_verify_period", Availability.Period.MORNING.getValue(), cursorUpdate.getInt(cursorUpdate.getColumnIndex(CalendarSubjectTable.PERIOD.getColumnName())));
        assertEquals("cursor_verify_duration", 3.5f, cursorUpdate.getFloat(cursorUpdate.getColumnIndex(CalendarSubjectTable.TIME.getColumnName())));

    }

    //Test delete
    public void testDeleteSubject() {
        int valueRemove = mMockContentResolver.delete(mContentUri,
                CalendarSubjectTable.WHERE_ID,
                new String[]{ String.valueOf(mCalendarSubject.getId()) } );

        assertEquals("verify_remove_count", 1, valueRemove);

        Cursor cursorRemove = mMockContentResolver.query(mContentUri,
                CalendarSubjectTable.SQL_SELECT,
                CalendarSubjectTable.WHERE_ID,
                new String[]{ String.valueOf( mCalendarSubject.getId() ) },
                null);

        assertNotNull("verify_removed_cursor", cursorRemove);
        assertEquals("count_removed_cursor", 0,cursorRemove.getCount());
    }

}