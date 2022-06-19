package comp5703.sydney.edu.au.comp5703_tracker_app.Util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

@SuppressLint("CommitPrefEdits")
class SessionManager(context: Context) {
    // initialize variable
    /*
    *android.content.SharedPreferences.Editor. Interface used for modifying values in a SharedPreferences object.
    * All changes you make in an editor are batched,
    * and not copied back to the original SharedPreferences until you call commit() or apply()*/
    //it should only be used for kids and parent
    var sharedPreferences: SharedPreferences
    var editor: Editor

    //Create constructor
    init {
        sharedPreferences = context.getSharedPreferences("Appkey", 0)
        editor = sharedPreferences.edit()
        editor.apply()
    }

    //Create set login method
    //Create get login method
    var login: Boolean
        get() = sharedPreferences.getBoolean("KEY_LOGIN", false)
        set(login) {
            editor.putBoolean("KEY_LOGIN", login)
            editor.commit()
        }

    //Create set username method
    //Create get username method
    var id: String?
        get() = sharedPreferences.getString("KEY_ID", "")
        set(id) {
            editor.putString("KEY_ID", id)
            editor.commit()
        }
    var creatorID: String?
        get() = sharedPreferences.getString("KEY_creatorID", "")
        set(creatorID) {
            editor.putString("KEY_creatorID", creatorID)
            editor.commit()
        }

}