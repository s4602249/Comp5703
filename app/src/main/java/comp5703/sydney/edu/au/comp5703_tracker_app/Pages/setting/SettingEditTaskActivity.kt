package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting

import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import comp5703.sydney.edu.au.comp5703_tracker_app.R

class SettingEditTaskActivity: SettingAddTaskActivity() {
    lateinit var databaseRefTask: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getStringExtra("id").toString()
        //val name = intent.getStringExtra("name").toString()
        val targetTask = intent.getStringExtra("targetTask").toString()

        databaseRefTask = database?.reference!!.child("Task").child(id).child(targetTask)

        databaseRefTask.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //println(snapshot.child("title").value.toString())
                //taskScore.setText(snapshot.child("score").value.toString())
                //taskLimit.setText(snapshot.child("timeLimit").value.toString())
                taskDescription.setText(snapshot.child("description").value.toString())
                if(snapshot.child("imageId").value.toString() == "1"){
                    imageViewChoice = 1
                    imageViewSelectBed1.background = resources.getDrawable(R.drawable.btn_dark_circle)
                    imageViewSelectListen2.background = null
                    imageViewSelectPush3.background = null
                    imageViewSelectSit4.background = null
                }
                else if(snapshot.child("imageId").value.toString() == "2"){
                    imageViewChoice = 2
                    imageViewSelectBed1.background = null
                    imageViewSelectListen2.background = resources.getDrawable(R.drawable.btn_dark_circle)
                    imageViewSelectPush3.background = null
                    imageViewSelectSit4.background = null
                }
                else if(snapshot.child("imageId").value.toString() == "3"){
                    imageViewChoice = 3
                    imageViewSelectBed1.background = null
                    imageViewSelectListen2.background = null
                    imageViewSelectPush3.background = resources.getDrawable(R.drawable.btn_dark_circle)
                    imageViewSelectSit4.background = null
                }
                else if(snapshot.child("imageId").value.toString() == "4"){
                    imageViewChoice = 4
                    imageViewSelectBed1.background = null
                    imageViewSelectListen2.background = null
                    imageViewSelectPush3.background = null
                    imageViewSelectSit4.background = resources.getDrawable(R.drawable.btn_dark_circle)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        taskTitle.setText(targetTask)
        taskTitle.isEnabled = false
        taskTitle.isFocusable = false
        taskTitle.isFocusableInTouchMode = false


    }
}