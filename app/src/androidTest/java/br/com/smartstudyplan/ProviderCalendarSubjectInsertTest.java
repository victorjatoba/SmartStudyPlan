package br.com.smartstudyplan;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.provider.StudyPlanProvider;
import br.com.smartstudyplan.provider.table.CalendarSubjectTable;

/**
 * Created by Victor on 18/12/2014.
 *
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 *
 */
public class ProviderCalendarSubjectInsertTest extends ProviderTestCase2<StudyPlanProvider> {

    private MockContentResolver mMockContentResolver;

    private CalendarSubject mCalendarSubject;

    private final Uri mContentUri = StudyPlanProvider.CONTENT_URI_CALENDAR_SUBJECT;

    public ProviderCalendarSubjectInsertTest() {
        super(StudyPlanProvider.class, StudyPlanProvider.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockContentResolver = getMockContentResolver();

        Subject subject = new Subject();
        subject.setId(1);
        subject.setIcon(1);
        subject.setDifficult(50);
        subject.setWeight(79);
        subject.setName("Matematica");

        mCalendarSubject = new CalendarSubject();
        mCalendarSubject.setSubject( subject );
        mCalendarSubject.setDuration( 2.0f );
    }

    //Test add
    public void testInsertCalendarSubject() {
        ContentValues contentValues = mCalendarSubject.getContentValues();

        contentValues.put( CalendarSubjectTable.WEEKDAY.getColumnName(), Availability.Weekday.MONDAY.getValue());
        contentValues.put( CalendarSubjectTable.PERIOD.getColumnName(), Availability.Period.AFTERNOON.getValue());

        Uri uri = mMockContentResolver.insert(mContentUri, contentValues);
        long id = ContentUris.parseId(uri);
        assertEquals("insert_id", 1L, id);
    }
}
