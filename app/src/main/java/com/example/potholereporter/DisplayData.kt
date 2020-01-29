package com.example.potholereporter


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.potholereporter.databinding.FragmentDisplayDataBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_display_data.*

class DisplayData : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var binding:FragmentDisplayDataBinding
    lateinit var dataref:DatabaseReference
    //lateinit var storage: FirebaseStorage
    lateinit var DataList:MutableList<Data>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_display_data,container,false)
        dataref=FirebaseDatabase.getInstance().getReference("FIRST")
        binding.recyclerId.layoutManager=LinearLayoutManager(context)
        //var storageReference=storage.reference
        DataList= mutableListOf()

        dataref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.i("DispalyFragment","Cancelled")
            }
            override fun onDataChange(p0: DataSnapshot) {
                DataList.clear()
                if (p0.exists()) {
                    Log.i("DisplayFragment","Entered")
                    for (h in p0.children) {
                        val first = h.getValue(Data::class.java)
                        DataList.add(first!!)
                    }
                    Log.i("DisplayFragment","Exited")
                    binding.recyclerId.adapter=MyAdapter(DataList)

                }
            }
        })
        return binding.root
    }


}
