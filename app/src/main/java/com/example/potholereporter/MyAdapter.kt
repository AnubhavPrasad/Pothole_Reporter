package com.example.potholereporter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference

class MyAdapter(var DataList: MutableList<Data>, var storageReference: StorageReference) :
    RecyclerView.Adapter<MyAdapter.MyviewHolder>() {

    class MyviewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var details = itemview.findViewById<TextView>(R.id.show_details)
        var img = itemview.findViewById<ImageView>(R.id.show_image)
        var latitude = itemview.findViewById<TextView>(R.id.latitude_id)
        var longitude = itemview.findViewById<TextView>(R.id.longitude_id)
        var region = itemview.findViewById<TextView>(R.id.region_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_view, parent, false)
        return MyviewHolder(view)
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        val id = DataList[position].id
        storageReference.child(id).downloadUrl.addOnSuccessListener {
            Glide.with(holder.itemView)
                .load(it)
                .centerCrop()
                .placeholder(R.drawable.loading_image)
                .override(300)
                .into(holder.img)
        }
        holder.latitude.text = DataList[position].latitude
        holder.longitude.text = DataList[position].longitude
        holder.region.text = DataList[position].region
        holder.details.text = DataList[position].desc
    }


}