package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import comp5703.sydney.edu.au.comp5703_tracker_app.R

//for Parent and Creator to use
class ParentAdapter(activity: Activity, val resourceId: Int, data: ArrayList<String>) :
    ArrayAdapter<String>(activity, resourceId, data) {
    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
        val image: ImageView = view.findViewById(R.id.imageOfItem)
        val role: TextView = view.findViewById(R.id.roleOfItem)
        val username: TextView = view.findViewById(R.id.nameOfItem)
        val roleOfUser = getItem(position)!!.split(":")[0]
        // 添加了一个中间变量转化函数
        var middleChange=getItem(position)!!.split(":")[1].trim()
        val nameOfUser = middleChange.split(";")[0]
        var idOfUser= middleChange.split(";")[1].trim()
        //  mylist.add("Child:" + "       " + item.child("username").value.toString())

        val modifyButton: ImageView = view.findViewById(R.id.settingBtn)
        modifyButton.setOnClickListener {
            println("zlxzlxzlx"+idOfUser)
            context.startActivity(Intent(context, ModifyChildAndParentInfo::class.java).putExtra("userId",idOfUser))
        }

        if (roleOfUser == "Parent") {
            image.setImageResource(R.drawable.parents);
            role.text = "Parent"
            username.text = nameOfUser
        }
        if (roleOfUser == "Child") {
            image.setImageResource(R.drawable.child);
            role.text = "Child"
            username.text = nameOfUser
        }
        return view
    }
}


