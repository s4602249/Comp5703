package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import kotlinx.android.synthetic.main.password_edit.*

class Repassoword: AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    private lateinit var databases : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_edit)

        auth = FirebaseAuth.getInstance()


        re_password.setOnClickListener {
            val email:String=forget_email.text.toString().trim{it<=' '}
            if(email.isEmpty()){
                Toast.makeText(this,"Please enter you email",Toast.LENGTH_SHORT).show()
            }else {FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener{task->if(task.isSuccessful){
                    Toast.makeText(this,"Email sent to you email",Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this,task.exception!!.message,Toast.LENGTH_SHORT).show()
                }
                }
            }

//        ChangePassword()

        }
    }

    //Only can send the email to change password at present
    /**
    private fun ChangePassword(){

        if(et_currentpassword.text.isNotEmpty() && et_newpassword.text.isNotEmpty() && et_confirm.text.isNotEmpty())
        {
            if(et_newpassword.text.toString().equals(et_confirm.text.toString()))
            {
                val user=auth.currentUser
                val passwords=et_newpassword.text.toString()
                if(user!=null && user.email!=null){
                    val credential=EmailAuthProvider.getCredential(user.email!!,et_currentpassword.text.toString())

                    user?.reauthenticate(credential).addOnCompleteListener{

                        if(it.isSuccessful){
                            Toast.makeText(this,"Repassword action success",Toast.LENGTH_SHORT).show()

                            auth.currentUser!!.updatePassword(passwords).addOnCompleteListener{ task->
                            if(task.isSuccessful){
                                Toast.makeText(this,"Change successful",Toast.LENGTH_SHORT).show()
                                val intent=Intent(this, ParentsActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        }else{
                            Toast.makeText(this,"Your current password not right OR new password and confirm not match",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{startActivity(Intent(this,ParentsActivity::class.java))}
            }else{Toast.makeText(this,"Passwrod mismatchind",Toast.LENGTH_SHORT).show()}
        }else{Toast.makeText(this,"sorry not success!!!",Toast.LENGTH_SHORT).show()}
    }
**/



}