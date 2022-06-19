package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage

import android.annotation.SuppressLint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View

import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentAdapter
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.LoginActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import kotlinx.android.synthetic.main.activity_invited_parent.*

class InvitedParentActivity : AppCompatActivity() {
    var sessionManager: SessionManager? = null

    // get creatorID
    var creatorID: String? = null

    //clarify database variables
    var database: FirebaseDatabase? = null
    var databaseRef: DatabaseReference? = null
    var databaseRefKid: DatabaseReference? = null
    var databaseRefParent: DatabaseReference? = null

    lateinit var addUsers: FloatingActionButton
    lateinit var parentNam_txtview: TextView

    //clarify list view components
    lateinit var listOfUser: ListView
    var mylist: ArrayList<String> = ArrayList()

    //store id information
    var reslist: ArrayList<String> = ArrayList()
    var myAdapter: ParentAdapter? = null

    //logout
    lateinit var logoutbtn3: ImageButton

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_home -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_person -> {
                val intent = Intent(this@InvitedParentActivity, ParentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> {
                val intent = Intent(this@InvitedParentActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@InvitedParentActivity, SettingMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> {
                val intent = Intent(this@InvitedParentActivity, NotifyActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invited_parent)
        //Get DB connection
        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference
        databaseRefKid = database?.reference!!.child("Children")
        databaseRefParent = database?.reference!!.child("Parent")
        //Initialize SessionManager
        sessionManager = SessionManager(applicationContext)
        //set up listview
        listOfUser = findViewById(R.id.listOfParent)
        myAdapter = ParentAdapter(this, R.layout.parent_list_item, mylist)
        //display username
        parentNam_txtview = findViewById(R.id.parentNam_txtview)
        //bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        //bottomNavigationView.selectedItemId = R.id.ic_home
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        creatorID = sessionManager!!.creatorID

        //logout btn
        logoutbtn3 = findViewById(R.id.logoutbtn3)
        logoutbtn3.isVisible=false
        //click logout button
        logoutbtn3.setOnClickListener {
            // Parents and children only have Session ID to check whether sign in
            sessionManager!!.login = false
            sessionManager!!.id = ""
            sessionManager!!.creatorID = ""
            //println("yyyyyy session logout")
            startActivity(Intent(this@InvitedParentActivity, LoginActivity::class.java))
            //println(sessionManager!!.id+"YYYY"+sessionManager!!.login)
            finish()

        }


        //show list content
        Thread(Runnable {
            showListView()
        }).start()

        //setup click listen for child item
        setUpListViewClickListener()

        /*make the button moveable*/
        var downX: Float = 0.0F
        var downY: Float = 0.0F
        var dx: Float = 0.0F
        var dy: Float = 0.0F
        var prev: Long = 0
        var current: Long = 0
        var dif: Long = 0
        addUsers = findViewById(R.id.addUsers)
        addUsers.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    prev = System.currentTimeMillis() / 200;//控制时间精度
                }
                MotionEvent.ACTION_MOVE -> {
                    dx += event.x - downX
                    dy += event.y - downY
                    if (dx < -862) {
                        dx = (-862).toFloat()
                    }
                    if (dx > 80) {
                        dx = 80.toFloat()
                    }
                    if (dy < -1340) {
                        dy = (-1340).toFloat()
                    }
                    if (dy > 127) {
                        dy = 127.toFloat()
                    }
                    addUsers.setTranslationX(dx)
                    addUsers.setTranslationY(dy)
                }

                MotionEvent.ACTION_UP -> {
                    current = System.currentTimeMillis() / 200;//控制时间精度
                    dif = current - prev;
                    if (dif == 0L) {
                        addUsers.performClick()
                    }

                }

            }
            true
        })
        Thread(Runnable {
            addUsers.setOnClickListener {
                val intent = Intent(this, ParentAddKidAndParent::class.java)
                intent.putStringArrayListExtra("userList", mylist)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
            }
        }).start()
        findCreatorName()
    }

    /*show the kid listview*/
    private fun showListView() {
        databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (creatorID == item.child("creatorId").value.toString()) {
                        //mylist.clear()
                        mylist.add("Child:" + "       " + item.child("username").value.toString() + ";" + "    " + item.key.toString())
                        //save key to another list
                        reslist.add(item.key.toString())
                        myAdapter!!.notifyDataSetChanged()
                        listOfUser.adapter = myAdapter
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
        databaseRefParent!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {

                    if (creatorID == item.child("creatorId").value.toString()) {
                        if (item.key.toString() != sessionManager!!.id) {
                            mylist.add("Parent:" + "    " + item.child("username").value.toString() + ";" + "    " + item.key.toString())
                            reslist.add(item.key.toString())
                            //save key to another list
                            myAdapter!!.notifyDataSetChanged()
                            // Connect the listView and the adapter
                            listOfUser.adapter = myAdapter
                        }
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
        listOfUser.setOnItemClickListener { parent, view, position, id ->

            //val element = myAdapter?.getItem(position) // The item that was clicked
            //val acountID = element!!.split("-")[1].split(":")[0].split(")")[0]
            val accountID = reslist[position]
            val intent = Intent(this@InvitedParentActivity, ShowUserInfo::class.java)
            intent.putExtra("userId", accountID)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        listOfUser.setOnItemLongClickListener { parent, view, position, id ->
            val accountID = reslist[position]
            val builder = AlertDialog.Builder(this@InvitedParentActivity)
            builder.setTitle("Delete an account").setMessage("Do you want to delete this account?")
                .setPositiveButton("Delete") { dialogInterface, i ->
                    databaseRefKid?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (item in dataSnapshot.children) {
                                if (accountID == item.key.toString()) {
                                    mylist.removeAt(position)
                                    //delete id in reslist
                                    reslist.removeAt(position)
                                    myAdapter?.notifyDataSetChanged()
                                    item.ref.removeValue()
                                    Toast.makeText(
                                        this@InvitedParentActivity,
                                        "Account successfully deleted",
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
                    databaseRefParent?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (item in dataSnapshot.children) {
                                if (accountID == sessionManager!!.id) {
                                    Toast.makeText(
                                        this@InvitedParentActivity,
                                        "You cannot delete yourself",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (accountID == item.key.toString()) {
                                    mylist.removeAt(position)
                                    //delete id in reslist
                                    reslist.removeAt(position)
                                    myAdapter?.notifyDataSetChanged()
                                    item.ref.removeValue()
                                    Toast.makeText(
                                        this@InvitedParentActivity,
                                        "Account successfully deleted",
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
                }
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    //Nothing happens here
                }
            builder.create().show()
            return@setOnItemLongClickListener (true)
        }
    }

    /*find parent name*/
    private fun findCreatorName() {
        databaseRefParent!!.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild(sessionManager!!.id.toString())) {
                    parentNam_txtview.text = dataSnapshot.child(sessionManager!!.id.toString())
                        .child("username").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }


}