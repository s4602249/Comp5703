package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage.MainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage.InvitedChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage.InvitedParentActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.LoginActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager

class Logout_sample : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var database: FirebaseDatabase? = null
    var databaseRefs: DatabaseReference? = null
    var sessionManager: SessionManager? = null
    lateinit var LogOut: Button
    /* Bottom navigation-> reused part for each activity*/
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_person -> {
               // return@OnNavigationItemSelectedListener false
            }
            R.id.ic_home -> {
                if(auth.currentUser!=null){
                    val intent = Intent(this@Logout_sample, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    overridePendingTransition(0,0)
                }
                else{
                    databaseRefs!!.addListenerForSingleValueEvent(object : ValueEventListener {
                        // ensure skip to which page-Parent or Child
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if( snapshot.child("Parent").hasChild(sessionManager!!.id.toString())||snapshot.child("Children").hasChild(sessionManager!!.id.toString()))
                            {
                                if(snapshot.child("Parent").hasChild(sessionManager!!.id.toString()))
                                {
                                    val intent = Intent(this@Logout_sample, InvitedParentActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(0,0)
                                    finish()
                                    overridePendingTransition(0,0)
                                }
                                else
                                {
                                    val intent = Intent(this@Logout_sample, InvitedChildActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(0,0)
                                    finish()
                                    overridePendingTransition(0,0)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
                return@OnNavigationItemSelectedListener true
            }

            R.id.ic_help -> { // it is a test, simply this to different activity
                return@OnNavigationItemSelectedListener false
            }

            R.id.ic_setting -> {
                val intent = Intent(this@Logout_sample, SettingMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@Logout_sample, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_logout_sample)
        LogOut=findViewById(R.id.button3)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefs =database?.reference
        //import the current user(not creator)
        sessionManager = SessionManager(applicationContext)

        //set logOut click event listen
        LogOut.setOnClickListener {

            if(auth.currentUser!=null)//creator logging out
            {
                auth.signOut()
                println("yyyyyy anth logout")
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
                startActivity(Intent(this@Logout_sample, LoginActivity::class.java))
                finish()
            }
            else
            {
                // Parents and children only have Session ID to check whether sign in
                sessionManager!!.login=false
                sessionManager!!.id=""
                sessionManager!!.creatorID=""
                //println("yyyyyy session logout")
                startActivity(Intent(this@Logout_sample, LoginActivity::class.java))
                //println(sessionManager!!.id+"YYYY"+sessionManager!!.login)
                finish()
            }
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        //set help selected
        bottomNavigationView.selectedItemId = R.id.ic_help
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
    }

}