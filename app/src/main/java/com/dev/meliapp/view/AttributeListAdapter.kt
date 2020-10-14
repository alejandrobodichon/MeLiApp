package com.dev.meliapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dev.meliapp.R
import com.dev.meliapp.databinding.ItemDescriptionBinding
import com.dev.meliapp.model.Attribute

class AttributeListAdapter(private val attributeList: ArrayList<Attribute>):
    RecyclerView.Adapter<AttributeListAdapter.AttributeViewHolder>() {

    class AttributeViewHolder(var view: ItemDescriptionBinding): RecyclerView.ViewHolder(view.root)

    fun updateAttributeList(newAttributeList: List<Attribute>){
        attributeList.clear()
        attributeList.addAll(newAttributeList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemDescriptionBinding>(inflater, R.layout.item_description,parent,false)
        return AttributeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttributeViewHolder, position: Int) {
        holder.view.attribute = attributeList[position]
    }

    override fun getItemCount() = attributeList.size

}