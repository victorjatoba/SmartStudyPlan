package br.com.smartstudyplan;

import android.content.ContentUris;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.provider.StudyPlanProvider;

/**
 * Created by Victor on 18/12/2014.
 *
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 *
 */
public class ProviderSubjectInsertTest extends ProviderTestCase2<StudyPlanProvider> {

    private MockContentResolver mMockContentResolver;

    private Subject mSubject;

    private final Uri mContentUri = StudyPlanProvider.CONTENT_URI_SUBJECT;

    public ProviderSubjectInsertTest() {
        super(StudyPlanProvider.class, StudyPlanProvider.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMockContentResolver = getMockContentResolver();

        mSubject = new Subject();
        mSubject.setIcon(1);
        mSubject.setDifficult(50);
        mSubject.setWeight(79);
        mSubject.setName("Matematica");
    }

    //Test add
    public void testInsertSubject() {
        Uri uri = mMockContentResolver.insert(mContentUri, mSubject.getContentValues());
        long id = ContentUris.parseId(uri);
        assertEquals("insert_id", 1L, id);
    }
}
