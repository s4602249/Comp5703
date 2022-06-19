package comp5703.sydney.edu.au.comp5703_tracker_app.Pages


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import android.app.DatePickerDialog;
import android.widget.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Creator
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    /*declare the variable we gonna use*/
    lateinit var auth:FirebaseAuth
    var databaseRef: DatabaseReference?=null
    var database: FirebaseDatabase?=null

    var registerButton: Button?=null
    var userNameInput: EditText?=null
    var EmailInput: EditText?=null
    var PasswordInput: EditText?=null
    var Back: Button?=null

    var PhoneInput:EditText?=null
    //var GenderInput:RadioGroup?=null
    var DOBInput:EditText?=null
    var DOB_Button:Button?=null
    //var gender_RB:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        /*Find each view by ID defined in xml design*/
        registerButton=findViewById(R.id.button2)
        userNameInput=findViewById(R.id.userNameInput)
        EmailInput=findViewById(R.id.EmailInput)
        PasswordInput=findViewById(R.id.PasswordInput)
        Back=findViewById(R.id.BackImageButton)

        DOB_Button=findViewById(R.id.Date_Picker)
        DOBInput=findViewById(R.id.DOBInput)
        //GenderInput=findViewById(R.id.GenderInput)
        PhoneInput=findViewById(R.id.PhoneInput)
        /*create database instance and firebase auth*/
        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        databaseRef=database?.reference!!.child("Creator")


        Thread(Runnable { //Thread().run { Thread.sleep(3000); }
            //some method here
            //create a thread to run heavy task otherwise the activity will become laggy
            register()
        }).start()

        Back!!.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
            overridePendingTransition(0, 0)
        }
        val myCalendar=Calendar.getInstance()
        val datePicker=DatePickerDialog.OnDateSetListener {view,year,month,dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateLable(myCalendar)

        }
        DOB_Button!!.setOnClickListener {
            val dateChoices=DatePickerDialog(this,datePicker,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH))
            dateChoices.datePicker.maxDate=myCalendar.timeInMillis
            dateChoices.show()

        }
//        GenderInput!!.setOnCheckedChangeListener { GenderInput,i->
//            val gender=findViewById<RadioButton>(i)
//            if(gender!=null)
//            {
//                gender_RB=gender.text.toString()
//            }
//        }

    }
    private fun updateLable(myCalendar: Calendar)
    {
        val myFormat="dd-MM-yyyy"
        val sdf= SimpleDateFormat(myFormat, Locale.UK)
        myCalendar
        DOBInput?.setText(sdf.format(myCalendar.time))
    }

    private fun register(){
        registerButton!!.setOnClickListener {
            if(TextUtils.isEmpty(userNameInput!!.text.toString())){
                userNameInput!!.setError("Please enter your username.")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(EmailInput!!.text.toString())){
                EmailInput!!.setError("Please enter your Email.")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(PasswordInput!!.text.toString())){
                PasswordInput!!.setError("Please enter your Password.")
                return@setOnClickListener
            }
            else if(PasswordInput!!.text.toString().length<6){
                PasswordInput!!.setError("Password must have 6 digits at least")
                return@setOnClickListener
            }
//            else if(TextUtils.isEmpty(PhoneInput!!.text.toString()))
//            {
//                PhoneInput!!.setError("Please enter your Phone.")
//                return@setOnClickListener
//            }
//            else if(TextUtils.isEmpty(DOBInput!!.text.toString()))
//            {
//                DOBInput!!.setError("Please enter your DOB.")
//                return@setOnClickListener
//            }
//            else if(gender_RB==null){
//                Toast.makeText(this,"Choose your gender",Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
            auth.createUserWithEmailAndPassword(
                EmailInput!!.text.toString(),
                PasswordInput!!.text.toString()
            ).addOnCompleteListener {
                //once task complete, then save user info
                if(it.isSuccessful){
                    val currentUser=auth.currentUser
                    val currentUserDb=databaseRef?.child(currentUser?.uid!!) // hash ID
                    val creator=Creator(userNameInput!!.text.toString(),EmailInput!!.text.toString(),
                      PhoneInput!!.text.toString(),DOBInput!!.text.toString())
                    /*currentUserDb?.child("Username")?.setValue(userNameInput!!.text.toString())
                    currentUserDb?.child("Email")?.setValue(EmailInput!!.text.toString())
                    currentUserDb?.child("Password")?.setValue(PasswordInput!!.text.toString())*/
                    currentUserDb?.setValue(creator)
                    Toast.makeText(this,"Congrats, Register successfully",Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    //start intent and send a value to check
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this,"Email is not valid or it's already been used,try another one",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}