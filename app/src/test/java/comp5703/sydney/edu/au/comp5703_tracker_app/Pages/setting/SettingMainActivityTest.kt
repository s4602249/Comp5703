package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting

import android.app.Activity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import org.junit.Assert.*
import org.junit.Test

class SettingMainActivityTest{
    @Test
    fun test_update_progress() {
        assertEquals(0, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("0","0"))
        assertEquals(100, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("1","0"))
        assertEquals(100, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("1","1"))
        assertEquals(50, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("50","100"))
        assertEquals(0, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("1","10000000000000"))
        assertTrue(SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("1000","10") > 100)
        assertEquals(0, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("-1","0"))
        assertEquals(0, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("-1","10"))
        assertEquals(0, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("-1","-1"))
        assertEquals(0, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("Other","String"))
        assertEquals(0, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("Other","10"))
        assertEquals(100, SettingMainActivity.ChildrenListAdapter(Activity(), R.layout.setting_child_list_item, ArrayList())
            .updateProgress("10","String"))

    }
}