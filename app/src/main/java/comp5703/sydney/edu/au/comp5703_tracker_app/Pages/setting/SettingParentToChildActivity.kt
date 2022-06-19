package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Task
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage.MainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage.InvitedParentActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import kotlinx.android.synthetic.main.activity_child_model.*
import kotlinx.android.synthetic.main.activity_parent.*
import kotlinx.android.synthetic.main.task_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class SettingParentToChildActivity: AppCompatActivity(){

    lateinit var lableText: TextView
    lateinit var backButton: Button
    lateinit var addTaskButton: Button
    lateinit var buttonGoalView: Button
    lateinit var taskList: ListView

    lateinit var auth: FirebaseAuth
    var database: FirebaseDatabase? = null
    var databaseRef: DatabaseReference? = null
    var myAdapter: TaskAdapter? = null
    var mylist: ArrayList<Task> = ArrayList()

    var choiceList: List<String> = listOf("Done - 1pt", "Good - 2pts", "Perfect - 3pts")
    //var adapter= GroupAdapter<ViewHolder>()

    /* Bottom navigation-> reused part for each activity*/
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_setting -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_person -> {
                val intent = Intent(this@SettingParentToChildActivity, ParentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_home -> {
                if(FirebaseAuth.getInstance().currentUser != null) {
                    val intent = Intent(this@SettingParentToChildActivity, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                }
                else{
                    val intent = Intent(this@SettingParentToChildActivity, InvitedParentActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingParentToChildActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingParentToChildActivity, NotifyActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_parent_to_child)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_setting
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

        lableText = findViewById(R.id.lableText)
        backButton = findViewById(R.id.backButton)
        addTaskButton = findViewById(R.id.addTaskButton)
        buttonGoalView = findViewById(R.id.buttonGoalView)
        taskList = findViewById(R.id.taskList)

        val name:String = intent.getStringExtra("name").toString()
        val id = intent.getStringExtra("id").toString()
        lableText.text = "Task List"

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference!!.child("Task").child(id)
        myAdapter = TaskAdapter(this, R.layout.task_list_item, mylist)

        backButton.setOnClickListener(){
            val intent = Intent(this@SettingParentToChildActivity, SettingMainActivity::class.java)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        taskList.setOnItemLongClickListener { adapterView, view, position, l ->
            val targetTask = myAdapter?.getItem(position)?.title
            val builder = AlertDialog.Builder(this@SettingParentToChildActivity)
            builder.setTitle("Delete a Task").setMessage("Do you want to delete this task?")
                .setPositiveButton("Delete") { dialogInterface, i ->
                    mylist.removeAt(position)
                    myAdapter?.notifyDataSetChanged()
                    databaseRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (item in dataSnapshot.children) {
                                if (targetTask == item.key.toString()) {
                                    item.ref.removeValue()
                                    Toast.makeText(
                                        this@SettingParentToChildActivity,
                                        "Task successfully deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    overridePendingTransition(0, 0)
                                    startActivity(intent)
                                    overridePendingTransition(0, 0)
                                    finish()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })
                }.setNegativeButton("Cancel") { dialogInterface, i ->
                    //Nothing happens here
                }
            builder.create().show()
            return@setOnItemLongClickListener (true)
        }
        /*taskList.setOnItemClickListener { adapterView, view, i, l ->
            val targetTask = myAdapter?.getItem(i)?.title
            val intent = Intent(this@SettingParentToChildActivity, SettingEditTaskActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("id", id)
            intent.putExtra("targetTask", targetTask)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }*/
        /*taskList.setOnItemClickListener { adapterView, view, position, l ->
            val targetTask = myAdapter?.getItem(position)!!.title
            val builder = AlertDialog.Builder(this@SettingParentToChildActivity)
            builder.setTitle("Submit a Task").setMessage("Do you want to submit this task?")
                .setPositiveButton("Submit") { dialogInterface, i ->
                    databaseRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (item in dataSnapshot.children) {
                                if (targetTask == item.key.toString()) {
                                    val score = item.child("score").getValue(true).toString().toInt()
                                    val timeLimit = item.child("timeLimit").getValue(true).toString().toInt()
                                    val title = item.child("title").getValue(true).toString()
                                    //val message = Task(id, targetTask).submit()
                                    val message = Task(id, targetTask, score, timeLimit, title, Date()).submit()
                                    Toast.makeText(
                                        this@SettingParentToChildActivity,
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    overridePendingTransition(0, 0)
                                    startActivity(intent)
                                    overridePendingTransition(0, 0)
                                    finish()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })
                }.setNegativeButton("Cancel") { dialogInterface, i ->
                    //Nothing happens here
                }
            builder.create().show()
        }*/

        taskList.setOnItemClickListener { adapterView, view, position, l ->
            val targetTask = myAdapter?.getItem(position)!!.title
            var singleChoiceIndex = 0
            AlertDialog.Builder(this)
                .setSingleChoiceItems(choiceList.toTypedArray(), singleChoiceIndex
                ) { _, which -> singleChoiceIndex = which }
                .setPositiveButton("Submit") { dialog, _ ->
                    databaseRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (item in dataSnapshot.children) {
                                if (targetTask == item.key.toString()) {
                                    val score = singleChoiceIndex + 1
                                    val timeLimit = item.child("timeLimit").getValue(true).toString().toInt()
                                    val title = item.child("title").getValue(true).toString()
                                    //val message = Task(id, targetTask).submit()
                                    val message = Task(id, targetTask, score, timeLimit, title, Date()).submit()
                                    Toast.makeText(
                                        this@SettingParentToChildActivity,
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    overridePendingTransition(0, 0)
                                    startActivity(intent)
                                    overridePendingTransition(0, 0)
                                    finish()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })
                    //Toast.makeText(this, "Score: " + score, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }.setNegativeButton("Cancel"){dialog, _ ->
                    //Do Nothing Here
                }.setTitle("Submit a Task")
                .show()

        }

        //Go to the Goal list page
        buttonGoalView.setOnClickListener(){
            val intent = Intent(this@SettingParentToChildActivity, SettingViewGoalActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        addTaskButton.setOnClickListener(){
            val intent = Intent(this@SettingParentToChildActivity, SettingAddTaskActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        databaseRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    var title = item.key.toString()
                    var score = item.child("score").value.toString()
                    var limit = item.child("timeLimit").value.toString()
                    var description = item.child("description").value.toString()
                    var imageId = item.child("imageId").value.toString()
                    //if (limit.equals("0"))
                    //    limit = "Unlimited"
                    //mylist.add("$title    Score: $score    attempt per week: $limit")
                    var t = Task(id, title, score.toInt(), limit.toInt(), description)
                    try {
                        t.imageId = imageId.toInt()
                    }
                    catch(e :NumberFormatException){}
                    mylist.add(t)
                    myAdapter!!.notifyDataSetChanged()
                    taskList.adapter = myAdapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }

        })

    }



    class TaskAdapter(activity: Activity, val resourceId: Int, data: List<Task>): ArrayAdapter<Task>(activity, resourceId, data){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val taskTitle: TextView = view.findViewById(R.id.taskListItemTitle)
            //val taskLimit: TextView = view.findViewById(R.id.taskListItemLimit)
            //val taskScore: TextView = view.findViewById(R.id.taskListItemPts)
            val taskDescription: TextView = view.findViewById(R.id.taskListItemDescription)
            val settingButton: ImageView = view.findViewById(R.id.settingBtnTaskList)
            val image: ImageView = view.findViewById(R.id.taskListItemImage)

            val task = getItem(position)
            if(task != null){
                taskTitle.text = task.title
                //if(task.score != 0){
                //    taskLimit.text = "remain attempt: " + task.timeLimit.toString()
                //}
                //taskScore.text = task.score.toString() + " pts"
                taskDescription.text = task.description
                settingButton.setOnClickListener(){
                    context.startActivity(Intent(context, SettingEditTaskActivity::class.java).putExtra("id", task.childId).putExtra("targetTask", task.title))
                }
                if(task.imageId == 1){
                    image.setImageDrawable(context.resources.getDrawable(R.drawable.bed))
                }
                else if(task.imageId == 2){
                    image.setImageDrawable(context.resources.getDrawable(R.drawable.listen))
                }
                else if(task.imageId == 3){
                    image.setImageDrawable(context.resources.getDrawable(R.drawable.push))
                }
                else if(task.imageId == 4){
                    image.setImageDrawable(context.resources.getDrawable(R.drawable.sit))
                }
            }

            return view
        }
    }
}