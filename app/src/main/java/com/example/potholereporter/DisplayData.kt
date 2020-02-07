package com.example.potholereporter


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.potholereporter.databinding.FragmentDisplayDataBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class DisplayData : Fragment() {
    lateinit var binding: FragmentDisplayDataBinding
    lateinit var dataref: DatabaseReference
    lateinit var DataList: MutableList<Data>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_display_data, container, false)
        DataList = mutableListOf()
        dataref = FirebaseDatabase.getInstance().getReference("FIRST")

        binding.recyclerId.layoutManager = LinearLayoutManager(context)
        binding.progressBar5.visibility = View.VISIBLE
        val storageReference = FirebaseStorage.getInstance().getReference("FIRST")
        dataref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("DispalyFragment", "Cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    DataList.clear()
                    for (h in p0.children) {
                        val first = h.getValue(Data::class.java)
                        if (first != null) {
                            DataList.add(first)
                        }
                    }
                    binding.progressBar5.visibility = View.GONE
                    binding.recyclerId.adapter = MyAdapter(DataList, storageReference)
                }
            }
        })
        return binding.root
    }

}
