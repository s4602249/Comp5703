package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting
import android.app.Activity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import org.junit.Assert.*
import org.junit.Test

class SettingViewGoalActivityTest {
    @Test
    fun test_update_progress() {
        assertEquals(0, SettingViewGoalActivity.GoalAdapter(Activity(), R.layout.goal_list_item, ArrayList())
            .updateProgress("0","0"))
        assertEquals(100, SettingViewGoalActivity.GoalAdapter(Activity(), R.layout.goal_list_item, ArrayList())
            .updateProgress("1","0"))
        assertEquals(100, SettingViewGoalActivity.GoalAdapter(Activity(), R.layout.goal_list_item, ArrayList())
            .updateProgress("1","1"))
        assertEquals(100, SettingViewGoalActivity.GoalAdapter(Activity(), R.layout.goal_list_item, ArrayList())
            .updateProgress("100","1"))
        assertEquals(10, SettingViewGoalActivity.GoalAdapter(Activity(), R.layout.goal_list_item, ArrayList())
            .updateProgress("10","100"))
        assertEquals(0, SettingViewGoalActivity.GoalAdapter(Activity(), R.layout.goal_list_item, ArrayList())
            .updateProgress("0","100"))
        //Try different input
        assertEquals(0, SettingViewGoalActivity.GoalAdapter(Activity(), R.layout.goal_list_item, ArrayList())
            .updateProgress("Input","GoalScore"))
        assertEquals(0, SettingViewGoalActivity.GoalAdapter(Activity(), R.layout.goal_list_item, ArrayList())
            .updateProgress("Input","1"))
        assertEquals(100, SettingViewGoalActivity.GoalAdapter(Activity(), R.layout.goal_list_item, ArrayList())
            .updateProgress("1","GoalScore"))

    }

}