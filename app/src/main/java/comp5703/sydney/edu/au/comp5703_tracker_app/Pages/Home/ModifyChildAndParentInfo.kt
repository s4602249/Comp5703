package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage.MainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage.InvitedParentActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class ModifyChildAndParentInfo : AppCompatActivity() {
    //var ShowId: TextView? = null
    var mylist: ArrayList<String> = ArrayList()
    var sessionManager: SessionManager? = null
    lateinit var auth: FirebaseAuth
    var databaseRef: DatabaseReference?=null
    var databaseRefKid: DatabaseReference? = null
    var databaseRefParent: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    // original information area
    var original_Name: String?=null
    var original_DOB: String?=null
    var original_Phone: String?=null

    //input area
    lateinit var DOB_input: EditText
    lateinit var Name_input: EditText
    lateinit var Phone_input: EditText
    lateinit var photo_user:ImageView

    // button area
    var formatDate = SimpleDateFormat("dd-MMMM-YYYY", Locale.UK)
    lateinit var btn_pick_date: ImageButton
    lateinit var btn_back: Button
    lateinit var btn_Edit: Button

    var Edited_User: String?=null
    lateinit var data:String
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_home -> {
                var intent:Intent
                if(sessionManager!!.id!="")
                {
                    intent = Intent(this@ModifyChildAndParentInfo, InvitedParentActivity::class.java)
                }
                else
                {
                    intent = Intent(this@ModifyChildAndParentInfo, MainActivity::class.java)
                }
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_person -> { // it is a test, simply this to different activity
                val intent = Intent(this@ModifyChildAndParentInfo,  ParentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> {
                val intent = Intent(this@ModifyChildAndParentInfo, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@ModifyChildAndParentInfo, SettingMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> {
                val intent = Intent(this@ModifyChildAndParentInfo, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_modify_child_and_parent_info)

        database = FirebaseDatabase.getInstance()
        databaseRef = database?.reference
        databaseRefKid = database?.reference!!.child("Children")
        databaseRefParent = database?.reference!!.child("Parent")
        sessionManager = SessionManager(applicationContext)
        init_FamilyMember()

        DOB_input=findViewById(R.id.DOB_Edit_New)
        Name_input=findViewById(R.id.Name_Edit_New)
        Phone_input=findViewById(R.id.Phone_Edit_New)
        photo_user=findViewById(R.id.profile_image5)

        btn_pick_date=findViewById(R.id.Parent_Edit_DOBtn)
        btn_Edit=findViewById(R.id.parent_Edit_children)
        btn_back=findViewById(R.id.parent_Edit_back)
        data= intent.getStringExtra("userId").toString()
        // init information of editing page and original element
        showChildren_EditingPage(data)
        //ShowId!!.text=data

        // init datepicker
        btn_pick_date.setOnClickListener {
            val getDate: Calendar = Calendar.getInstance()
            val datepicker = DatePickerDialog(
                this,
                // you can add style in line 70
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                    val selectDate: Calendar = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, i)
                    selectDate.set(Calendar.MONTH, i2)
                    selectDate.set(Calendar.DAY_OF_MONTH, i3)
                    val date: String = formatDate.format(selectDate.time)
                    DOB_input.setText(date)
                },
                getDate.get(Calendar.YEAR),
                getDate.get(Calendar.MONTH),
                getDate.get(Calendar.DAY_OF_MONTH)
            )
            datepicker.datePicker.maxDate=getDate.timeInMillis
            datepicker.show()
        }
        btn_Edit.setOnClickListener {

            if(Edited_User=="Parent")
            {
                parent_InfoEdit()
                println("parent_InfoEdit()")
            }
            else
            {
                children_InfoEdit()
                println("children_InfoEdit()")
            }
            val intent = Intent(this@ModifyChildAndParentInfo, ModifyChildAndParentInfo::class.java)
            intent.putExtra("userId", data)
            overridePendingTransition(0, 0)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)

        }
        btn_back.setOnClickListener {
            val intent :Intent
            if(sessionManager!!.id!="")
            {
                //println("parent Nancy"+sessionManager!!.id)
                intent = Intent(this@ModifyChildAndParentInfo, InvitedParentActivity::class.java)
            }
            else
            {
                //println("creator Nancy")
                intent= Intent(this@ModifyChildAndParentInfo, MainActivity::class.java)
            }
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
            overridePendingTransition(0, 0)
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
    }
    private fun showChildren_EditingPage(data:String){
        databaseRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Name_input.isFocusable=true
                Phone_input.isFocusable=true
                DOB_input.isFocusable=true
                btn_pick_date.isVisible=true
                btn_Edit.isVisible=true
                if(snapshot.child("Children").hasChild(data))
                {
                    photo_user.setImageResource(R.drawable.child)
                    Edited_User="Children"
                    var kid: DataSnapshot =snapshot.child("Children").child(data)
                    original_DOB=kid.child("dob").getValue(true).toString()
                    DOB_input.setText(kid.child("dob").getValue(true).toString())

                    original_Phone=kid.child("phone").getValue(true).toString()
                    Phone_input.setText(kid.child("phone").getValue(true).toString())

                    original_Name=kid.child("username").getValue(true).toString()
                    Name_input.setText(kid.child("username").getValue(true).toString())
                }
                else{
                    photo_user.setImageResource(R.drawable.parents)
                    Edited_User="Parent"
                    if(data==sessionManager!!.id.toString())
                    {
                        Name_input.isFocusable=false
                        Phone_input.isFocusable=false
                        DOB_input.isFocusable=false
                        btn_pick_date.isVisible=false
                        btn_Edit.isVisible=false
                    }
                    var parent: DataSnapshot =snapshot.child("Parent").child(data)
                    original_DOB=parent.child("dob").getValue(true).toString()
                    DOB_input.setText(parent.child("dob").getValue(true).toString())

                    original_Phone=parent.child("phone").getValue(true).toString()
                    Phone_input.setText(parent.child("phone").getValue(true).toString())

                    original_Name=parent.child("username").getValue(true).toString()
                    Name_input.setText(parent.child("username").getValue(true).toString())

                }
                //TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
    private fun parent_InfoEdit(){
        if(Name_input.text.toString()!=""&&Name_input.text.toString()!=original_Name.toString())
        {

                        if (mylist.contains(Name_input.text.toString())) {
                            // use "username" already exists
                            Toast.makeText(
                                this@ModifyChildAndParentInfo,
                                "Username already used by another Parent",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            // "username" does not exist yet.
                            updateDatbase(Name_input.text.toString())
                            updateUI(Name_input.text.toString())
                        }


        }
        else
        {
            println("ZLXZLX parent empty or nochange Name")
            updateDatbase(original_Name.toString())
            updateUI(original_Name.toString())
        }

    }
    private  fun children_InfoEdit(){
        if(Name_input.text.toString()!=""&&Name_input.text.toString()!=original_Name.toString())
        {

                        if (mylist.contains(Name_input.text.toString())) {
                            // use "username" already exists
                            Toast.makeText(
                                this@ModifyChildAndParentInfo,
                                "Username already used by another Kid",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            updateDatbase(Name_input.text.toString())
                            updateUI(Name_input.text.toString())
                            // "username" does not exist yet.
                        }

        }
        else
        {
            println("ZLXZLX child empty or nochange Name")
            updateDatbase(original_Name.toString())
            updateUI(original_Name.toString())
        }

    }
    private fun updateDatbase(name:String){
        if(Edited_User=="Parent"){
            var changeUser=databaseRefParent?.child(data)
            changeUser?.updateChildren(mapOf(
                "username" to name,
                "phone" to Phone_input.text.toString(),
                "dob" to DOB_input.text.toString()
            ))?.addOnSuccessListener { println("zlxzlxzlx pp successful") }?.addOnFailureListener {println("zlxzlxzlx pp fail")  }
        }
        else{
            var changeUser=databaseRefKid?.child(data)
            changeUser?.updateChildren(mapOf(
                "username" to name,
                "phone" to Phone_input.text.toString(),
                "dob" to DOB_input.text.toString()
            ))?.addOnSuccessListener { println("zlxzlxzlx cc successful") }?.addOnFailureListener {println("zlxzlxzlx cc fail")  }
        }
    }
    private fun updateUI(name: String){
//        original_Name?.text=name
//        original_Phone?.text=Phone_input.text.toString()
//        original_DOB?.text=DOB_input.text.toString()

        Name_input.setText(name)
        DOB_input.setText(DOB_input.text.toString())
        Phone_input.setText(Phone_input.text.toString())

    }
    private fun init_FamilyMember(){
        databaseRefParent!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (sessionManager!!.creatorID == item.child("creatorId").value.toString()) {
                        mylist.add(item.child("username").value.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (sessionManager!!.creatorID == item.child("creatorId").value.toString()) {
                        //mylist.clear()
                        mylist.add(item.child("username").value.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
}