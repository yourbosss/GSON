package com.example.mydialer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.*
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(DebugTree())
        Request(this)
    }
}

fun Request(mainActivity: MainActivity) {
    val client = OkHttpClient()
    val request = Request.Builder().url("https://drive.google.com/u/0/uc?id=1-KO-9GA3NzSgIc1dkAsNm8Dqw0fuPxcR&export=download").build()

    client.newCall(request).enqueue(object : Callback {

        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                val json = response?.body()?.string()
                val gson = GsonBuilder().create()
                var list = gson.fromJson(json, Array<Contact>::class.java).toList() as ArrayList<Contact>
                var copy = list.toList() as ArrayList<Contact>
                val recyclerView = mainActivity.findViewById<RecyclerView>(R.id.rView)
                val adapter = MyAdapter(mainActivity, list, json.toString(), mainActivity)

                mainActivity.runOnUiThread {
                    recyclerView.layoutManager = LinearLayoutManager(mainActivity)
                    recyclerView.adapter = adapter
                }

                val btn_search = mainActivity.findViewById<Button>(R.id.btn_search)
                val search_text = mainActivity.findViewById<EditText>(R.id.et_search)

                btn_search.setOnClickListener {
                    var text = search_text.text.toString()

                    list.clear()
                    list.addAll(copy)

                    list.filter{ text !in it.name }.forEach{ list.remove(it)}
                    adapter.notifyDataSetChanged()
                }
            }
        }
    })

}