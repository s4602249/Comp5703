package comp5703.sydney.edu.au.comp5703_tracker_app.Util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import comp5703.sydney.edu.au.comp5703_tracker_app.R

class horizontal_scrolling: RecyclerView.Adapter<horizontal_scrolling.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.tasklist_row,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 5
    }

    class MyViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){

    }
}