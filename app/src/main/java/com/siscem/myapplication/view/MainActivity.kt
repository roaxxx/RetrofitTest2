package com.siscem.myapplication.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.itextpdf.text.Document
import com.siscem.myapplication.databinding.ActivityMainBinding
import com.siscem.myapplication.model.Header
import com.siscem.myapplication.model.People
import com.siscem.myapplication.model.PoolDTO
import com.siscem.myapplication.model.rtf.ServiceBuilder
import com.siscem.myapplication.model.gsonConvert.Person
import com.siscem.myapplication.model.rtf.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val persons = mutableListOf<People>()
    private lateinit var adapter: AdapterRV
    private lateinit var binding: ActivityMainBinding
    val title = "Este es el titulo del PDF"
    val body = "Este es un ejemplo de texto para la generación de un pdf\n" +
            "donde cada salto de linea es a su vez un salto de linea en \n" +
            "el documento, esto hace que sea más fácil crear el documento\n" +
            "de acuerdo a las necesidades. También, con esto, se busca \n" +
            "aprender a usar la librería de forma correcta para poder generar\n" +
            "dos pdf que son necesarios para el desarrollo del proyecto."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        getClients()
        if (chekPermission()) {
            Toast.makeText(this, "Ya se cuenta con los permisos", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissons()
        }
    }

    private fun requestPermissons() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
            200
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            if (grantResults.size > 0) {
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permisos listos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Permisos listos", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun chekPermission(): Boolean {
        val permisson1 = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val permisson2 = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
        return permisson1 == PackageManager.PERMISSION_GRANTED && permisson2 == PackageManager.PERMISSION_GRANTED
    }

    private fun getClients() {
        val retrofit = ServiceBuilder.buildService(APIService::class.java)
        retrofit.downloadPeople(PoolDTO("coach_2_pruebas_f_e")).enqueue(
            object : Callback<Person> {
                override fun onResponse(call: Call<Person>, response: Response<Person>) {
                    if (response.body() != null) {
                        val perList = response.body()?.persons ?: emptyList()
                        persons.clear()
                        persons.addAll(perList)
                        addHeaders()
                    }
                }

                override fun onFailure(call: Call<Person>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Hubo un error", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun addHeaders() {
        var header : String? = null
        val headers = mutableListOf<Header>()
        for ( i in 0 until persons.size){
            if(persons[i].rol!=header){
                header = persons[i].rol
                headers.add(Header(i,header))
            }
        }
        headers.forEach { header->
            persons.add(header.position,People("","",header.header))
        }
        adapter.changeData(persons)
    }

    private fun initRecyclerView() {
        adapter = AdapterRV(persons)
        binding.rcv!!.layoutManager = LinearLayoutManager(this)
        binding.rcv!!.adapter = adapter
    }

    fun goNext(view: View) {
        //generatePDF()

            generateClientListPDF()

        //startActivity(Intent(this, example::class.java))
    }

    private fun generateClientListPDF()  {
        val companyName = "My Company"
        val employeeName = "John Doe"


        // Create a new PDF document
        val document = Document()
        val path = Environment.getExternalStorageDirectory().toString()
        //val path = Environment.getExternalStorageDirectory().absolutePath+"/CoachDocuments"

        val dir = File(path)
        Log.d("pathynhio","$dir")
        if(!dir.exists()){
            dir.mkdirs()
        }
        val file = File(Environment.getExternalStorageDirectory(), "/Documents/prueba(2).pdf")

        try{
            val fileOutputStream = FileOutputStream(file)

            PdfWriter.getInstance(document, fileOutputStream)
        }catch (ex: FileNotFoundException){
              Log.d("falla","ds$ex")

        }


        // Open the document for writing
        document.open()
        // Add a title with the company name and employee name
        val title = Paragraph("$companyName - Product List\nPrepared by $employeeName \n\n\n")
        title.alignment = Paragraph.ALIGN_CENTER
        document.add(title)

        // Create a table to hold the product information
        val table = PdfPTable(2)
        table.widthPercentage = 80f

        // Add the headers to the table
        table.addCell("Product Name")
        table.addCell("Price")
        // Add the product information to the table
        for (i in 0 until persons.size) {
            table.addCell(persons[i].name)
            table.addCell(persons[i].rol)

            // Check if the table has 35 rows
            if(table.rows.size % 35 == 0 && i != persons.size - 1){
                document.add(table)
                document.newPage() // Add new page
                table.deleteBodyRows() // Clear table for new page
            }
        }
        // Add the table to the document
        document.add(table)

        // Close the document
        document.close()

    }
}