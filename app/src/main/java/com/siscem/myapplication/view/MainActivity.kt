package com.siscem.myapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.siscem.myapplication.databinding.ActivityMainBinding
import com.siscem.myapplication.model.People
import com.siscem.myapplication.model.PoolDTO
import com.siscem.myapplication.model.rtf.ServiceBuilder
import com.siscem.myapplication.model.gsonConvert.Person
import com.siscem.myapplication.model.rtf.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val  persons = mutableListOf<People>()
    private lateinit var adapter: AdapterRV
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        //getClients()
        val retrofit = ServiceBuilder.buildService(APIService::class.java)
        retrofit.downloadPeople(PoolDTO("coach_2_pruebas_f_e")).enqueue(
            object:Callback<Person>{
                override fun onResponse(call: Call<Person>, response: Response<Person>) {
                    if(response.body()!= null){
                        Log.d("TAG","${response.body()?.People?.size}")
                        val perList = response.body()?.People ?: emptyList()
                        persons.clear()
                        persons.addAll(perList)
                        adapter.notifyDataSetChanged()
                    }
                }
                override fun onFailure(call: Call<Person>, t: Throwable) {
                    Log.d("TAG","$t")
                    Toast.makeText(this@MainActivity,"Hubo un error",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun initRecyclerView() {
        adapter = AdapterRV(persons)
        binding.rcv!!.layoutManager = LinearLayoutManager(this)
        binding.rcv!!.adapter = adapter
    }


    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://192.168.10.10:8081/COACH_API_P/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
  /*
    private fun getClients(){
        val pool = PoolDTO("coach_2_pruebas_f_e")
        Toast.makeText(this@MainActivity,"Bien acá",Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            val response = getRetrofit().create(APIService::class.java).downloadPeople(pool)
            //val personss = response.body()
            runOnUiThread{

                if(response.isSuccessful){
                    Toast.makeText(this@MainActivity,"Es correcta",Toast.LENGTH_SHORT).show()
                   /* val perList = personss?.people ?: emptyList()
                    persons.clear()
                    persons.addAll(perList)*/
                    //adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@MainActivity,"Hay un error",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/
}