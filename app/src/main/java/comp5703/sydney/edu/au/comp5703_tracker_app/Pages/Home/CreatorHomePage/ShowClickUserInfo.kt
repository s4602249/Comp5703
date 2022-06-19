package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify.NotifyActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.Logout_sample
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R

class ShowClickUserInfo : AppCompatActivity() {

    //数据库三件套
    lateinit var auth: FirebaseAuth
    var databaseRefKid: DatabaseReference? = null
    var databaseRefParent: DatabaseReference? = null
    var database: FirebaseDatabase? = null


    lateinit var userId_txtview: TextView
    lateinit var userName_txtview: TextView
    lateinit var userPhone_txtview: TextView
    lateinit var userBth_txtview: TextView
    lateinit var qrCode: ImageView
    /* Bottom navigation-> reused part for each activity*/
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_home -> {
                val intent = Intent(this@ShowClickUserInfo, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_person -> {
                val intent = Intent(this@ShowClickUserInfo, ParentsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_help -> {
                val intent = Intent(this@ShowClickUserInfo, Logout_sample::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_setting -> {
                val intent = Intent(this@ShowClickUserInfo, SettingMainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.ic_notify -> {
                val intent = Intent(this@ShowClickUserInfo, NotifyActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                overridePendingTransition(0, 0)
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_click_user_info)
        userId_txtview=findViewById(R.id.userId_txtview)
        userName_txtview=findViewById(R.id.userName_txtview)
        userPhone_txtview=findViewById(R.id.userPhone_txtview)
        userBth_txtview=findViewById(R.id.userBth_txtview)
        //create database instance
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRefKid = database?.reference!!.child("Children")
        databaseRefParent = database?.reference!!.child("Parent")
        //create session manager to remember session
        qrCode=findViewById(R.id.QRcode)
        //data is uid
        val data:String = intent.getStringExtra("accountID").toString()
        //get username intent
        val username:String = intent.getStringExtra("username").toString().split(";")[0]
        userId_txtview.text = "User ID:   $data"
        userName_txtview.text="Username:  $username"



        //find kids and parents in database according to the id and update textview info
        databaseRefKid?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (data == item.key.toString()) {
                        userPhone_txtview.text="User phone:   "+item.child("phone").value.toString()
                        userBth_txtview.text="User birthday:   "+item.child("dob").value.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        databaseRefParent?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children) {
                    if (data == item.key.toString()) {
                        userPhone_txtview.text="User phone:   "+item.child("phone").value.toString()
                        userBth_txtview.text="User birthday:   "+item.child("dob").value.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })




        val writer=QRCodeWriter()
        try{
            //encode matrix
            val bitMatrix=writer.encode(data, BarcodeFormat.QR_CODE,550,550)
            val width=bitMatrix.width
            val height=bitMatrix.height
            //generate matrix
            val bmp= Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
            for(x in 0 until width){
                for(y in 0 until height){
                    bmp.setPixel(x,y,
                        if(bitMatrix[x,y])  Color.parseColor("#00688B") else Color.WHITE)
                }

            }
            //show in the image view
            qrCode.setImageBitmap(bmp)
        }
        catch(e:WriterException){

        }
        //bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_home
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
    }





}