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
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Goal
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage.MainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage.InvitedParentActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import java.lang.Exception
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.ArrayList

class SettingViewGoalActivity: AppCompatActivity() {

    lateinit var ownerName: TextView
    lateinit var buttonAddGoal: Button
    lateinit var buttonBack: Button
    lateinit var goalList: ListView

    lateinit var auth: FirebaseAuth
    var database: FirebaseDatabase? = null
    var databaseRef: DatabaseReference? = null
    var myAdapter: GoalAdapter? = null
    var mylist: ArrayList<Goal> = ArrayList()

    /* Bottom navigation-> reused part for each activity*/
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_setting -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_person -> {
                val intent = Intent(this@SettingViewGoalActivity, ParentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_home -> {
                if(FirebaseAuth.getInstance().currentUser != null) {
                    val intent = Intent(this@SettingViewGoalActivity, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                }
                else{
                    val intent = Intent(this@SettingViewGoalActivity, InvitedParentActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingViewGoalActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingViewGoalActivity, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_setting_view_goal)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_setting
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

        ownerName = findViewById(R.id.ownerName)
        buttonAddGoal = findViewById(R.id.buttonAddGoal)
        buttonBack = findViewById(R.id.buttonBack)
        goalList = findViewById(R.id.goalList)

        val name:String = intent.getStringExtra("name").toString()
        val id = intent.getStringExtra("id").toString()
        //ownerName.text = id

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference!!.child("Goal").child(id)
        myAdapter = GoalAdapter(this, R.layout.goal_list_item, mylist)

        buttonBack.setOnClickListener(){
            val intent = Intent(this@SettingViewGoalActivity, SettingParentToChildActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        buttonAddGoal.setOnClickListener(){
            val intent = Intent(this@SettingViewGoalActivity, SettingAddGoalActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        goalList.setOnItemLongClickListener { adapterView, view, position, l ->
            val targetGoal = myAdapter?.getItem(position)?.title
            val builder = AlertDialog.Builder(this@SettingViewGoalActivity)
            //val intent = Intent(this@SettingViewGoalActivity, Logout_sample::class.java)
            builder.setTitle("Delete the Goal").setMessage("Want to delete this Goal?")
                .setPositiveButton("Delete") { dialogInterface, i ->
                    mylist.removeAt(position)
                    myAdapter?.notifyDataSetChanged()
                    databaseRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (item in dataSnapshot.children) {
                                if (targetGoal == item.key.toString()) {
                                    item.ref.removeValue()
                                    Toast.makeText(
                                        this@SettingViewGoalActivity,
                                        "Goal successfully deleted",
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
        /*goalList.setOnItemClickListener { adapterView, view, i, l ->
            val targetGoal = myAdapter?.getItem(i).toString().split(" ")[0]
            val intent = Intent(this@SettingViewGoalActivity, SettingEditGoalActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("id", id)
            intent.putExtra("targetGoal", targetGoal)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }*/
        goalList.setOnItemClickListener { adapterView, view, position, l ->
            val targetGoal = myAdapter?.getItem(position)!!.title
            val builder = AlertDialog.Builder(this@SettingViewGoalActivity)
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
                                    if(originalScore >= score) {
                                        Toast.makeText(
                                            this@SettingViewGoalActivity,
                                            "Goal Exchange Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }else{
                                        Toast.makeText(
                                            this@SettingViewGoalActivity,
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

        databaseRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
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

    }

    class GoalAdapter(activity: Activity, val resourceId: Int, data: List<Goal>): ArrayAdapter<Goal>(activity, resourceId, data){
        fun updateProgress(current: String, gScore: String): Int{
            var c = 0.0
            var gscore = 0.0
            try{
                c = current.toDouble()
                gscore = gScore.toDouble()
            }
            catch (e: NumberFormatException){}

            if(c <= 0){
                return 0
            }
            else{
                if(gscore <= 0){
                    return 100
                }else if(c >= gscore){
                    return (c/c * 100).toInt()
                }else {
                    return (c/gscore * 100).toInt()
                }
            }
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val goalTitle: TextView = view.findViewById(R.id.goalListItemTitle)
            val goalScore: TextView = view.findViewById(R.id.goalListItemPts)
            val goalComment: TextView = view.findViewById(R.id.goalListItemComment)
            val settingButton: ImageView = view.findViewById(R.id.settingBtnGoalList)
            val image: ImageView = view.findViewById(R.id.goalListItemImage)
            val progressBar: ProgressBar = view.findViewById(R.id.progressBarSettingGoal)
            val progressText: TextView = view.findViewById(R.id.goalProgress)
            val database = FirebaseDatabase.getInstance()
            val goal = getItem(position)

            if(goal != null){
                goalTitle.text = goal.title
                goalScore.text = goal.score.toString()
                goalComment.text = goal.comment
                settingButton.setOnClickListener(){
                    context.startActivity(Intent(context, SettingEditGoalActivity::class.java).putExtra("id", goal.childId).putExtra("targetGoal", goal.title))
                }
                image.setImageDrawable(context.resources.getDrawable(R.drawable.target))

                val databaseRefCurrentScore = database.reference.child("Children").child(goal.childId).child("score")
                databaseRefCurrentScore.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            val currentScore = snapshot.value.toString()
                            val gScore = goal.score.toString()
                            progressText.text = currentScore + "/" + gScore
                            progressBar.progress = updateProgress(currentScore, gScore)
                        }
                        catch (e: Exception){
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        //do nothing here
                    }
                })


            }

            return view
        }
    }


}