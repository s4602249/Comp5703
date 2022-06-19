package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage.InvitedParentActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import kotlinx.android.synthetic.main.activity_parent.*
import kotlinx.android.synthetic.main.activity_qrparents.*

class QRParents: AppCompatActivity() {

    var databaseRefParent: DatabaseReference? = null

    var show_Name: TextView?= null
    var phones: TextView?= null
    var parentdob: TextView?= null


    var database: FirebaseDatabase? = null
    var databaseRefs: DatabaseReference? = null
    var sessionManager: SessionManager? = null


    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_person -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_home -> {
                val intent = Intent(this@QRParents, InvitedParentActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@QRParents, SettingMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@QRParents, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@QRParents, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_qrparents)

        database = FirebaseDatabase.getInstance()
        databaseRefs =database?.reference

        sessionManager = SessionManager(applicationContext)

        getParentInfo(sessionManager!!.id.toString())





        //bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        //bottomNavigationView.selectedItemId = R.id.ic_home
        bottomNavigationView.selectedItemId = R.id.ic_person
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)


        uploadProfile()



    }

    private fun getParentInfo(id: String){

        //val bar = findViewById<ProgressBar>(R.id.progressbar)
        show_Name=findViewById(R.id.Parentsname)
        phones=findViewById(R.id.phones_text)
        parentdob=findViewById(R.id.bods_text)


        databaseRefs!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("Parent").hasChild(id)) {

                    //put the child data query here
                    var root: DataSnapshot = snapshot
                    var kid: DataSnapshot = root.child("Parent").child(id)
                    show_Name?.setText("User:" + kid.child("username").getValue(true).toString())
                    phones?.setText(kid.child("phone").getValue(true).toString())
                    parentdob?.setText(kid.child("dob").getValue(true).toString())

                    var judgedob=kid.child("dob").getValue(true)
                    var judgephones=kid.child("phone").getValue(true)
                    if(judgephones.toString()=="null"){phones?.setText("123")}
                    if(judgedob.toString()=="null" || bods_text.toString()==""){
                        parentdob?.setText("1-January-2022").toString() }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun uploadProfile(){
        Edits.setOnClickListener{
            Log.d("QRParents", "Try to edit information")
            val intent=Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
    }




}
