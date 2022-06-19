package comp5703.sydney.edu.au.comp5703_tracker_app.Pages

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage.MainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanContract

import androidx.activity.result.ActivityResultLauncher
import com.journeyapps.barcodescanner.ScanIntentResult
import android.widget.Toast
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage.InvitedChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage.InvitedParentActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.EditActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import java.util.regex.Pattern


const val RC_SIGN_IN = 123

class LoginActivity : AppCompatActivity() {
    /*declare the variable we gonna use*/
    val auth = FirebaseAuth.getInstance()
    var sessionManager: SessionManager? = null
    //var database = FirebaseDatabase.getInstance()
    //var databaseRef: DatabaseReference? = null
    var LoginEmail: EditText? = null
    var LogInPassword: EditText? = null
    var button: Button? = null
    var Scanner: Button? = null

    //var Google_login: Button? = null
    var RegisterBtn: Button? = null

    var databaseRefKid: DatabaseReference? = null
    var databaseRefParent: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    var databaseRefs: DatabaseReference? = null
    var databaseRefCreator: DatabaseReference? = null
    //to handle the UI change in thread
    var handler = Handler(Looper.getMainLooper())

    // Register the launcher and result handler
    private val barcodeLauncher: ActivityResultLauncher<ScanOptions?> = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@LoginActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                this@LoginActivity,
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()
            databaseRefs!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(Pattern.compile("[0-9]*").matcher(result.contents).matches())
                    {
                        if(snapshot.child("Parent").hasChild(result.contents)||snapshot.child("Children").hasChild(result.contents))
                        {
                            sessionManager= SessionManager(applicationContext)
                            sessionManager!!.id=result.contents
                            sessionManager!!.login=true

                            Toast.makeText(
                                this@LoginActivity,
                                "Account successfully Login",
                                Toast.LENGTH_LONG
                            ).show()
                            overridePendingTransition(0, 0)
                            if(snapshot.child("Parent").hasChild(result.contents)) {
                                val CreatorID=snapshot.child("Parent").child(sessionManager!!.id.toString()).
                                child("creatorId").value.toString()
                                sessionManager!!.creatorID=CreatorID
                                println("dengluyyyy"+sessionManager!!.creatorID)
                                startActivity(
                                    Intent(this@LoginActivity, InvitedParentActivity::class.java)

                                )
                            } else {
                                val CreatorID=snapshot.child("Children").child(sessionManager!!.id.toString()).
                                child("creatorId").value.toString()
                                sessionManager!!.creatorID=CreatorID
                                startActivity(
                                    Intent(this@LoginActivity, InvitedChildActivity::class.java)
                                )
                            }
                            overridePendingTransition(0, 0)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Invaild Code",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Invaild Code",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*Find each view by ID defined in xml design*/
        setContentView(R.layout.activity_login)
        LoginEmail = findViewById(R.id.LogInEmail)
        LogInPassword = findViewById(R.id.LogInPassword)
        button = findViewById(R.id.button)
        Scanner = findViewById(R.id.Scanner)
        val Google_login = findViewById<View>(R.id.Google_login) as SignInButton
        RegisterBtn = findViewById(R.id.register_btn)
        database = FirebaseDatabase.getInstance()
        databaseRefs = database?.reference
        databaseRefKid = database?.reference!!.child("Children")
        databaseRefParent = database?.reference!!.child("Parent")
        databaseRefCreator = database?.reference!!.child("Creator")
        //launch scanner
        Scanner!!.setOnClickListener {
            val options = ScanOptions()
            options.setOrientationLocked(false)
            barcodeLauncher.launch(ScanOptions())
        }


        RegisterBtn!!.setOnClickListener {
            overridePendingTransition(0, 0)
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.clientId))
            .requestEmail()
            .build()
        //init firebase auth


        // Build a GoogleSignInClient with the options specified by gso.
        val gooleAuth = GoogleSignIn.getClient(this, gso);


        Google_login.setOnClickListener {
            //create a thread to run heavy task otherwise the activity will become laggy
            Thread(Runnable { //Thread().run { Thread.sleep(3000); }
                //some method here
                val signInIntent: Intent = gooleAuth.getSignInIntent()
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }).start()
        }

        //login account
        login()
    }

    /*email and password login*/
    private fun login() {
        button!!.setOnClickListener {
            if (TextUtils.isEmpty(LoginEmail?.text.toString())) {
                LoginEmail!!.setError("Please enter email")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(LogInPassword?.text.toString())) {
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(
                LoginEmail!!.text.toString(),
                LogInPassword!!.text.toString()
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed, please try again", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    /*from google override onActivityResult*/
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            //create a thread to run heavy task otherwise the activity will become laggy
            Thread(Runnable {
                //some method here
                handler.postDelayed({
                    handleSignInResult(task)
                },0)
            }).start()
        }
    }

    /*from google override handleSignInResult*/
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        //println("xxxxxxxxx handleSignInResult got called")
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            firebasWithGoogleSignin(account)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "onActivitResult:${e.message}")
            Toast.makeText(this, "${e}", Toast.LENGTH_LONG).show()
        }
    }

    /*google signin method*/
    fun firebasWithGoogleSignin(account: GoogleSignInAccount) {
        Log.d("TAG", "firebase With Google Account------ begin")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(TAG, "firebase With Google Account------ successful logged in")
                //get logged in user and create session
                val firebaseUser = auth.currentUser
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email
                Log.d(TAG, "firebase With Google Account------ Uid " + uid)
                Log.d(TAG, "firebase With Google Account------ emali " + email)
                //check user is new or existing
                if (authResult.additionalUserInfo!!.isNewUser) {
                    //user is new account created
                    Log.d("TAG", "firebase With Google Account------ added +" + email)
                    Toast.makeText(this, "Congrats, Email added successfully", Toast.LENGTH_SHORT)
                        .show()
                    databaseRefs?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.child("Creator").hasChild(uid)) {
                                //必须同时在authentication跟realtime都不存在
                                Toast.makeText(this@LoginActivity, "Already in realtime database", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            else{
                                databaseRefs?.child("Creator")!!.child(uid).child("email").setValue(email)
                                startActivity(Intent(this@LoginActivity, EditActivity::class.java))
                                finish()
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                } else {
                    Toast.makeText(this, "Logged in...", Toast.LENGTH_SHORT).show()
                    //用户没注册又退出了 再次点击google登录 则还是需要去完善信息
                    databaseRefs?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            //没有username则表示还没注册好 则去到edit页面
                            if (!snapshot.child("Creator").child(uid).hasChild("username")) {
                                startActivity(Intent(this@LoginActivity, EditActivity::class.java))
                                finish()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Logged in failed, please tey again.", Toast.LENGTH_SHORT)
                    .show()
                Log.d("TAG", "Logged in error " + error.message)
            }
    }

}
