package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Person

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import comp5703.sydney.edu.au.comp5703_tracker_app.Util.SessionManager
import kotlinx.android.synthetic.main.activity_child_model.view.*
import kotlinx.android.synthetic.main.activity_list_child.*
import kotlinx.android.synthetic.main.activity_parent.*
import kotlinx.android.synthetic.main.chil_num.view.*

class ChildActivityList : AppCompatActivity() {

    var database: FirebaseDatabase? = null
    var databaseRefs: DatabaseReference? = null
    var sessionManager: SessionManager? = null
    var backButton: Button ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_child)
        database = FirebaseDatabase.getInstance()
        databaseRefs =database?.reference

        sessionManager = SessionManager(applicationContext)

        getChildActivityList(sessionManager!!.id.toString())
    }

    private fun getChildActivityList(id: String) {
        backButton = findViewById(R.id.backButton)

        databaseRefs!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("Children").hasChild(id)) {

                    val adapter= GroupAdapter<ViewHolder>()
                    var kid: DataSnapshot = snapshot.child("Children").child(id)

                    adapter.add(EmptySpace())

                    if (kid.child("completedTask").getValue(true) != null) {

                        /*
                        val completedTask: ArrayList<HashMap<String, String>> = kid.child("completedTask").getValue() as ArrayList<HashMap<String, String>>
                        completedTask.sortWith(compareByDescending{((it.get("completedTime") as HashMap<String, Long>).get("time"))})
                        for (item: HashMap<String, String> in completedTask) {
                            adapter.add(ActivityItem(pointJudge(item.get("score").toString().toInt()), item.get("title").toString()))
                        }

                        var completedTask: ArrayList<HashMap<DataSnapshot, Long>> = ArrayList()
                        for(item:DataSnapshot in kid.child("completedTask").children) {
                            val tempHashMap: HashMap<DataSnapshot, Long> = HashMap()
                            tempHashMap.put(item, item.child("completedTime").child("time").getValue(true).toString().toLong())
                            completedTask?.add(tempHashMap)
                        }
                        println("before "+completedTask)
                        //completedTask.sortWith(compareByDescending{(it.get("completedTask") as HashMap<DataSnapshot,Long>).child("completedTime").child("time").getValue(true).toString().toLong())})
                        completedTask.sortWith(compareByDescending{it.get(it.)})
                        println("after "+completedTask)

                         */

                        var completedTask: ArrayList<HashMap<String, String>> = ArrayList()
                        for(item:DataSnapshot in kid.child("completedTask").children) {
                            val tempHashMap: HashMap<String, String> = HashMap()
                            tempHashMap.put("score", item.child("score").getValue(true).toString())
                            tempHashMap.put("title", item.child("title").getValue(true).toString())
                            tempHashMap.put("time", item.child("completedTime").child("time").getValue(true).toString())
                            completedTask?.add(tempHashMap)
                        }
                        completedTask.sortWith(compareByDescending{(it.get("time")?.toLong())})
                        for (item: HashMap<String, String> in completedTask) {
                            adapter.add(ActivityItem(pointJudge(item.get("score").toString().toInt()), item.get("title").toString()))
                        }
                    }

                    childActivityList.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        backButton?.setOnClickListener(){
            val intent = Intent(this@ChildActivityList, ChildActivity::class.java)
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

    private fun pointJudge(score : Int):String {
        if (score == -1) {
            return "Foul"
        }
        if (score == 1) {
            return "Free-throw"
        }
        if (score == 2) {
            return "Basket"
        }
        if (score == 3) {
            return "3-pointer"
        }
        return "No Feedback"
    }

    class ActivityItem(val score:String, val activityName:String) : Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.score.text = score
            viewHolder.itemView.activityName.text = activityName
        }

        override fun getLayout(): Int {
            return R.layout.activity_child_model
        }
    }

    class EmptySpace() : Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {

        }

        override fun getLayout(): Int {
            return R.layout.empty_space
        }
    }

}