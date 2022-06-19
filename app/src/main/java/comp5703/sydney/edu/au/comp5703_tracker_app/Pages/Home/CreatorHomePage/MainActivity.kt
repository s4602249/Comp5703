package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentAdapter
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.LoginActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import kotlinx.android.synthetic.main.activity_edit.view.*
import java.util.*
import kotlin.collections.ArrayList
import android.view.View.*
import android.view.MotionEvent

import android.view.View.OnTouchListener
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.EditActivity


class MainActivity : AppCompatActivity() {
    /*declare the variable we gonna use*/
    lateinit var auth: FirebaseAuth
    var databaseRefKid: DatabaseReference? = null
    var databaseRefParent: DatabaseReference? = null
    var databaseRefCreator: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    lateinit var AddMember: FloatingActionButton
    lateinit var listOfRole: ListView
    lateinit var CreatorNam_txtview: TextView
    var mylist: ArrayList<String> = ArrayList()

    //check session
    var sessionManager: SessionManager? = null
    //store id information
    var reslist: ArrayList<String> = ArrayList()
    var myAdapter: ParentAdapter? = null
    //Logout button
    lateinit var logoutbtn: ImageButton

    /* Bottom navigation-> reused part for each activity*/
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_home -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_person -> {
                val intent = Intent(this@MainActivity, ParentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> {
                val intent = Intent(this@MainActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@MainActivity, SettingMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> {
                val intent = Intent(this@MainActivity, NotifyActivity::class.java)
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
        //dont push window
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        /*Find each view by ID defined in xml design*/
        setContentView(R.layout.activity_main)
        //set textview content
        CreatorNam_txtview = findViewById(R.id.CreatorNam_txtview)
        sessionManager = SessionManager(applicationContext)
        /*make the button movable*/
        var downX: Float= 0.0F
        var downY: Float= 0.0F
        var dx: Float= 0.0F
        var dy: Float= 0.0F
        var prev: Long = 0
        var current: Long = 0
        var dif: Long = 0
        AddMember = findViewById(R.id.AddMember)
        AddMember.setOnTouchListener(OnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    prev = System.currentTimeMillis() / 200;//控制时间精度
                }
                MotionEvent.ACTION_MOVE -> {
                    dx += event.x - downX
                    dy += event.y - downY
                    if(dx<-862){
                        dx= (-862).toFloat()
                    }
                    if(dx >80){
                        dx=80.toFloat()
                    }
                    if(dy<-1340){
                        dy= (-1340).toFloat()
                    }
                    if(dy >127){
                        dy=127.toFloat()
                    }
                    AddMember.setTranslationX(dx)
                    AddMember.setTranslationY(dy)
                }

                MotionEvent.ACTION_UP -> {
                    current = System.currentTimeMillis() / 200;//控制时间精度
                    dif = current - prev;
                    if (dif == 0L) {
                        AddMember.performClick()
                    }

                }

            }
            true
        })
        //logout btn
        logoutbtn=findViewById(R.id.logoutbtn)
        logoutbtn.isVisible=false

        listOfRole = findViewById(R.id.listOfRole)
        myAdapter = ParentAdapter(this, R.layout.parent_list_item, mylist)

        /*create database instance and firebase auth*/
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefCreator = database?.reference!!.child("Creator")
        databaseRefKid = database?.reference!!.child("Children")
        databaseRefParent = database?.reference!!.child("Parent")

        //check google signin user if fill in the information
        checkGoogleSignin()

        //Click add button go to add button page
        Thread(Runnable {
            AddMember.setOnClickListener {
                val intent = Intent(this@MainActivity, AddParentAndKids::class.java)
                intent.putStringArrayListExtra("userList", mylist)

                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
            }
        }).start()

        Thread(Runnable {
            //some method here
            //create a thread to run heavy task otherwise the main activity will become laggy
            loadUserInfo()
        }).start()


        //click logout button
        logoutbtn.setOnClickListener {

            if(auth.currentUser!=null)//creator logging out
            {
                auth.signOut()
                println("yyyyyy auth logout")
                sessionManager!!.login=false
                sessionManager!!.id=""
                sessionManager!!.creatorID=""
                //create new GoogleSignIn clent
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.clientId))//personal credential key
                    .requestEmail()
                    .build()
                //init firebase auth

                // Build a GoogleSignInClient with the options specified by gso.
                val gooleAuth = GoogleSignIn.getClient(this, gso)
                gooleAuth.signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            else
            {
                // Parents and children only have Session ID to check whether sign in
                sessionManager!!.login=false
                sessionManager!!.id=""
                sessionManager!!.creatorID=""
                //println("yyyyyy session logout")
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                //println(sessionManager!!.id+"YYYY"+sessionManager!!.login)
                finish()
            }
        }


        //bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_home
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

        //show list content
        Thread(Runnable {
            //some method here
            //create a thread to run heavy task otherwise the activity will become laggy
            showListView()
        }).start()

        //listview click listener
        setUpListViewClickListener()
        //show username on the page
        findCreatorName()
    }

    /*set up long click listener and click listener*/
    private fun setUpListViewClickListener() {
        listOfRole.setOnItemClickListener { parent, view, position, id ->
            val accountID = reslist[position]
            val username = mylist[position].split(":")[1].trim()
            val intent = Intent(this@MainActivity, ShowClickUserInfo::class.java)

            intent.putExtra("accountID", accountID)
            intent.putExtra("username", username)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        listOfRole.setOnItemLongClickListener { parent, view, position, id ->
            //val element = myAdapter?.getItem(position)
            //val accountID = element!!.split("-")[1].split(":")[0].split(")")[0]
            val accountID = reslist[position]
            val builder = AlertDialog.Builder(this@MainActivity)
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
                                        this@MainActivity,
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
                                if (accountID == item.key.toString()) {
                                    mylist.removeAt(position)
                                    //delete id in reslist
                                    reslist.removeAt(position)
                                    myAdapter?.notifyDataSetChanged()
                                    item.ref.removeValue()
                                    Toast.makeText(
                                        this@MainActivity,
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

    /*check if user logged in*/
    private fun loadUserInfo() {
        val user = auth.currentUser
        //user didn't login go to log in page
        if (user == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
    private fun checkGoogleSignin(){
        //用户没注册又退出了 再次点击google登录 则还是需要去完善信息
        database?.reference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //没有username则表示还没注册好 则去到edit页面
                if (!snapshot.child("Creator").child(auth.currentUser!!.uid).hasChild("username")) {
                    startActivity(Intent(this@MainActivity, EditActivity::class.java))
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    /*show the parents and kid listview*/
    private fun showListView() {
        databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (auth.currentUser!!.uid == item.child("creatorId").value.toString()) {
                        //mylist.clear()
                        mylist.add("Child:" + "       " + item.child("username").value.toString()+";"+"    "+item.key.toString())
                        //save key to another list
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
        databaseRefParent!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (auth.currentUser!!.uid == item.child("creatorId").value.toString()) {
                        mylist.add("Parent:" + "    " + item.child("username").value.toString()+";"+"    "+item.key.toString())
                        reslist.add(item.key.toString())
                        //save key to another list
                        myAdapter!!.notifyDataSetChanged()
                        // Connect the listView and the adapter
                        listOfRole.adapter = myAdapter
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun findCreatorName() {
        databaseRefCreator!!.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild(auth.currentUser!!.uid.toString())) {
                    CreatorNam_txtview.text = dataSnapshot.child(auth.currentUser!!.uid)
                        .child("username").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

}
