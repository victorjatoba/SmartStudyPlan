package br.com.smartstudyplan;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import java.util.prefs.Preferences;

import br.com.smartstudyplan.activity.EaseOfLearningActivity_;
import br.com.smartstudyplan.activity.settings.SettingsActivity_;
import br.com.smartstudyplan.bean.EaseOfLearning;
import br.com.smartstudyplan.manager.StudyPlanManager_;

/**
 * Created by Victor on 30/03/2015.
 *
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 *
 */
public class ConfigurationPersistenceTest extends ActivityInstrumentationTestCase2<SettingsActivity_> {

    private SharedPreferences mPreferences;

    public ConfigurationPersistenceTest() {
        super(SettingsActivity_.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(getInstrumentation().getTargetContext());
    }

    public void testNotificationOn(){
        mPreferences
                .edit().putBoolean("notification", true)
                .apply();

        assertTrue(mPreferences.getBoolean("notification", false));
    }

    public void testNotificationOff(){
        mPreferences
                .edit().putBoolean("notification", false)
                .apply();

        assertFalse(mPreferences.getBoolean("notification", true));
    }

}
