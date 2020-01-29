package com.example.potholereporter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var DataList:MutableList<Data>): RecyclerView.Adapter<MyAdapter.MyviewHolder>() {

    class MyviewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
     var txt=itemview.findViewById<TextView>(R.id.show_details)
        var img=itemview.findViewById<ImageView>(R.id.show_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.item_view,parent,false)
        return MyviewHolder(view)
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {

        holder.txt.text="${DataList[position].region}\n ${DataList[position].desc}"

    }


}