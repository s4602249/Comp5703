package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Goal
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingChildrenActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import kotlinx.android.synthetic.main.activity_child.*
import kotlinx.android.synthetic.main.activity_reward_list_child.*
import kotlinx.android.synthetic.main.activity_show_click_child_info.*
import kotlinx.android.synthetic.main.milestone_done_model.view.*
import kotlinx.android.synthetic.main.milestone_done_model.view.milestone
import kotlinx.android.synthetic.main.milestone_not_done_model.view.*

//add "1" to each variable for the convenience (copy from zheng yu's activity)
class ShowClickChildInfo : AppCompatActivity() {
    var show_Name1: TextView?= null
    var control_Level1: TextView?= null
    var current_Score1: TextView?= null
    var reward1: TextView?= null
    var milestone_One1: TextView?= null
    var milestone_Two1: TextView?= null
    var milestone_Three1: TextView?= null
    var last_Score1: TextView?= null
    var last_Acitivity_Name1: TextView?= null
    var database: FirebaseDatabase? = null
    var databaseRefs: DatabaseReference? = null
    var sessionManager: SessionManager? = null
    var nextGoal: TextView?=null
    var lastCompletedGoal: TextView?=null
    var nextGoalShow: TextView?=null

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.ic_home -> {
                val intent = Intent(this@ShowClickChildInfo, InvitedChildActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_person -> {
                val intent = Intent(this@ShowClickChildInfo, ChildActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@ShowClickChildInfo, SettingChildrenActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }

            R.id.ic_notify -> {
                val intent = Intent(this@ShowClickChildInfo, NotifyActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@ShowClickChildInfo, Logout_sample::class.java)
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
        setContentView(R.layout.activity_show_click_child_info)
        database = FirebaseDatabase.getInstance()
        databaseRefs =database?.reference
        //sessionManager = SessionManager(applicationContext)

        val data:String = intent.getStringExtra("accountID").toString()
        val username:String = intent.getStringExtra("username").toString()
        getChildInfo(data)

       // getChildInfo(sessionManager!!.id.toString())

        //bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.home
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
    }
    private fun getChildInfo(id: String) {
        val bar = findViewById<ProgressBar>(R.id.progressbar1)
        show_Name1=findViewById(R.id.childNameShow1)
        //control_Level1=findViewById(R.id.controlLevel1)
        current_Score1=findViewById(R.id.childCurrentScore1)
        reward1=findViewById(R.id.reward1)
        nextGoal=findViewById(R.id.nextGoal1)
        nextGoalShow=findViewById(R.id.nextGoalShow)
        lastCompletedGoal=findViewById(R.id.lastCompletedGoal)
        last_Score1=findViewById(R.id.lastScore1)
        last_Acitivity_Name1=findViewById(R.id.lastActivityName1)
        databaseRefs!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("Children").hasChild(id)) {

                    //put the child data query here
                    var root: DataSnapshot = snapshot
                    var kid: DataSnapshot = root.child("Children").child(id)
                    show_Name1?.setText( kid.child("username").getValue(true).toString())
                    current_Score1?.setText(kid.child("score").getValue(true).toString())

                    getMilestoneInfo(id, kid, root)

                    getLastReward(intent.getStringExtra("accountID").toString())

                    getLastActivityInfo(id, kid)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun getMilestoneInfo(id: String, kid: DataSnapshot, root: DataSnapshot) {

        //for testing the functionality of the circular bar
        val childScore = kid.child("score").getValue(true).toString().toInt()

        var goalList:ArrayList<DataSnapshot> ?= ArrayList()
        var defaultGoalList:ArrayList<Goal> ?= ArrayList()
        var biggestGoal:DataSnapshot ?= null
        var nextGoalData:DataSnapshot ?= null
        defaultGoalList?.add(Goal(id, "$10", 30, ""))
        defaultGoalList?.add(Goal(id, "$20", 60, ""))
        defaultGoalList?.add(Goal(id, "$30", 100, ""))

        val childGoal = root.child("Goal")
        if(childGoal.hasChild(id)) {
            val adapter= GroupAdapter<ViewHolder>()
            for(item:DataSnapshot in childGoal.child(id).children) {
                goalList?.add(item)
            }
            goalList?.sortWith(compareBy{(it.child("score").getValue(true).toString().toInt())})
            findViewById<ProgressBar>(R.id.progressbar1)?.setProgress(
                childScore * 100 / goalList?.get(goalList.size - 1)?.child("score")?.getValue(true).toString().toInt(),
                false
            )

            for(item:DataSnapshot in goalList!!.iterator()) {
                if(childScore >= item.child("score").getValue(true).toString().toInt()) {
                    biggestGoal = item
                    adapter.add(
                        CompletedItem(
                            item.child("score").getValue(true).toString()
                        )
                    )
                }else{
                    adapter.add(
                        NotAchievedItem(
                            item.child("score").getValue(true).toString()
                        )
                    )
                }
            }

            if(biggestGoal != null) {
                findViewById<ImageView>(R.id.celebrate1)?.visibility = ImageView.VISIBLE
                control_Level1?.setText("Level: "+biggestGoal.child("comment")?.getValue(true).toString())
                reward1?.setText(biggestGoal.child("title").getValue(true).toString())
                //nextGoal?.setText(biggestGoal.child("comment")?.getValue(true).toString())
                nextGoalData = biggestGoal
                for(item:DataSnapshot in goalList!!.iterator()) {
                    if(item.child("score").getValue(true).toString().toInt() > biggestGoal?.child("score").getValue(true).toString().toInt()) {
                        nextGoalData = item
                        break
                    }
                }
                if(nextGoalData == biggestGoal) {
                    nextGoal?.setText("Biggest Goal Achieved!")
                }else{
                    nextGoal?.setText(nextGoalData?.child("title")?.getValue(true).toString())
                }
            } else {
                findViewById<ImageView>(R.id.celebrate1)?.visibility = ImageView.VISIBLE
                reward1?.setText("none")
                nextGoal?.setText(goalList.get(0).child("title")?.getValue(true).toString())
            }

           // milestoneList1.adapter = adapter

        } else {
            nextGoal?.setText("none")
            reward1?.setText("none")
        }

    }

    private fun getLastActivityInfo(id : String, kid: DataSnapshot) {

        //for testing the functionality of the last activity completed
        if (kid.child("completedTask").getValue(true) == null) {
            last_Score1?.setText("No completed task")
        } else {
            val databaseRefTask = databaseRefs?.child("Children")?.child(id)?.child("completedTask")
            var goal:DataSnapshot ?
            databaseRefTask!!.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    goal = snapshot.children.elementAt(0)
                    for(item:DataSnapshot in snapshot.children) {
                        if(item.child("completedTime").child("time").getValue(true).toString().toLong() >
                            goal?.child("completedTime")?.child("time")?.getValue(true).toString().toLong()) {
                            goal = item
                        }
                    }

                    last_Acitivity_Name1?.setText(goal?.child("title")?.getValue(true).toString())
                    val score = goal?.child("score")?.getValue(true).toString()
                    //last_Score1?.setText(score)

                    if (score.toInt() == -1 || score.toInt() == 1 || score.toInt() == 2 || score.toInt() == 3) {
                        if (score.toInt() == -1) {
                            last_Score1?.setText("Foul")
                        }
                        if (score.toInt() == 1) {
                            last_Score1?.setText("Free-throw")
                        }
                        if (score.toInt() == 2) {
                            last_Score1?.setText("Basket")
                        }
                        if (score.toInt() == 3) {
                            last_Score1?.setText("3-pointer")
                        }
                    } else {
                        last_Score1?.setText(score)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }

    private fun getLastReward(id: String) {

        databaseRefs!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("Children").hasChild(id)) {

                    var kid: DataSnapshot = snapshot.child("Children").child(id)

                    if (kid.child("completedGoal").getValue(true) != null) {
                        var completedTask: ArrayList<HashMap<String, String>> = ArrayList()
                        for (item: DataSnapshot in kid.child("completedGoal").children) {
                            val tempHashMap: HashMap<String, String> = HashMap()
                            tempHashMap.put("score", item.child("score").getValue(true).toString())
                            tempHashMap.put("title", item.child("title").getValue(true).toString())
                            tempHashMap.put(
                                "time",
                                item.child("completedTime").child("time").getValue(true).toString()
                            )
                            completedTask?.add(tempHashMap)
                        }
                        completedTask.sortWith(compareByDescending { (it.get("time")?.toLong()) })
                        reward1?.setText(completedTask.get(0).get("title").toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    //达到了就是这种情况
    class CompletedItem(val goal:String) : Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.milestone.text = goal
        }

        override fun getLayout(): Int {
            return R.layout.milestone_done_model
        }
    }

    //没达到就是这种情况
    class NotAchievedItem(val goal:String) : Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.milestone.text = goal
        }

        override fun getLayout(): Int {
            return R.layout.milestone_not_done_model
        }
    }

}