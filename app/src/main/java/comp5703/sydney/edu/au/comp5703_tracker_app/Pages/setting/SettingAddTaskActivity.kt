package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Task
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import java.lang.Exception
import java.lang.NumberFormatException

open class SettingAddTaskActivity: AppCompatActivity() {
    lateinit var title: TextView
    lateinit var warnlabel: TextView
    lateinit var taskTitle: EditText
    //lateinit var taskScore: EditText
    //lateinit var taskLimit: EditText
    lateinit var taskDescription: EditText
    lateinit var backButton: Button
    lateinit var confirmButton: Button
    lateinit var imageViewSelectBed1: ImageView
    lateinit var imageViewSelectListen2: ImageView
    lateinit var imageViewSelectPush3: ImageView
    lateinit var imageViewSelectSit4: ImageView
    lateinit var auth: FirebaseAuth
    var imageViewChoice = 1
    var database: FirebaseDatabase? = null
    var databaseRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_add_task)

        val id = intent.getStringExtra("id").toString()
        val name = intent.getStringExtra("name").toString()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference!!.child("Task").child(id)

        title = findViewById(R.id.addTaskLable)
        warnlabel = findViewById(R.id.textViewAddTaskWarn)
        taskTitle = findViewById(R.id.editTextTaskTitle)
        //taskScore = findViewById(R.id.editTextNumberTaskScore)
        //taskLimit = findViewById(R.id.editTextNumberWeekLimit)
        taskDescription = findViewById(R.id.editTextTextDescription)
        backButton = findViewById(R.id.buttonAddTaskCancel)
        confirmButton = findViewById(R.id.buttonAddTaskConfirm)

        imageViewSelectClick()

        //title.text = "Add Task for $name($id)"
        backButton.setOnClickListener(){
            val intent = Intent(this@SettingAddTaskActivity, SettingParentToChildActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        confirmButton.setOnClickListener(){
            try {
                val t = Task(id.toString().trim(),
                    taskTitle.editableText.toString().trim(),
                    //taskScore.editableText.toString().toInt(),
                    1,
                    //taskLimit.editableText.toString().toInt(),
                    0,
                    taskDescription.text.toString().trim())
                //Log.i("Task", t.toString())
                if(t.title.isEmpty()){
                    throw AddTaskException("Title cannot be empty!")
                }
                if(t.title.contains("/")){
                    throw AddTaskException("Please do not include '.', '/', '#', '$', '[', or ']'")
                }
                t.imageId = imageViewChoice
                databaseRef?.child(t.title)!!.setValue(t)
                backButton.callOnClick()
            }
            catch (e:NumberFormatException){
                warnlabel.text = "Invalid Input!"
            }
            catch (e:DatabaseException){
                warnlabel.text = "Please do not include '.', '/', '#', '$', '[', or ']'"
            }
            catch (e:AddTaskException){
                warnlabel.text = e.message
            }
            catch (e: Exception){
                warnlabel.text = e.message
            }

        }


    }

    private fun imageViewSelectClick(){
        imageViewSelectBed1 = findViewById(R.id.imageViewSelectBed1)
        imageViewSelectListen2 = findViewById(R.id.imageViewSelectListen2)
        imageViewSelectPush3 = findViewById(R.id.imageViewSelectPush3)
        imageViewSelectSit4 = findViewById(R.id.imageViewSelectSit4)

        imageViewSelectBed1.setOnClickListener(){
            imageViewChoice = 1
            imageViewSelectBed1.background = resources.getDrawable(R.drawable.btn_dark_circle)
            imageViewSelectListen2.background = null
            imageViewSelectPush3.background = null
            imageViewSelectSit4.background = null
        }
        imageViewSelectListen2.setOnClickListener(){
            imageViewChoice = 2
            imageViewSelectBed1.background = null
            imageViewSelectListen2.background = resources.getDrawable(R.drawable.btn_dark_circle)
            imageViewSelectPush3.background = null
            imageViewSelectSit4.background = null
        }
        imageViewSelectPush3.setOnClickListener(){
            imageViewChoice = 3
            imageViewSelectBed1.background = null
            imageViewSelectListen2.background = null
            imageViewSelectPush3.background = resources.getDrawable(R.drawable.btn_dark_circle)
            imageViewSelectSit4.background = null
        }
        imageViewSelectSit4.setOnClickListener(){
            imageViewChoice = 4
            imageViewSelectBed1.background = null
            imageViewSelectListen2.background = null
            imageViewSelectPush3.background = null
            imageViewSelectSit4.background = resources.getDrawable(R.drawable.btn_dark_circle)
        }
    }

    class AddTaskException(override val message: String?): Throwable(){}
}