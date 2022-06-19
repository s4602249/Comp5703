package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Child
import comp5703.sydney.edu.au.comp5703_tracker_app.Model.Parent
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.DatabaseReference
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity


class AddParentAndKids : AppCompatActivity() {
    /*declare the variable we gonna use*/
    lateinit var auth: FirebaseAuth
    var databaseRefKid: DatabaseReference? = null
    var databaseRefParent: DatabaseReference? = null
    var database: FirebaseDatabase? = null


    @SuppressLint("WeekBasedYear")
    var formatDate = SimpleDateFormat("dd-MMMM-YYYY", Locale.UK)

    lateinit var familyNameInput: EditText
    lateinit var Phone_input: EditText
    lateinit var DOB_input: EditText
    lateinit var btn_pick_date: ImageButton
    lateinit var radioGroup: RadioGroup
    lateinit var btn_save: Button
    //cope with users item from mainpage
    val users: ArrayList<String> = ArrayList()
    /* Bottom navigation-> reused part for each activity*/
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_home -> {
                val intent = Intent(this@AddParentAndKids, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_person -> {
                val intent = Intent(this@AddParentAndKids, ParentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> {
                val intent = Intent(this@AddParentAndKids, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@AddParentAndKids, SettingMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> {
                val intent = Intent(this@AddParentAndKids, NotifyActivity::class.java)
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
        setContentView(R.layout.activity_add_parent_and_kids)
        /*add firebase instance*/
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefKid = database?.reference!!.child("Children")
        databaseRefParent = database?.reference!!.child("Parent")
        /*find widgets*/
        familyNameInput = findViewById(R.id.familyNameInput)
        radioGroup = findViewById(R.id.radioGroup)
        btn_pick_date = findViewById(R.id.btn_pick_date)
        Phone_input = findViewById(R.id.Phone_input)
        DOB_input = findViewById(R.id.DOB_input)
        btn_save = findViewById(R.id.btn_save)

        var radioButtonValue: String? = "Parent"

        //userlist from mainpage
        val data: ArrayList<String>? =
            intent.getStringArrayListExtra("userList") as ArrayList<String>?
        if (data != null) {
            for (i in data) {
                users.add(i.split(";")[0].split(":")[1].trim())
            }
        }
        println("xxxxxxxxx  userlist "+users)
        //this is the radio group for user type(kid, parent)
        radioGroup.setOnCheckedChangeListener { radioGroup1, i ->
            val rb = findViewById<RadioButton>(i)
            if (rb != null) {
                radioButtonValue = rb.text.toString()
            }
        }
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

        //Click save button
        btn_save.setOnClickListener {
            if (familyNameInput.text.isNullOrEmpty()) {
                familyNameInput.error = "Username cannot be null"
            }
//            else if (Phone_input.text.isNullOrEmpty()) {
//                Phone_input.error = "Phone number cannnot be null"
//            } else if (DOB_input.text.isNullOrEmpty()) {
//                DOB_input.error = "Date of birth cannnot be null"
//            }
            else {
                if (radioButtonValue.equals("Parent")) {
                    AddParent()
                } else if (radioButtonValue.equals("Child")) {
                    AddChild()
                }
            }
            println("xx")
        }

        //bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_home
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)

    }


    /*Add Parent*/
    private fun AddParent() {
        if(users.contains(familyNameInput.text.toString())){
            Toast.makeText(
                this@AddParentAndKids,
                "Username already used by another group member",
                Toast.LENGTH_SHORT).show()
        }
        else{
            val currentUser = auth.currentUser
            val current = System.currentTimeMillis().toString()
            val currentUserDb = databaseRefParent?.child(current) // current time as a ID
            val parent = Parent(
                familyNameInput.text.toString(),
                "2",
                "Parent",
                currentUser!!.uid,
                Phone_input.text.toString(),
                DOB_input.text.toString()
            )
            currentUserDb?.setValue(parent)
            Toast.makeText(
                this@AddParentAndKids,
                "Congrats, parent added successfully",
                Toast.LENGTH_SHORT
            )
                .show()
            startActivity(Intent(this@AddParentAndKids, MainActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
            overridePendingTransition(0, 0)
        }
    }

    /*Add Child*/
    private fun AddChild() {

        /*
        val arraytest : MutableList<Task> = ArrayList()
        arraytest.add(Task("cc","dd",3,0,"no",Date()))
        Thread.sleep(1000)
        arraytest.add(Task("ee","gg",2,0,"yes",Date()))

         */

        if(users.contains(familyNameInput.text.toString())){
            Toast.makeText(
                this@AddParentAndKids,
                "Username already used by another group member",
                Toast.LENGTH_SHORT).show()
        }
        else{
            val currentUser = auth.currentUser
            val current = System.currentTimeMillis().toString()
            val currentUserDb = databaseRefKid?.child(current) // hash ID
            val child = Child(
                familyNameInput.text.toString(),
                "1",
                "Child",
                currentUser!!.uid,
                Phone_input.text.toString(),
                DOB_input.text.toString(),
                0,
                //arraytest
            )
            currentUserDb?.setValue(child)
            Toast.makeText(
                this@AddParentAndKids,
                "Congrats, child added successfully",
                Toast.LENGTH_SHORT
            )
                .show()
            startActivity(Intent(this@AddParentAndKids, MainActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
            overridePendingTransition(0, 0)
        }

    }
}