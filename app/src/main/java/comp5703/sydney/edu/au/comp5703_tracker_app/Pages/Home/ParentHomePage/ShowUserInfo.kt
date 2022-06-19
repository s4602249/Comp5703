package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import de.hdodenhof.circleimageview.CircleImageView

class ShowUserInfo : AppCompatActivity() {
    //数据库四件套
    lateinit var auth: FirebaseAuth
    var databaseRefKid: DatabaseReference? = null
    var databaseRefParent: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    lateinit var userId_txtview1: TextView
    lateinit var userName_txtview1: TextView
    lateinit var userPhone_txtview1: TextView
    lateinit var userBth_txtview1: TextView
    lateinit var profile_image3: CircleImageView

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_home -> {
                val intent = Intent(this@ShowUserInfo, InvitedParentActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_person -> { // it is a test, simply this to different activity
                val intent = Intent(this@ShowUserInfo,  ParentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> {
                val intent = Intent(this@ShowUserInfo, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@ShowUserInfo, SettingMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> {
                val intent = Intent(this@ShowUserInfo, NotifyActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_user_info)
        userId_txtview1=findViewById(R.id.userId_txtview1)
        userName_txtview1=findViewById(R.id.userName_txtview1)
        userPhone_txtview1=findViewById(R.id.userPhone_txtview1)
        userBth_txtview1=findViewById(R.id.userBth_txtview1)
        profile_image3=findViewById(R.id.profile_image3)

        //create firebase instance
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefKid = database?.reference!!.child("Children")
        databaseRefParent = database?.reference!!.child("Parent")

        val data:String = intent.getStringExtra("userId").toString()

        //find kids and parents in database according to the id and update textview info
        databaseRefKid?.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (data == item.key.toString()) {
                        userId_txtview1.text= "User ID:   $data"
                        userName_txtview1.text="Username:  "+item.child("username").value.toString().split(";")[0]
                        userPhone_txtview1.text="User phone:   "+item.child("phone").value.toString()
                        userBth_txtview1.text="User birthday:   "+item.child("dob").value.toString()
                        profile_image3.setImageResource(R.drawable.child)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        databaseRefParent?.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (data == item.key.toString()) {
                        userId_txtview1.text= "User ID:   $data"
                        userName_txtview1.text="Username:  "+item.child("username").value.toString()
                        userPhone_txtview1.text="User phone:   "+item.child("phone").value.toString()
                        userBth_txtview1.text="User birthday:   "+item.child("dob").value.toString()
                        profile_image3.setImageResource(R.drawable.parents)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })





        //bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        //bottomNavigationView.selectedItemId = R.id.ic_home
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
    }
}