package comp5703.sydney.edu.au.comp5703_tracker_app.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class Task {
    var imageId: Int = 1
    var childId: String = ""
    var title: String = "Task"
    var score: Int = 1
    //var timeLimit: Int = 0
    var timeLimit: Int ?= null
    var description: String = ""
    var completedTime: Date ?= null
    var valid: Boolean = true //False on DatabaseError

    constructor(childId: String, title: String){
        this.childId = childId
        this.title = title
        val database = FirebaseDatabase.getInstance()
        val databaseRef = database.getReference("Task").child(childId).child(title)
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                score = snapshot.child("score").value.toString().toInt()
                timeLimit = snapshot.child("timeLimit").value.toString().toInt()
                description = snapshot.child("description").value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                valid = false
            }

        })

    }

    constructor(childId: String, title: String, score: Int, description: String, completedTime: Date) {
        this.childId = childId
        this.title = title
        this.score = score
        this.description = description
        this.completedTime = completedTime
    }
    constructor(childId: String, title: String, score: Int, timeLimit: Int, description: String){
        this.childId = childId
        this.title = title
        this.score = score
        this.timeLimit = timeLimit
        this.description = description
    }
    constructor(childId: String, title: String, score: Int, timeLimit: Int, description: String, completedTime: Date){
        this.childId = childId
        this.title = title
        this.score = score
        this.timeLimit = timeLimit
        this.description = description
        this.completedTime = completedTime
    }

    /*
    constructor(childId: String, title: String, score: Int, timeLimit: Int, description: String, completedTime: Date){
        this.childId = childId
        this.title = title
        this.score = score
        this.timeLimit = timeLimit
        this.description = description
        this.completedTime = completedTime
    }

     */

    override fun toString(): String {
        return "Task(childId='$childId', title='$title', score=$score, timeLimit=$timeLimit, description='$description')"
    }

    // return a message to child
    fun submit(): String {
        if(!valid)
            return "Error on init the Task"  // DatabaseError happened, stop submitting

        //completedTime = Date(System.currentTimeMillis())
        val database = FirebaseDatabase.getInstance()
        val databaseRef = database.getReference("Task").child(childId).child(title)
        val databaseRefKid = database.getReference("Children").child(childId)
        val databaseRefRecord = database?.reference!!.child("Record")
        var record_time = Date(System.currentTimeMillis()).toString()


        var record = Record("Task","$title","$childId")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseRef.child("timeLimit").setValue(snapshot.child("timeLimit").getValue(true).toString().toInt() - 1)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        databaseRefKid.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseRefKid.child("score").setValue(snapshot.child("score").getValue(true).toString().toInt() + score)
                databaseRefKid.child("completedTask").child(completedTime?.time.toString()).setValue(Task(childId, title, score, description, completedTime as Date))
                databaseRefRecord.child(record_time).setValue(record)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        //println("Task(childId='$childId', title='$title', score=$score, timeLimit=$timeLimit, description='$description', completedTime='$completedTime‘)")
        //TODO("Submit to the database")
        /**
         * if (reach to the attempt limit of the week)
         *      return "Submitted Failed! This Task can not be submit more in this week"
         * **/

        return "Task successfully submitted" // Successfully submitted
    }

}
