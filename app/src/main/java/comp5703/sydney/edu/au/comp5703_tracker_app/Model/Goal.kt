package comp5703.sydney.edu.au.comp5703_tracker_app.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class Goal {
    var childId: String = ""
    var title: String = "Goal"
    var score: Int = 1
    var comment: String = ""
    var completedTime: Date ?= null
    var valid: Boolean = true //False on DatabaseError

    constructor(childId: String, title: String){
        this.childId = childId
        this.title = title
        val database = FirebaseDatabase.getInstance()
        val databaseRef = database.getReference("Goal").child(childId).child(title)
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                score = snapshot.child("score").value.toString().toInt()
                comment = snapshot.child("comment").value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                valid = false
            }

        })

    }

    //constructor()
//    constructor(childId: String, title: String, comment: String){
//        this.childId = childId
//        this.title = title
//        this.comment = comment
//    }

    constructor(childId: String, title: String, score: Int, comment: String){
        this.childId = childId
        this.title = title
        this.score = score
        this.comment = comment
    }

    constructor(childId: String, title: String, score: Int, comment: String, completedTime: Date){
        this.childId = childId
        this.title = title
        this.score = score
        this.comment = comment
        this.completedTime = completedTime
    }

    fun submit(): String {
        if(!valid)
            return "Error on init the Goal"  // DatabaseError happened, stop submitting

        val database = FirebaseDatabase.getInstance()
        val databaseRefKid = database.getReference("Children").child(childId)

        val databaseRefRecord = database?.reference!!.child("Record")

        var record_time = Date(System.currentTimeMillis()).toString()


        var record = Record("Goal","$title","$childId")


        databaseRefKid.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("score").getValue(true).toString().toInt() >= score) {
                    databaseRefKid.child("score")
                        .setValue(snapshot.child("score").getValue(true).toString().toInt() - score)
                    databaseRefKid.child("completedGoal").child(completedTime?.time.toString())
                        .setValue(Goal(childId, title, score, comment, completedTime as Date))
                    databaseRefRecord.child(record_time).setValue(record)

                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        return ""

        //TODO("Submit to the database")
        /**
         * if (the score of child less than the score of the Goal)
         *      return "Submitted Failed! Your score is not enough to obtain the goal!"
         * **/

        // Successfully submitted
    }

    override fun toString(): String {
        return "Goal(childId='$childId', title='$title', score=$score, comment='$comment', completedTime=$completedTime, valid=$valid)"
    }


}