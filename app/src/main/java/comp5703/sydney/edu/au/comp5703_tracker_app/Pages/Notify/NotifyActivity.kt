package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Notify
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage.MainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage.InvitedChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage.InvitedParentActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ChildActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person.ParentsActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.SplashScreenActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingChildrenActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting.SettingMainActivity
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import kotlin.collections.ArrayList

class NotifyActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseRefKid: DatabaseReference? = null
    var databaseRefParent: DatabaseReference? = null
    var databaseRefTask: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    var sessionManager: SessionManager? = null
    var mylist: ArrayList<String> = ArrayList()
    var myAdapter: RecordAdapter? = null
    var taskIdList: ArrayList<String> = ArrayList()
    var taskNameList: ArrayList<String> = ArrayList()
    var childId: String = ""
    var score: String = ""
    var title: String = ""
    var creatorId: String = ""
    var username: String = ""
    var role: String = ""
    var record_type: String = ""
    var record_opt: String = ""
    var record_from: String = ""
    var record_time: String = ""
    var record_name: String = ""
    var record_list: ArrayList<String> = ArrayList()
    var databaseRefRecord: DatabaseReference? = null
    var intent_kid_parent: String = ""
    lateinit var record_null_show: TextView
    lateinit var listOfRecord: ListView
    lateinit var listofgoals: ListView
    var record_list_t: List<String> = ArrayList()
    var mylist_t: List<String> = ArrayList()
    var record_from_list: ArrayList<String> = ArrayList()
    var record_from_list1: ArrayList<String> = ArrayList()
    var record_to_list: ArrayList<String> = ArrayList()

    /* Bottom navigation-> reused part for each activity*/
    private val navigation = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.ic_notify -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.ic_person -> {
                if (auth.currentUser == null) {
                    if (intent_kid_parent == "child") {
                        var intent = Intent(this@NotifyActivity, ChildActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    } else {
                        var intent = Intent(this@NotifyActivity, ParentsActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                } else {
                    var intent = Intent(this@NotifyActivity, ParentsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }

            }
            R.id.ic_setting -> {
                if (intent_kid_parent == "child") {
                    val intent = Intent(this@NotifyActivity, SettingChildrenActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                } else {
                    val intent = Intent(this@NotifyActivity, SettingMainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.ic_home -> {
                if (auth.currentUser == null) {
                    if (intent_kid_parent == "child") {
                        var intent = Intent(this@NotifyActivity, InvitedChildActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    } else {
                        var intent = Intent(this@NotifyActivity, InvitedParentActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }

                } else {
                    var intent = Intent(this@NotifyActivity, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }

            }

            R.id.ic_help -> { // it is a test, simply this to different activity
                val intent = Intent(this@NotifyActivity, SplashScreenActivity::class.java)
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
        setContentView(R.layout.activity_notify_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.buttom_navigation)
        bottomNavigationView.selectedItemId = R.id.ic_notify
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation)
        sessionManager = SessionManager(applicationContext)
        record_null_show = findViewById(R.id.notify_text_records)
        listOfRecord = findViewById(R.id.notify_list_records)
        /*create database instance and firebase auth*/
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        databaseRefKid = database?.reference!!.child("Children")
        databaseRefParent = database?.reference!!.child("Parent")
        databaseRefTask = database?.reference!!.child("Task")
        databaseRefRecord = database?.reference!!.child("Record")
        //Log.i("Tag","${record1.record_time}/${record2.record_time}")
        user_detect()


    }

    private fun child_query(Id: String) {
        databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (item in dataSnapshot.children) {
                    //Log.i("TAG", item.key.toString())
                    //Log.i("TAG", item.value.toString())
                    if (item.key.equals(Id)) {
                        creatorId = item.child("creatorId").value.toString()
                        role = item.child("role").value.toString()
                        username = item.child("username").value.toString()
                    } else {

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }

        })
    }

    private fun task_show(Id: String) {
        databaseRefTask!!.child(Id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (item in dataSnapshot.children) {
                    // Log.i("TAG", item.key.toString())
                    // Log.i("TAG", item.value.toString())
                    childId = item.child("childId").value.toString()
                    title = item.child("title").value.toString()
                    score = item.child("score").value.toString()

                    mylist.add("$username:   $title  $score")
                    myAdapter!!.notifyDataSetChanged()
                    listOfRecord.adapter = myAdapter

                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }

        })
    }

    /*set up long click listener and click listener*/
    private fun setUpListViewClickListener() {
        listOfRecord.setOnItemLongClickListener { parent, view, position, id ->
            val builder = AlertDialog.Builder(this@NotifyActivity)
            builder.setTitle("Delete a record").setMessage("Do you want to delete this record?")
                .setPositiveButton("Delete") { dialogInterface, i ->
                    databaseRefRecord!!.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var click_on = record_list[position]
                            for (item in snapshot.children) {
                                if (item.key.toString() == click_on) {
                                    mylist.removeAt(position)
                                    myAdapter?.notifyDataSetChanged()
                                    record_list.removeAt(position)
                                    item.ref.removeValue()
                                    Toast.makeText(
                                        this@NotifyActivity,
                                        "Record successfully deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                }
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    //Nothing happens here
                }
            builder.create().show()
            return@setOnItemLongClickListener (true)

        }

    }

    private fun kid_from_to(record_from: String): String {
        var record_to: String = ""
        databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    if (record_from == item.key.toString()) {
                        record_to = item.child("creatorId").value.toString()


                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return record_to
    }

    private fun parent_from_to(record_from: String): String {
        var record_to: String = ""
        databaseRefParent!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    if (record_from == item.key.toString()) {
                        record_to = item.child("creatorId").value.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return record_to
    }

    private fun user_detect() {

        if (auth.currentUser == null) {
            databaseRefKid!!.addListenerForSingleValueEvent(object : ValueEventListener {
                //child login
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.hasChild(sessionManager?.id.toString())) {


                        intent_kid_parent = "child"

                        var record_num = 0


                        databaseRefRecord!!.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (item in snapshot.children) {


                                    var record_from = item.child("record_from").value.toString()
                                    var record_type = item.child("record_type").value.toString()
                                    var record_time = item.key.toString()
                                    var record_name = item.child("record_name").value.toString()
                                    databaseRefKid!!.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            for (item in snapshot.children) {
                                                if (record_from == item.key.toString()) {
                                                    var record_to =
                                                        item.child("creatorId").value.toString()
                                                    var user_name =
                                                        item.child("username").value.toString()
                                                    databaseRefKid!!.addListenerForSingleValueEvent(
                                                        object :
                                                            ValueEventListener {
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                for (item in snapshot.children) {

                                                                    if (sessionManager?.id.toString() == item.key.toString()) {
                                                                        var session_id =
                                                                            item.child("creatorId").value.toString()

                                                                        if (record_to == session_id) {


                                                                            record_num += 1

                                                                        } else {

                                                                        }
                                                                    }
                                                                }

                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                TODO("Not yet implemented")
                                                            }
                                                        })


                                                }


                                            }


                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                        databaseRefRecord!!.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            var record_num1 = 0
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (item in snapshot.children) {


                                    var record_from = item.child("record_from").value.toString()
                                    var record_type = item.child("record_type").value.toString()
                                    var record_time = item.key.toString()
                                    var record_name = item.child("record_name").value.toString()
                                    databaseRefKid!!.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            for (item in snapshot.children) {
                                                if (record_from == item.key.toString()) {
                                                    var record_to =
                                                        item.child("creatorId").value.toString()
                                                    var user_name =
                                                        item.child("username").value.toString()
                                                    databaseRefKid!!.addListenerForSingleValueEvent(
                                                        object :
                                                            ValueEventListener {
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                for (item in snapshot.children) {

                                                                    if (sessionManager?.id.toString() == item.key.toString()) {
                                                                        var session_id =
                                                                            item.child("creatorId").value.toString()

                                                                        if (record_to == session_id) {

                                                                            record_list.add(
                                                                                record_time
                                                                            )


                                                                            when (record_type) {
                                                                                "Task" -> mylist.add(
                                                                                    "$user_name submitted $record_name"
                                                                                )

                                                                                "Goal" -> mylist.add(
                                                                                    "$user_name achieved $record_name"
                                                                                )
                                                                            }

                                                                            record_num1 += 1

                                                                            if (record_num - record_num1 == 0) {
                                                                                Log.i(
                                                                                    "record",
                                                                                    record_list.toString()
                                                                                )
                                                                                record_list =
                                                                                    ArrayList(
                                                                                        record_list.reversed()
                                                                                    )
                                                                                mylist =
                                                                                    ArrayList(mylist.reversed())
                                                                                myAdapter =
                                                                                    RecordAdapter(
                                                                                        this@NotifyActivity,
                                                                                        R.layout.notification_list_item,
                                                                                        mylist
                                                                                    )
                                                                                myAdapter!!.notifyDataSetChanged()
                                                                                listOfRecord.adapter =
                                                                                    myAdapter
                                                                            }

                                                                        } else {

                                                                        }
                                                                    }
                                                                }

                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                TODO("Not yet implemented")
                                                            }
                                                        })


                                                }


                                            }


                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })


                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })
            databaseRefParent!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(sessionManager?.id.toString())) {
                        intent_kid_parent = "parent"
                        setUpListViewClickListener()
//show the list of record

                        var record_num = 0
                        databaseRefRecord!!.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (item in snapshot.children) {


                                    var record_from = item.child("record_from").value.toString()
                                    var record_type = item.child("record_type").value.toString()
                                    var record_time = item.key.toString()
                                    var record_name = item.child("record_name").value.toString()
                                    databaseRefKid!!.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            for (item in snapshot.children) {
                                                var user_name =
                                                    item.child("username").value.toString()
                                                if (record_from == item.key.toString()) {
                                                    var record_to =
                                                        item.child("creatorId").value.toString()

                                                    databaseRefParent!!.addListenerForSingleValueEvent(
                                                        object :
                                                            ValueEventListener {
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                for (item in snapshot.children) {
                                                                    if (sessionManager?.id.toString() == item.key.toString()) {
                                                                        var session_id =
                                                                            item.child("creatorId").value.toString()
                                                                        if (record_to == session_id) {

                                                                            record_num += 1


                                                                        } else {

                                                                        }
                                                                    }
                                                                }

                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                TODO("Not yet implemented")
                                                            }
                                                        })


                                                }


                                            }


                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                        databaseRefRecord!!.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            var record_num1 = 0
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (item in snapshot.children) {


                                    var record_from = item.child("record_from").value.toString()
                                    var record_type = item.child("record_type").value.toString()
                                    var record_time = item.key.toString()
                                    var record_name = item.child("record_name").value.toString()
                                    databaseRefKid!!.addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            for (item in snapshot.children) {
                                                var user_name =
                                                    item.child("username").value.toString()
                                                if (record_from == item.key.toString()) {
                                                    var record_to =
                                                        item.child("creatorId").value.toString()

                                                    databaseRefParent!!.addListenerForSingleValueEvent(
                                                        object :
                                                            ValueEventListener {
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                for (item in snapshot.children) {
                                                                    if (sessionManager?.id.toString() == item.key.toString()) {
                                                                        var session_id =
                                                                            item.child("creatorId").value.toString()
                                                                        if (record_to == session_id) {
                                                                            record_list.add(
                                                                                record_time
                                                                            )


                                                                            when (record_type) {
                                                                                "Task" -> mylist.add(
                                                                                    "$user_name submitted $record_name"
                                                                                )

                                                                                "Goal" -> mylist.add(
                                                                                    "$user_name achieved $record_name"
                                                                                )
                                                                            }

                                                                            record_num1 += 1


                                                                            if (record_num - record_num1 == 0) {


                                                                                record_list =
                                                                                    ArrayList(
                                                                                        record_list.reversed()
                                                                                    )
                                                                                mylist =
                                                                                    ArrayList(mylist.reversed())
                                                                                Log.i(
                                                                                    "record_list",
                                                                                    record_list.toString()
                                                                                )
                                                                                Log.i(
                                                                                    "mylist",
                                                                                    mylist.toString()
                                                                                )
                                                                                myAdapter =
                                                                                    RecordAdapter(
                                                                                        this@NotifyActivity,
                                                                                        R.layout.notification_list_item,
                                                                                        mylist
                                                                                    )
                                                                                myAdapter!!.notifyDataSetChanged()
                                                                                listOfRecord.adapter =
                                                                                    myAdapter

                                                                            }


                                                                        } else {

                                                                        }
                                                                    }
                                                                }

                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                TODO("Not yet implemented")
                                                            }
                                                        })


                                                }


                                            }


                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        } else {
            setUpListViewClickListener()
            var record_num = 0
            databaseRefRecord!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (item in snapshot.children) {


                        var record_from = item.child("record_from").value.toString()
                        var record_type = item.child("record_type").value.toString()
                        var record_time = item.key.toString()
                        var record_name = item.child("record_name").value.toString()
                        databaseRefKid!!.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                for (item in snapshot.children) {
                                    var user_name = item.child("username").value.toString()
                                    if (record_from == item.key.toString()) {
                                        var record_to = item.child("creatorId").value.toString()

                                        if (record_to == auth.currentUser?.uid.toString()) {


                                            record_num += 1


                                        } else {

                                        }


                                    }


                                }


                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }

                }


                override fun onCancelled(error: DatabaseError) {

                }
            })
            databaseRefRecord!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var record_num1 = 0
                    for (item in snapshot.children) {


                        var record_from = item.child("record_from").value.toString()
                        var record_type = item.child("record_type").value.toString()
                        var record_time = item.key.toString()
                        var record_name = item.child("record_name").value.toString()
                        databaseRefKid!!.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                for (item in snapshot.children) {
                                    var user_name = item.child("username").value.toString()
                                    if (record_from == item.key.toString()) {
                                        var record_to = item.child("creatorId").value.toString()

                                        if (record_to == auth.currentUser?.uid.toString()) {


                                            record_list.add(record_time)


                                            when (record_type) {
                                                "Task" -> mylist.add("$user_name submitted $record_name")

                                                "Goal" -> mylist.add("$user_name achieved $record_name")
                                            }

                                            record_num1 += 1

                                            if (record_num - record_num1 == 0) {
                                                Log.i("record", record_list.toString())
                                                record_list = ArrayList(record_list.reversed())
                                                mylist = ArrayList(mylist.reversed())
                                                myAdapter = RecordAdapter(
                                                    this@NotifyActivity,
                                                    R.layout.notification_list_item,
                                                    mylist
                                                )
                                                myAdapter!!.notifyDataSetChanged()
                                                listOfRecord.adapter = myAdapter
                                            }

                                        } else {

                                        }


                                    }


                                }


                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }

                }


                override fun onCancelled(error: DatabaseError) {

                }
            })

        }
    }
}




    class RecordAdapter(activity: Activity, val resourceId: Int, data: List<String>) :
        ArrayAdapter<String>(activity, resourceId, data) {

        lateinit var auth: FirebaseAuth
        var database: FirebaseDatabase? = null
        var databaseRefKid: DatabaseReference? = null
        var databaseRefParent: DatabaseReference? = null


        fun itemlistchange(itemList : ArrayList<String> , itemList1 : ArrayList<String> , item : String, ) : ArrayList<String> {
            //link two
            var itemListadd = itemList + itemList1
            //remove item
            var itemListadda =  ArrayList<String>(itemListadd)


            itemListadda.remove(item)
            //reverse list
            return ArrayList(itemListadda.reversed())
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val record_item: TextView = view.findViewById(R.id.recordListItemTitle)

            val record = getItem(position)
            if (record != null) {
                record_item.text = record

            }
            return view
        }
    }








