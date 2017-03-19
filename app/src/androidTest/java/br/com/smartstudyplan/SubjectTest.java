package br.com.smartstudyplan;

import android.test.AndroidTestCase;

import br.com.smartstudyplan.bean.Subject;

/**
 * Created by Victor on 22/12/2014.
 *
 *  <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 *
 */
public class SubjectTest extends AndroidTestCase {

    private Subject mSubject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSubject = new Subject();
    }

    public void testWeightLessThanZero(){
        mSubject.setWeight(-10);
        assertEquals(0, mSubject.getWeight());
    }

    public void testWeightMoreThanOneHundred(){
        mSubject.setWeight(200);
        assertEquals(100, mSubject.getWeight());
    }

    public void testDifficultLessThanZero(){
        mSubject.setDifficult(-10);
        assertEquals(0, mSubject.getDifficult());
    }

    public void testDifficultMoreThanOneHundred(){
        mSubject.setDifficult(200);
        assertEquals(100, mSubject.getDifficult());
    }
}
