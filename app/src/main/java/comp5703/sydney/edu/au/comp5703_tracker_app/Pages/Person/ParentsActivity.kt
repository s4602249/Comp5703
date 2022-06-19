package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage.MainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.databinding.ActivityParentBinding
import kotlinx.android.synthetic.main.activity_parent.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import kotlin.collections.ArrayList


class ParentsActivity:AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseRefs: DatabaseReference? = null
    var databaseCreator: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    var mylist: ArrayList<String> = ArrayList()
    var databaseRefParent: DatabaseReference? = null


    var sessionManager: SessionManager? = null

    var creatorIds: String = String()



    private lateinit var binding : ActivityParentBinding

    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_person -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_setting -> {
                val intent = Intent(this@ParentsActivity, SettingMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_home -> {
                val intent = Intent(this@ParentsActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }

            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@ParentsActivity, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                overridePendingTransition(0,0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> { // it is a test, simply this to different activity
                val intent = Intent(this@ParentsActivity, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_parent)
        databaseRefs = database?.reference
        binding = ActivityParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(applicationContext)







        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_person
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)


        //read database
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        databaseRefParent= database?.reference!!.child("Parent")
        databaseCreator=FirebaseDatabase.getInstance().getReference("Creator")

        if(auth.currentUser == null){
            databaseRefParent!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot){
                    if(dataSnapshot.hasChild(sessionManager?.id.toString())){
                        val intent = Intent(this@ParentsActivity, QRParents::class.java)
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
        }


        fetchCreator()
        uploadProfile()




    }



    private fun fetchCreator(){
        val refs=FirebaseDatabase.getInstance().getReference("Creator")

        refs.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {

                    for(pos in snapshot.children) {

                        val Creators = pos.getValue(creatoruser::class.java)!!

                        if(auth.currentUser?.email==Creators?.email) {
                            binding.toolbartitle.setText(Creators.username)
                            binding.emailText.setText(Creators.email)
                            binding.phoneText.setText(Creators.phone)
                            binding.bodText.setText(Creators.dob)

                            var judgebod=Creators.dob
                            var judgephone=Creators.phone

                            if(judgebod=="null"||judgebod==""|| judgebod==null)
                            {binding.bodText.setText("1-January-2022")}

                            if(judgephone=="null"|| judgephone==null ){binding.phoneText.setText("123")}
                        }

                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }

    private fun uploadProfile(){
        edits.setOnClickListener{
            Log.d("ParentsActivity", "Try to edit information")
            val intent=Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
        
        btn_changepassowrd.setOnClickListener{
            Log.d("ParentsActivity", "Try to change password")
            val intent=Intent(this, Repassoword::class.java)
            startActivity(intent)
        }     
        
    }


    

}
