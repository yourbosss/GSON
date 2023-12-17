package com.example.mydialer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val context: Context,
                private val list: ArrayList<Contact>,
                private val json: String,
                private val mainActivity: MainActivity):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textName = view.findViewById<TextView>(R.id.textName)
        val textPhone = view.findViewById<TextView>(R.id.textPhone)
        val textType = view.findViewById<TextView>(R.id.textType)
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textName.text = list[position].name
        holder.textPhone.text = list[position].phone
        holder.textType.text = list[position].type
    }
}