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
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Goal
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage.InvitedChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import java.util.*
import kotlin.collections.ArrayList

class SettingChildrenGoalActivity: AppCompatActivity()  {
    var databaseRef: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    lateinit var goalList: ListView
    lateinit var goalPageTitle: TextView
    lateinit var buttonChildrenGoalBack: Button
    var mylist: ArrayList<Goal> = ArrayList()
    var myAdapter: GoalAdapter? = null

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_setting -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_person -> {
                val intent = Intent(this@SettingChildrenGoalActivity, ChildActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_home -> {
                val intent = Intent(this@SettingChildrenGoalActivity, InvitedChildActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }

            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingChildrenGoalActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingChildrenGoalActivity, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_setting_children_goal)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_setting
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

        goalPageTitle = findViewById(R.id.textViewChildrenGoalTitle)
        goalList = findViewById(R.id.listViewChildrenGoal)
        buttonChildrenGoalBack = findViewById(R.id.buttonChildrenGoalBack)
        myAdapter = GoalAdapter(this, R.layout.goal_list_item_children, mylist)

        val id = intent.getStringExtra("id").toString()
        val name:String = intent.getStringExtra("name").toString()

        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference?.child("Goal")?.child(id)

        goalPageTitle.text = "Your Goal"

        databaseRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    var title = item.key.toString()
                    var score = item.child("score").value.toString()
                    var comment = item.child("comment").value.toString()
                    var tempG = Goal(id, title, score.toInt(), comment)

                    mylist.add(tempG)
                    myAdapter!!.notifyDataSetChanged()
                    goalList.adapter = myAdapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }

        })
/*
       goalList.setOnItemClickListener { adapterView, view, position, l ->
            val targetGoal = myAdapter?.getItem(position).toString().split(" ")[0]
            val builder = AlertDialog.Builder(this@SettingChildrenGoalActivity)
            val databaseRefKid = database?.getReference("Children")?.child(id)
            var originalScore = 0

            databaseRefKid?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    originalScore = snapshot.child("score").getValue(true).toString().toInt()
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

            builder.setTitle("Submit a Goal").setMessage("Want to submit this Goal? Your score will be deducted.")
                .setPositiveButton("Submit") { dialogInterface, i ->
                    databaseRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (item in dataSnapshot.children) {
                                if (targetGoal == item.key.toString()) {
                                    val score = item.child("score").getValue(true).toString().toInt()
                                    val comment = item.child("comment").getValue(true).toString()
                                    val title = item.child("title").getValue(true).toString()

                                    val message = Goal(id, title, score, comment, Date()).submit()

                                    if(originalScore > score) {
                                        Toast.makeText(
                                            this@SettingChildrenGoalActivity,
                                            "Goal Exchange Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }else{
                                        Toast.makeText(
                                            this@SettingChildrenGoalActivity,
                                            "You have not achieved the target score",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
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
        }
        */

        buttonChildrenGoalBack.setOnClickListener(){
            val intent = Intent(this@SettingChildrenGoalActivity, SettingChildrenActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

    }

    class GoalAdapter(activity: Activity, val resourceId: Int, data: List<Goal>): ArrayAdapter<Goal>(activity, resourceId, data){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val goalTitle: TextView = view.findViewById(R.id.goalListChildItemTitle)
            val goalScore: TextView = view.findViewById(R.id.goalListChildItemPts)
            val goalComment: TextView = view.findViewById(R.id.goalListChildItemComment)
            val image: ImageView = view.findViewById(R.id.goalListChildItemImage)

            val goal = getItem(position)
            if(goal != null){
                goalTitle.text = goal.title
                goalScore.text = goal.score.toString()
                goalComment.text = goal.comment
                image.setImageDrawable(context.resources.getDrawable(R.drawable.target))

            }

            return view
        }
    }

}
