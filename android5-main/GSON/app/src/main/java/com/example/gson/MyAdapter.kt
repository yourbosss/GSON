package com.example.gson

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class MyAdapter(private val itemList: List<Drawable>, private  val linksList: List<String>)
    : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    override fun getItemCount() = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, null)
        return MyViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.topic.setImageDrawable(itemList[position])
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var topic: ImageView

        init {
            itemView.setOnClickListener(this)
            topic = itemView.findViewById<View>(R.id.imageView) as ImageView
        }

        override fun onClick(view: View) {
            Toast.makeText(view.context, "copied", Toast.LENGTH_SHORT).show()
            Timber.i("Тут ссылка")
        }
    }
}