package comp5703.sydney.edu.au.comp5703_tracker_app.Pages

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage.InvitedChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage.InvitedParentActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import com.google.firebase.auth.FirebaseAuth
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage.MainActivity
import android.os.Looper


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    /*declear the variable we gonna use*/

    private val Splash_time: Long = 3000 //Long is required in postDelayed->3 s
    var sessionManager: SessionManager? = null
    var database: FirebaseDatabase? = null
    var databaseRefs: DatabaseReference? = null
    val auth = FirebaseAuth.getInstance()
    var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sessionManager = SessionManager(applicationContext)
        database = FirebaseDatabase.getInstance()
        databaseRefs = database?.reference
        if (sessionManager!!.login == true) {
            databaseRefs?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("Parent").hasChild(sessionManager!!.id.toString())
                        || snapshot.child("Children").hasChild(sessionManager!!.id.toString())
                    ) {
                        if (snapshot.child("Parent").hasChild(sessionManager!!.id.toString())) {
                            startActivity(
                                Intent(this@SplashScreenActivity, InvitedParentActivity::class.java)
                                    .putExtra("PARENTS", "PARENTS")
                            )
                            finish()
                        } else {
                            startActivity(
                                Intent(this@SplashScreenActivity, InvitedChildActivity::class.java)
                                    //.putExtra("Infomation_ID", result.contents)
                                    .putExtra("CHILDREN", "CHILDREN")
                            )
                            finish()
                        }
                    }

                    //else?
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        } else {
            Thread(Runnable { //Thread().run { Thread.sleep(3000); }
                //some method here
                handler.postDelayed({
                    println("yyyyy+check1")
                    println("yyyyy id check1 " + sessionManager!!.id)
                    checkUserLoginStatus()
                }, Splash_time)
            }).start()
        }

    }

    /*if user is logged in then go the mainActivity directly
     if not go to login page
     */
    private fun checkUserLoginStatus() {
        val firebaseCurrentUser = auth.currentUser
        if (firebaseCurrentUser != null) {
            sessionManager!!.creatorID= auth.currentUser?.uid
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
            overridePendingTransition(0, 0)
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}