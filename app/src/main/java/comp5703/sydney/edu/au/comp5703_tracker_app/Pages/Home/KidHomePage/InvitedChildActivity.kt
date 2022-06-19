package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.LoginActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity

import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingChildrenActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager

class InvitedChildActivity : AppCompatActivity() {

    var database: FirebaseDatabase? = null
    var databaseRefs: DatabaseReference? = null
    var databaseRefKid: DatabaseReference? = null

    var sessionManager: SessionManager? = null
    var creatorID: String? = null
    lateinit var ChildNam_txtview: TextView

    //clarify list view components
    lateinit var listOfRole: ListView
    var reslist: ArrayList<String> = ArrayList()
    var mylist: ArrayList<String> = ArrayList()
    var myAdapter: ArrayAdapter<String>? = null

    //Logout button
    lateinit var logoutbtn2: ImageButton
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_home -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_person -> {
                val intent = Intent(this@InvitedChildActivity, ChildActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
/*                personalPageIntent = if("Child" == intent.getStringExtra("Property")) {
                    Intent(this@InvitedChildActivity, ChildActivity::class.java)
                } else {
                    Intent(this@InvitedChildActivity, Logout_sample::class.java)
                }
                startActivity(personalPageIntent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)*/
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> {
                val intent = Intent(this@InvitedChildActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@InvitedChildActivity, NotifyActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@InvitedChildActivity, SettingChildrenActivity::class.java)
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
        setContentView(R.layout.activity_invited_child)
        //Get DB connection
        database = FirebaseDatabase.getInstance()
        databaseRefs = database?.reference
        databaseRefKid = database?.reference!!.child("Children")
        //Initialize SessionManager
        sessionManager = SessionManager(applicationContext)
        creatorID = sessionManager!!.creatorID
        ChildNam_txtview = findViewById(R.id.ChildNam_txtview)

        listOfRole = findViewById(R.id.invited_Children_list)
        myAdapter = ChildAdapter(this, R.layout.child_list_item, mylist)
        //do a data query
        getChildInfo()
        //logout
        //logout btn
        logoutbtn2 = findViewById(R.id.logoutbtn2)
        logoutbtn2.isVisible=false
        logoutbtn2.setOnClickListener {
            // Parents and children only have Session ID to check whether sign in
            sessionManager!!.login = false
            sessionManager!!.id = ""
            sessionManager!!.creatorID = ""
            //println("yyyyyy session logout")
            startActivity(Intent(this@InvitedChildActivity, LoginActivity::class.java))
            //println(sessionManager!!.id+"YYYY"+sessionManager!!.login)
            finish()
        }

        //bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_home
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

        Thread(Runnable {
            showListView()
        }).start()
        setUpListViewClickListener()
    }

    private fun getChildInfo() {
        databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild(sessionManager!!.id.toString())) {
                    ChildNam_txtview.text = dataSnapshot.child(sessionManager!!.id.toString())
                        .child("username").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun showListView() {
        databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    //judge the creatorid and ensure this children not be included
                    if (creatorID == item.child("creatorId").value.toString() && item.key.toString() != sessionManager!!.id) {
                        //mylist.clear()
                        mylist.add("Child:" + "       " + item.child("username").value.toString())
                        reslist.add(item.key.toString())
                        myAdapter!!.notifyDataSetChanged()
                        listOfRole.adapter = myAdapter
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    /*set up long click listener and click listener*/
    private fun setUpListViewClickListener() {
        listOfRole.setOnItemClickListener { parent, view, position, id ->
            val accountID = reslist[position]
            val username = mylist[position].split(":")[1].trim()
            val intent = Intent(this@InvitedChildActivity, ShowClickChildInfo::class.java)

            intent.putExtra("accountID", accountID)
            intent.putExtra("username", username)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }


    //child adapter no setting button
    class ChildAdapter(activity: Activity, val resourceId: Int, data: ArrayList<String>) :
        ArrayAdapter<String>(activity, resourceId, data) {
        @SuppressLint("ViewHolder", "SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val image: ImageView = view.findViewById(R.id.imageOfItem_child)
            val role: TextView = view.findViewById(R.id.roleOfItem_child)
            val username: TextView = view.findViewById(R.id.nameOfItem_child)
            val roleOfUser = getItem(position)!!.split(":")[0]
            val nameOfUser = getItem(position)!!.split(":")[1].trim()
            if (roleOfUser == "Child") {
                image.setImageResource(R.drawable.child);
                role.text = "Child"
                username.text = nameOfUser
            }
            return view
        }
    }
}