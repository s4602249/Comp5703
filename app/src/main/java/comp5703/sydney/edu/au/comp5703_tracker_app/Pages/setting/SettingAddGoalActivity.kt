package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Goal
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Record
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import java.lang.Exception
import java.lang.NumberFormatException

open class SettingAddGoalActivity: AppCompatActivity()  {

    lateinit var title: TextView
    lateinit var warnlabel: TextView
    lateinit var goalTitle: EditText
    lateinit var goalScore: EditText
    lateinit var goalComment: EditText
    lateinit var backButton: Button
    lateinit var confirmButton: Button
    var record: Record? = null

    lateinit var auth: FirebaseAuth
    var database: FirebaseDatabase? = null
    var databaseRef: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_add_goal)

        val id = intent.getStringExtra("id").toString()
        val name = intent.getStringExtra("name").toString()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference!!.child("Goal").child(id)

        title = findViewById(R.id.addGoalLabel)
        warnlabel = findViewById(R.id.textViewAddGoalWarn)
        goalTitle = findViewById(R.id.editTextGoalTitle)
        goalScore = findViewById(R.id.editTextAddGoalScore)
        goalComment = findViewById(R.id.editTextAddGoalComment)
        backButton = findViewById(R.id.buttonAddGoalBack)
        confirmButton = findViewById(R.id.buttonAddGoalConfirm)

        title.text = "Add Goal"

        backButton.setOnClickListener(){
            val intent = Intent(this@SettingAddGoalActivity, SettingViewGoalActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        confirmButton.setOnClickListener(){
            try {
                val tempGoalTitle = goalTitle.editableText.toString().trim()
                //No empty input
                if(tempGoalTitle.isEmpty()){
                    throw AddGoalException("Title cannot be empty!")
                }
                //Avoid the database problem when store data in Firebase database and need to get some attributes.
                if(true == tempGoalTitle.contains('/')){
                    throw AddGoalException("Please do not include '/', '.', '#', '$', '[', or ']' in title!")
                }
                //It's same as the detection of database exception, but to make the logic as normal using
                if(true == tempGoalTitle.contains('.')){
                    throw AddGoalException("Please do not include '/', '.', '#', '$', '[', or ']' in title!")
                }
                if(true == tempGoalTitle.contains('#')){
                    throw AddGoalException("Please do not include '/', '.', '#', '$', '[', or ']' in title!")
                }
                if(true == tempGoalTitle.contains('$')){
                    throw AddGoalException("Please do not include '/', '.', '#', '$', '[', or ']' in title!")
                }
                if(true == tempGoalTitle.contains('[')){
                    throw AddGoalException("Please do not include '/', '.', '#', '$', '[', or ']' in title!")
                }
                if(true == tempGoalTitle.contains(']')){
                    throw AddGoalException("Please do not include '/', '.', '#', '$', '[', or ']' in title!")
                }
                if(tempGoalTitle.length>15){
                    throw AddGoalException("Title better not be too long! No more than 15 characters")
                }

                val g = Goal(id.toString().trim(),
                    goalTitle.editableText.toString().trim(),
                    goalScore.editableText.toString().toInt(),
                    goalComment.text.toString().trim())

                if(g.score <= 0 || g.score > 9999){
                    throw AddGoalException("Invalid Input for Score! Please input an integer that between 1~9999!")
                }
                if(g.comment.length>15){
                    throw AddGoalException("Better not to say too much~ No more than 15 characters in comment")
                }

                databaseRef?.child(g.title)!!.setValue(g)
                backButton.callOnClick()
            }
            catch (e: DatabaseException){
                warnlabel.text = "Please do not include '/', '.', '#', '$', '[', or ']' in title!"
            }
            catch (e: NumberFormatException){
                warnlabel.text = "Invalid Input for Score! Please input an integer that between 1~9999!"
            }
            catch (e:AddGoalException){
                warnlabel.text = e.message
            }
            catch (e: Exception){
                warnlabel.text = e.message
            }

        }


    }
    class AddGoalException(override val message: String?): Throwable(){}


}