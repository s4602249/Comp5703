package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Goal
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage.InvitedChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingChildrenActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import kotlinx.android.synthetic.main.activity_add_parent_and_kids.view.*
import kotlinx.android.synthetic.main.activity_child.*
import kotlinx.android.synthetic.main.activity_child_model.view.*
import kotlinx.android.synthetic.main.milestone_done_model.view.*
import kotlinx.android.synthetic.main.milestone_done_model.view.milestone
import kotlinx.android.synthetic.main.milestone_none_text.view.*
import kotlinx.android.synthetic.main.milestone_not_done_model.view.*
import java.util.*
import kotlin.collections.ArrayList

class ChildActivity : AppCompatActivity() {

    var show_Name: TextView?= null
    var control_Level: TextView?= null
    var current_Score: TextView?= null
    var reward:TextView?= null
    var milestone_One: TextView?= null
    var milestone_Two: TextView?= null
    var milestone_Three: TextView?= null
    var gift_number: TextView?= null
    var task_completed_number: TextView?= null
    var last_Score: TextView?= null
    var last_Acitivity_Name: TextView?= null
    var show_All_Button : Button?= null
    var reward_List_Button : Button?= null
    var database: FirebaseDatabase? = null
    var databaseRefs: DatabaseReference? = null
    var sessionManager: SessionManager? = null

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_person -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_home -> {
                val intent = Intent(this@ChildActivity, InvitedChildActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@ChildActivity, SettingChildrenActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@ChildActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@ChildActivity, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_child)
        database = FirebaseDatabase.getInstance()
        databaseRefs =database?.reference

        sessionManager = SessionManager(applicationContext)

        getChildInfo(sessionManager!!.id.toString())

        //bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_person
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
    }

    private fun getChildInfo(id: String) {
        val bar = findViewById<ProgressBar>(R.id.progressbar)
        show_Name=findViewById(R.id.childNameShow)
        //control_Level=findViewById(R.id.controlLevel)
        current_Score=findViewById(R.id.childCurrentScore)
        reward=findViewById(R.id.reward)
        /*
        milestone_One=findViewById(R.id.milestoneOne)
        milestone_Two=findViewById(R.id.milestoneTwo)
        milestone_Three=findViewById(R.id.milestoneThree)

         */
        //last_Score=findViewById(R.id.lastScore)
        //last_Acitivity_Name=findViewById(R.id.lastActivityName)
        //task_completed_number=findViewById(R.id.taskCompleted)
        show_All_Button=findViewById(R.id.showAll)
        //gift_number=findViewById(R.id.giftGot)
        reward_List_Button=findViewById(R.id.rewardList)
        databaseRefs!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("Children").hasChild(id)) {

                    //put the child data query here
                    var root: DataSnapshot = snapshot
                    var kid: DataSnapshot = root.child("Children").child(id)
                    show_Name?.setText(kid.child("username").getValue(true).toString())
                    current_Score?.setText(kid.child("score").getValue(true).toString())

                    getMilestoneInfo(id, kid, root)

                    getLastActivityInfo(id, kid)

                    //gift_number?.setText(getGiftNumber(kid).toString())

                    //task_completed_number?.setText(getTaskCompletedNumber(kid).toString())

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        show_All_Button?.setOnClickListener() {
            val intent = Intent(this@ChildActivity, ChildActivityList::class.java)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        reward_List_Button?.setOnClickListener() {
            val intent = Intent(this@ChildActivity, ChildRewardList::class.java)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

    }

    private fun getMilestoneInfo(id: String, kid: DataSnapshot, root: DataSnapshot) {

        //for testing the functionality of the circular bar
        val childScore = kid.child("score").getValue(true).toString().toInt()

        var goalList:ArrayList<DataSnapshot> ?= ArrayList()
        var defaultGoalList:ArrayList<Goal> ?= ArrayList()
        var goalSet:Boolean ?= false
        var earnGoal:Boolean ?= false
        var biggestGoal:DataSnapshot ?= null
        defaultGoalList?.add(Goal(id, "$10", 30, ""))
        defaultGoalList?.add(Goal(id, "$20", 60, ""))
        defaultGoalList?.add(Goal(id, "$30", 100, ""))

        val childGoal = root.child("Goal")
        if(childGoal.hasChild(id)) {
            /*
            if (childGoal.child(id).childrenCount.toInt() == 3) {
                goalSet = true
                for(item:DataSnapshot in childGoal.child(id).children){
                    goalList?.add(item)
                }
                goalList?.sortWith(compareBy{(it.child("score").getValue(true).toString().toInt())})
                findViewById<ProgressBar>(R.id.progressbar)?.setProgress(
                    childScore.toInt() * 100 / goalList?.get(2)?.child("score")?.getValue(true).toString().toInt(),
                    false
                )
            } else {
                findViewById<ProgressBar>(R.id.progressbar)?.setProgress(
                    childScore.toInt() * 100 / defaultGoalList?.get(2)?.score.toString().toInt(),
                    false
                )
            }

             */
            val adapter= GroupAdapter<ViewHolder>()
            for(item:DataSnapshot in childGoal.child(id).children) {
                goalList?.add(item)
            }
            goalList?.sortWith(compareBy{(it.child("score").getValue(true).toString().toInt())})
            findViewById<ProgressBar>(R.id.progressbar)?.setProgress(
                childScore * 100 / goalList?.get(goalList.size - 1)?.child("score")?.getValue(true).toString().toInt(),
                false
            )

            for(item:DataSnapshot in goalList!!.iterator()) {
                if(childScore >= item.child("score").getValue(true).toString().toInt()) {
                    biggestGoal = item
                    adapter.add(CompletedItem(item.child("score").getValue(true).toString()))
                }else{
                    adapter.add(NotAchievedItem(item.child("score").getValue(true).toString()))
                }
            }

            if(biggestGoal != null) {
                //findViewById<ImageView>(R.id.celebrate)?.visibility = ImageView.VISIBLE
                //control_Level?.setText("Level: "+biggestGoal.child("comment")?.getValue(true).toString())
                reward?.setText(biggestGoal.child("title").getValue(true).toString())
            }else{
                //control_Level?.setText("Level: none")
                reward?.setText("none")
            }

            milestoneList.adapter = adapter

        } else {
            /*
            findViewById<ProgressBar>(R.id.progressbar)?.setProgress(
                childScore.toInt() * 100 / defaultGoalList?.get(2)?.score.toString().toInt(),
                false
            )

             */
            reward?.setText("none")
            val adapter= GroupAdapter<ViewHolder>()
            adapter.add(NoneText())
            milestoneList.adapter = adapter
        }

        //for testing the functionality of status image
        /*
        if(goalSet == true) {
            milestone_One?.setText(goalList?.get(0)?.child("score")?.getValue(true).toString())
            milestone_Two?.setText(goalList?.get(1)?.child("score")?.getValue().toString())
            milestone_Three?.setText(goalList?.get(2)?.child("score")?.getValue().toString())
            if (childScore >= goalList?.get(0)?.child("score")?.getValue(true).toString().toInt()) {
                findViewById<ImageView>(R.id.celebrate)?.visibility = ImageView.VISIBLE
                findViewById<ImageView>(R.id.completedOne)?.visibility = ImageView.VISIBLE
                reward?.setText((goalList?.get(0)?.child("title")?.getValue(true).toString()))
                control_Level?.setText("Level: "+goalList?.get(0)?.child("level")?.getValue(true).toString())
            } else {
                findViewById<ImageView>(R.id.notAchievedOne)?.visibility = ImageView.VISIBLE
            }

            if (childScore >= goalList?.get(1)?.child("score")?.getValue(true).toString().toInt()) {
                findViewById<ImageView>(R.id.celebrate)?.visibility = ImageView.VISIBLE
                findViewById<ImageView>(R.id.completedTwo)?.visibility = ImageView.VISIBLE
                reward?.setText((goalList?.get(1)?.child("title")?.getValue(true).toString()))
                control_Level?.setText("Level: "+goalList?.get(1)?.child("level")?.getValue(true).toString())
            } else {
                findViewById<ImageView>(R.id.notAchievedTwo)?.visibility = ImageView.VISIBLE
            }

            if (childScore >= goalList?.get(2)?.child("score")?.getValue(true).toString().toInt()) {
                findViewById<ImageView>(R.id.celebrate)?.visibility = ImageView.VISIBLE
                findViewById<ImageView>(R.id.completedThree)?.visibility = ImageView.VISIBLE
                reward?.setText((goalList?.get(2)?.child("title")?.getValue(true).toString()))
                control_Level?.setText("Level: "+goalList?.get(2)?.child("level")?.getValue(true).toString())
            } else {
                findViewById<ImageView>(R.id.notAchievedThree)?.visibility = ImageView.VISIBLE
            }
        } else {
            milestone_One?.setText(defaultGoalList?.get(0)?.score.toString())
            milestone_Two?.setText(defaultGoalList?.get(1)?.score.toString())
            milestone_Three?.setText(defaultGoalList?.get(2)?.score.toString())
            if (childScore >= defaultGoalList?.get(0)?.score as Int) {
                findViewById<ImageView>(R.id.celebrate)?.visibility = ImageView.VISIBLE
                findViewById<ImageView>(R.id.completedOne)?.visibility = ImageView.VISIBLE
                reward?.setText("$10")
                control_Level?.setText("Level: primary")
            } else {
                findViewById<ImageView>(R.id.notAchievedOne)?.visibility = ImageView.VISIBLE
            }

            if (childScore >= defaultGoalList?.get(1)?.score) {
                findViewById<ImageView>(R.id.celebrate)?.visibility = ImageView.VISIBLE
                findViewById<ImageView>(R.id.completedTwo)?.visibility = ImageView.VISIBLE
                reward?.setText("$20")
                control_Level?.setText("Level: intermediate")
            } else {
                findViewById<ImageView>(R.id.notAchievedTwo)?.visibility = ImageView.VISIBLE
            }

            if (childScore >= defaultGoalList?.get(2)?.score) {
                findViewById<ImageView>(R.id.celebrate)?.visibility = ImageView.VISIBLE
                findViewById<ImageView>(R.id.completedThree)?.visibility = ImageView.VISIBLE
                reward?.setText("$30")
                control_Level?.setText("Level: senior")
            } else {
                findViewById<ImageView>(R.id.notAchievedThree)?.visibility = ImageView.VISIBLE
            }
        }

         */
    }

    private fun getLastActivityInfo(id : String, kid: DataSnapshot) {

        //for testing the functionality of the last activity completed
        if (kid.child("completedTask").getValue(true) == null) {
            last_Score?.setText("No task completed yet")
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

                    last_Acitivity_Name?.setText(goal?.child("title")?.getValue(true).toString())

                    val score = goal?.child("score")?.getValue(true).toString()
                    /*
                    if (score.toInt() == -1) {
                        last_Score?.setText("Foul")
                    }
                    if (score.toInt() == 1) {
                        last_Score?.setText("Free-throw")
                    }
                    if (score.toInt() == 2) {
                        last_Score?.setText("Basket")
                    }
                    if (score.toInt() == 3) {
                        last_Score?.setText("3-pointer")
                    }

                     */
                    last_Score?.setText(score)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }

    /*
    private fun getGiftNumber(kid:DataSnapshot) : Int{
        return kid.child("completedTask").children.count()
    }

    private fun getTaskCompletedNumber(kid:DataSnapshot) : Int{
        return kid.child("completedGoal").children.count()
    }

     */

    class CompletedItem(val goal:String) : Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.milestone.text = goal
        }

        override fun getLayout(): Int {
            return R.layout.milestone_done_model
        }
    }

    class NotAchievedItem(val goal:String) : Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.milestone.text = goal
        }

        override fun getLayout(): Int {
            return R.layout.milestone_not_done_model
        }
    }

    class NoneText() : Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.noneText.text = "none"
        }

        override fun getLayout(): Int {
            return R.layout.milestone_none_text
        }
    }

}
