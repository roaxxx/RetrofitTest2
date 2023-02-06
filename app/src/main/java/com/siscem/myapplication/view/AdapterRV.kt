package com.siscem.myapplication.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.siscem.myapplication.R
import com.siscem.myapplication.model.People

class AdapterRV(val people: MutableList<People>):
    RecyclerView.Adapter<ViewHolder>(){
    var header = ""
    val ITEM_LIST = 0
    val HEADER = 1

    @SuppressLint("NotifyDataSetChanged")
    fun changeData(persons: MutableList<People>){
        people.clear()
        people.addAll(persons)
        this.notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType==ITEM_LIST){
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.person_list, parent, false)
            return ViewHolderItem(v)
        }else{
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.header_list, parent, false)
            return HeaderViewHolder(v)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(people[position].id==""){
            return HEADER
        }else{
            return ITEM_LIST
        }
    }

    override fun getItemCount(): Int = people.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder is ViewHolderItem){
            holder.render(people[position])
        }else if(holder is HeaderViewHolder) {
            holder.render(people[position])
        }else{
            Log.d("","Error onBindViewHolder")
        }

    }

    inner class ViewHolderItem(view: View):ViewHolder(view){
        val name= view.findViewById<TextView>(R.id.name)
        val rol= view.findViewById<TextView>(R.id.rol)
        //Función que incializa los componentes de la vista reciclada
        fun render(people: People) {
            name.text = people.name
            rol.text = people.rol
        }

    }
    inner class HeaderViewHolder(view: View): ViewHolder(view){
        val header = view.findViewById<TextView>(R.id.headerTv)
        //Función que incializa los componentes de la vista reciclada
        fun render(people: People) {
            header.text = people.rol
        }
    }
}