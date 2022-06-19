package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting

import android.os.Bundle
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import comp5703.sydney.edu.au.comp5703_tracker_app.R

class SettingEditGoalActivity: SettingAddGoalActivity() {
    lateinit var databaseRefGoal: DatabaseReference
    lateinit var title_edit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getStringExtra("id").toString()
        val name = intent.getStringExtra("name").toString()
        val targetGoal = intent.getStringExtra("targetGoal").toString()

        databaseRefGoal = database?.reference!!.child("Goal").child(id).child(targetGoal)

        goalTitle.setText(targetGoal)
        goalTitle.isEnabled = false
        goalTitle.isFocusable = false
        goalTitle.isFocusableInTouchMode = false

        title_edit = findViewById(R.id.addGoalLabel)
        title_edit.text = "Edit Goal for: $targetGoal"

        databaseRefGoal.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                goalScore.setText(snapshot.child("score").value.toString())
                goalComment.setText(snapshot.child("comment").value.toString())
            }
            override fun onCancelled(error: DatabaseError) {}
        })


    }

}