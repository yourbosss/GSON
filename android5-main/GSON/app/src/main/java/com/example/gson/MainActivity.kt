package com.example.gson

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.net.URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.io.InputStream

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
    val request = Request.Builder().url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1").build()

    client.newCall(request).enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                val json = response?.body()?.string()
                val gson = GsonBuilder().create()
                val wrapper: Wrapper = gson.fromJson(json, Wrapper::class.java)
                val jsonSize: Int? = wrapper.photos?.photo?.size
                val listPNG = arrayListOf<Drawable>()
                val listLinks = arrayListOf<String>()

                fiveElements(wrapper, jsonSize, gson)
                fillLists(listPNG, listLinks, wrapper)

                val rView = mainActivity.findViewById<RecyclerView>(R.id.rView)
                val layoutManager = GridLayoutManager(mainActivity, 2)
                val myAdapter = MyAdapter(listPNG, listLinks)

                rView.setHasFixedSize(true)

                mainActivity.runOnUiThread {
                    rView.layoutManager = layoutManager
                    rView.adapter = myAdapter
                }
            }
        }
    })
}

fun fiveElements(wrapper: Wrapper, jsonSize: Int?, gson: Gson) {
    for (index in 0 until jsonSize!!) {
        if (index % 4 == 0) Timber.d(gson.toJson(wrapper.photos?.photo?.get(index)))
    }
}

fun fillLists(listPNG: ArrayList<Drawable>, listLinks: ArrayList<String>, wrapper: Wrapper) {
    var lim: Int = 5
    for (index in 0 .. lim) {
        try {
            var urlStr = "https://farm${wrapper.photos!!.photo[index].farm}.staticflickr.com/${wrapper.photos!!.photo[index].server}/${wrapper.photos!!.photo[index].id}_${wrapper.photos!!.photo[index].secret}_z.jpg"
            var url = URL(urlStr).content as InputStream
            var draw = Drawable.createFromStream(url, "scr name")

            listPNG.add(draw!!)
            listLinks.add(urlStr)
        }
        catch (ex: java.lang.Exception) {
            lim++
            continue
        }
    }
}