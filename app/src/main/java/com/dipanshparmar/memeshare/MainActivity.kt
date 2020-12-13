package com.dipanshparmar.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    private var url: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    private fun loadMeme() {
        findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val imgUrl = response.getString("url")
            Glide.with(this).load(imgUrl).listener(object: RequestListener<Drawable> {

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                    Toast.makeText(MainActivity(), "Error fetching meme...", Toast.LENGTH_LONG).show()
                    return false
                }
            }).into(findViewById(R.id.mainMeme))
        }, {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show()
        })

        queue.add(jsonObjectRequest)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey! checkout this cool meme I found on Reddit: $url")
        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }

}