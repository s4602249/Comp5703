package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Task
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage.InvitedChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import java.util.*
import kotlin.collections.ArrayList

class SettingChildrenActivity: AppCompatActivity() {
    var databaseRef: DatabaseReference? = null
    var databaseRefChild: DatabaseReference? =null
    var database: FirebaseDatabase? = null

    lateinit var taskList: ListView
    lateinit var pageTitle: TextView
    lateinit var buttonChildrenViewGoal: Button
    lateinit var sessionManager: SessionManager
    var mylist: ArrayList<Task> = ArrayList()
    var myAdapter: TaskAdapter? = null

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_setting -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_person -> {
                val intent = Intent(this@SettingChildrenActivity, ChildActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_home -> {
                val intent = Intent(this@SettingChildrenActivity, InvitedChildActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }

            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingChildrenActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingChildrenActivity, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_setting_children)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_setting
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

        sessionManager = SessionManager(applicationContext)

        pageTitle = findViewById(R.id.textViewSettingChildrenTitle)
        taskList = findViewById(R.id.listViewChildrenTask)
        buttonChildrenViewGoal = findViewById(R.id.buttonChildrenViewGoal)
        myAdapter = TaskAdapter(this, R.layout.task_list_item_children, mylist)

        val id = sessionManager.id.toString()
        //val name:String = intent.getStringExtra("name").toString()

        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference?.child("Task")?.child(id)
        databaseRefChild = database?.reference?.child("Children")?.child(id)?.child("username")

        pageTitle.text = "Task List For "

        databaseRefChild?.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pageTitle.text = "Task List For " + snapshot.value
                println(pageTitle.text.toString().replaceFirst("Task List For ",""))
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        databaseRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    var title = item.key.toString()
                    var score = item.child("score").value.toString()
                    var limit = item.child("timeLimit").value.toString()
                    var description = item.child("description").value.toString()
                    var imageId = item.child("imageId").value.toString()
                    /*
                    if (limit.equals("0"))
                        limit = "Unlimited"

                     */

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

        /*taskList.setOnItemClickListener { adapterView, view, position, l ->
            val targetTask = myAdapter?.getItem(position)!!.title
            val builder = AlertDialog.Builder(this@SettingChildrenActivity)
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
                                    var t_title= title
                                    var record = Record("Task","$t_title","$id")
                                    record!!.submit()
                                    val message = Task(id, targetTask, score, timeLimit, title, Date()).submit()
                                    Toast.makeText(
                                        this@SettingChildrenActivity,
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

        buttonChildrenViewGoal.setOnClickListener(){
            val intent = Intent(this@SettingChildrenActivity, SettingChildrenGoalActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", pageTitle.text.toString().replaceFirst("Task List For ",""))
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }


    }

    private fun submitTask(t: Task){
        t.submit()
    }
    class TaskAdapter(activity: Activity, val resourceId: Int, data: List<Task>): ArrayAdapter<Task>(activity, resourceId, data){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val taskTitle: TextView = view.findViewById(R.id.taskListItemTitle)
            //val taskLimit: TextView = view.findViewById(R.id.taskListItemLimit)
            //val taskScore: TextView = view.findViewById(R.id.taskListItemPts)
            val taskDescription: TextView = view.findViewById(R.id.taskListItemDescription)
            val image: ImageView = view.findViewById(R.id.taskListItemImage)

            val task = getItem(position)
            if(task != null){
                taskTitle.text = task.title
                //if(task.score != 0){
                //    taskLimit.text = "remain attempt: " + task.timeLimit.toString()
                //}
                //taskScore.text = task.score.toString() + " pts"
                taskDescription.text = task.description
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