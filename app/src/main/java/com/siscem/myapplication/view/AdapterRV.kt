package com.siscem.myapplication.view

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siscem.myapplication.R
import com.siscem.myapplication.model.People

class AdapterRV(val people: MutableList<People>):
    RecyclerView.Adapter<AdapterRV.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.person_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = people.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(people[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name= view.findViewById<TextView>(R.id.name)
        val rol= view.findViewById<TextView>(R.id.rol)
        //Función que incializa los componentes de la vista reciclada
        fun render(people: People) {
            name.text = people.name
            rol.text = people.rol
        }

    }
}