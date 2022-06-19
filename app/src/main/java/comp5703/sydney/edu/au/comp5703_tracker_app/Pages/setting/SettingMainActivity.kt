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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Child
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage.MainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage.InvitedParentActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import java.lang.Exception
import java.lang.NumberFormatException

class SettingMainActivity: AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    //lateinit var gooleAuth: GoogleSignInClient
    var databaseRefKid: DatabaseReference? = null
    var databaseRefParent: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    lateinit var listOfRole: ListView

    var mylist: ArrayList<Child> = ArrayList()
    var childIdList: ArrayList<String> = ArrayList()
    var childNameList: ArrayList<String> = ArrayList()
    //var myAdapter: ArrayAdapter<String>? = null
    var myAdapter: ChildrenListAdapter? = null

    var sessionManager: SessionManager? = null
    var creatorId: String = String()

    /* Bottom navigation-> reused part for each activity*/
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_setting -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_person -> {
                val intent = Intent(this@SettingMainActivity, ParentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_home -> {
                if(FirebaseAuth.getInstance().currentUser != null) {
                    val intent = Intent(this@SettingMainActivity, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                }
                else{
                    val intent = Intent(this@SettingMainActivity, InvitedParentActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingMainActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@SettingMainActivity, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_setting_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_setting
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

        sessionManager = SessionManager(applicationContext)

        //sessionManager!!.id?.let { Log.d("ID", it) }

        listOfRole = findViewById(R.id.listOfRole)
        myAdapter = ChildrenListAdapter(this, R.layout.setting_child_list_item, mylist)

        /*create database instance and firebase auth*/
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        databaseRefKid = database?.reference!!.child("Children")
        databaseRefParent = database?.reference!!.child("Parent")

        //var radioButtonValue: String? = "Parent"
        if(auth.currentUser == null){
            creatorId = sessionManager?.creatorID.toString()
            databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot){
                    if(dataSnapshot.hasChild(sessionManager?.id.toString())){
                        val intent = Intent(this@SettingMainActivity, SettingChildrenActivity::class.java)
                        intent.putExtra("id", sessionManager?.id.toString())
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    }
                }
                override fun onCancelled(error: DatabaseError){
                }
            })
        }else{
            creatorId = auth.currentUser?.uid.toString()
        }
        /*if(auth.currentUser == null){
            databaseRefParent!!.child(sessionManager?.id.toString()).child("creatorId").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    creatorId = snapshot.value.toString()
                }
                override fun onCancelled(error: DatabaseError){}
            })
        } */

        databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (creatorId == item.child("creatorId").value.toString()) {
                        var id = item.key.toString()
                        var name = item.child("username").value.toString()
                        //mylist.add("Child    (ID-$id):    $name")
                        mylist.add(Child(name, id))
                        //childIdList.add(id)
                        //childNameList.add(name)
                        myAdapter!!.notifyDataSetChanged()
                        listOfRole.adapter = myAdapter
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }

        })
        setUpListViewClickListener()
    }

    private fun setUpListViewClickListener(){
        listOfRole.setOnItemClickListener { parent, view, position, id ->
            //val element = myAdapter?.getItem(position) // The item that was clicked
            //val name = element!!.split(":")[1]
            //val childId = element!!.split('-')[1].split()
            val intent = Intent(this@SettingMainActivity, SettingParentToChildActivity::class.java)

            intent.putExtra("name", mylist[position].username)
            intent.putExtra("id", mylist[position].creatorId) //in this usage, it child id, not creatorId
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
    class ChildrenListAdapter(activity: Activity, val resourceId: Int, data: ArrayList<Child>): ArrayAdapter<Child>(activity, resourceId, data){
        fun updateProgress(current: String, max: String): Int{
            var c = 0.0
            var m = 0.0
            try{
                c = current.toDouble()
                m = max.toDouble()
            }
            catch (e: NumberFormatException){}

            if(c <= 0){
                return 0
            }
            else{
                if(m <= 0){
                    return 100
                }
                return (c/m * 100).toInt()
            }
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val role: TextView = view.findViewById(R.id.roleOfItem_child_setting)
            //val name: TextView = view.findViewById(R.id.nameOfItem_child_setting)
            val image: ImageView = view.findViewById(R.id.imageOfItem_child_setting)
            val progressBar: ProgressBar = view.findViewById(R.id.progressBar_setting_main)
            val progressText: TextView = view.findViewById(R.id.progressText_setting_main)
            val child = getItem(position)
            val database = FirebaseDatabase.getInstance()
            if(child != null){
                role.text = child.username
                //name.text = child
                image.setImageResource(R.drawable.child)

                val databaseRefCurrentScore = database.reference.child("Children").child(child.creatorId).child("score") //creatorId is childId
                databaseRefCurrentScore.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            val currentScore = snapshot.value.toString()
                            val maxScore = progressText.text.split("/")[1]
                            progressText.text = currentScore + "/" + maxScore
                            progressBar.progress = updateProgress(currentScore, maxScore)
                        }
                        catch (e: Exception){
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        //do nothing here
                    }
                })

                val databaseRefMaxScore = database.reference.child("Goal").child(child.creatorId) //creatorId is childId
                databaseRefMaxScore.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var maxScore = progressText.text.split("/")[1].toInt()
                        val currentScore = progressText.text.split("/")[0]
                        for(item in snapshot.children){
                            val s = item.child("score").value.toString().toInt()
                            if (maxScore < s){
                                maxScore = s
                            }
                        }
                        progressText.text = currentScore + "/" + maxScore
                        progressBar.progress = updateProgress(currentScore, maxScore.toString())
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