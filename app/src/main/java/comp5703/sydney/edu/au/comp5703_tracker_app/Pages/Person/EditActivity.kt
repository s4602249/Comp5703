package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Parents
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Users
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.creatoruser
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import comp5703.sydney.edu.au.comp5703_tracker_app.databinding.ActivityEditBinding
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity :AppCompatActivity(){

    lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityEditBinding

    private lateinit var databases : DatabaseReference
    private lateinit var databasesPf : DatabaseReference
    private lateinit var databasesKid : DatabaseReference

    lateinit var DOB_input: EditText
    lateinit var dateup:EditText

    var databaseRefs: DatabaseReference? = null
    var database: FirebaseDatabase? = null




    lateinit var pick_date: ImageButton


    var sessionManager: SessionManager? = null

    var formatDate = SimpleDateFormat("dd-MMMM-YYYY", Locale.UK)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        databases = FirebaseDatabase.getInstance().getReference("Creator")

        databasesPf=FirebaseDatabase.getInstance().getReference("Parent")
        databasesKid=FirebaseDatabase.getInstance().getReference("Children")

        database = FirebaseDatabase.getInstance()

        databaseRefs =database?.reference


        sessionManager = SessionManager(applicationContext)


        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        DOB_input=findViewById(R.id.dobText)

        pick_date=findViewById(R.id.Parents_Edits_DOBtn)

        var dateup: String? =null

        pick_date.setOnClickListener {
            val getDate: Calendar = Calendar.getInstance()

            val datepicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    val selectDate: Calendar = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, i)
                    selectDate.set(Calendar.MONTH, i2)
                    selectDate.set(Calendar.DAY_OF_MONTH, i3)
                    val date: String = formatDate.format(selectDate.time)
                    DOB_input.setText(date)
                    dateup=date
                },
                getDate.get(Calendar.YEAR),
                getDate.get(Calendar.MONTH),
                getDate.get(Calendar.DAY_OF_MONTH)
            )
            datepicker.show()
        }




        binding.Editbt.setOnClickListener {
            var username = binding.username.text.toString()
            var phone = binding.phone.text.toString()
            var date=dateup.toString()

            judgenull(username,phone, date,sessionManager!!.id.toString())

        }


    }

    private fun judgenull(username: String, phone: String,newdate:String,id: String){

        var usernames=username
        var phones=phone
        var newdates=newdate
        var unam:String? =null


        if(auth.currentUser!=null){
            databases.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        for(pos in snapshot.children) {
                            val Creators = pos.getValue(creatoruser::class.java)!!

                            if(auth.currentUser!!.email==Creators.email) {
                                if (username.isBlank()){
                                    usernames= Creators.username
                                }
                                if(phone.isBlank()){
                                    phones=Creators.phone
                                }
                                if(newdate.isBlank()){
                                    newdates=Creators.dob
                                }
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

            databasesPf.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        for (pos in snapshot.children) {
                            var Parentsinfo = pos.getValue(Parents::class.java)!!
                            if (auth.currentUser!!.uid == Parentsinfo.creatorId) {
                                if (usernames == Parentsinfo.username) {
                                    unam=Parentsinfo.username
                                    tips2()
                                    break
                                }
                            }
                        }

                        if(usernames!=unam.toString()){
                            updateData(usernames,phones, newdates,id) }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

            databasesKid.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        for (pos in snapshot.children) {
                            val kids = pos.getValue(Users::class.java)!!
                            if (auth.currentUser!!.uid == kids.creatorId) {
                                if (usernames == kids.username) {
                                    unam=kids.username
                                    tips2()
                                    break
                                }
                            }
                        }

                        if(usernames!=unam){
                            updateData(usernames,phones, newdates,id) }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })


        }


        /** parents **/
        else{
            databaseRefs!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.child("Parent").hasChild(id)) {
                        //put in  parent data
                        var root: DataSnapshot = snapshot
                        var sorta=root.child("Parent")
                        var paren: DataSnapshot = root.child("Parent").child(id)
                        var parentcrid=paren.child("creatorId").getValue(true).toString()
                        var prusername=paren.child("username").getValue(true).toString()
                        var prphone=paren.child("phone").getValue(true).toString()
                        var prdob=paren.child("dob").getValue(true).toString()

                        if (username.isBlank()) {
                            usernames = prusername
                        }
                        if (phone.isBlank()) {
                            phones = prphone
                        }
                        if (newdate.isBlank()) {
                            newdates = prdob
                        }

                        sorta.children.forEach{
                            for(pos in sorta.children){
                                val parentinfo = pos.getValue(Parents::class.java)!!

                                if(id!=pos.key.toString() && parentcrid==parentinfo.creatorId){
                                    if(usernames==parentinfo.username ){
                                        unam=parentinfo.username
                                        tips2()
                                        break
                                    }
                                }

                            }
                        }

                        if(usernames!=unam){
                            updateData(usernames,phones, newdates,id) }

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }

            })


        }

    }



    private fun tips2(){
        Toast.makeText(this,"user name repeat!", Toast.LENGTH_SHORT).show()
    }



    private fun updateData(username: String, phone: String,newdate:String,id: String) {


        val user = mapOf<String,String>(
            "username" to username,
            "phone" to phone,
            "dob" to newdate
        )

        val currentid= auth.currentUser?.uid

        if (currentid != null) {
            databases.child(currentid).updateChildren(user).addOnSuccessListener {
                binding.username.text.clear()
                binding.phone.text.clear()
                binding.dobText.text.clear()
            }.addOnFailureListener{
                Toast.makeText(this,"Failed to Update", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this,"Successfuly Updated Creator", Toast.LENGTH_SHORT).show()
            Log.d("EditActivity", "Try to Creator information")
            val intent= Intent(this, ParentsActivity::class.java)
            startActivity(intent)
        }

        else {
            databasesPf.child(id).updateChildren(user).addOnSuccessListener {
                binding.username.text.clear()
                binding.phone.text.clear()
                binding.dobText.text.clear()

            }.addOnFailureListener{

                Toast.makeText(this,"Failed to Update", Toast.LENGTH_SHORT).show()

            }
            Toast.makeText(this,"Successfuly Updated Parent", Toast.LENGTH_SHORT).show()
            Log.d("EditActivity", "Try to Parent information")
            val intent= Intent(this, QRParents::class.java)
            startActivity(intent)
        }

    }
}
