package com.udacity

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val biding:ActivityDetailBinding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(biding.root)
        setSupportActionBar(toolbar)
        biding.details.filename.text=intent.getStringExtra(filename)
        biding.details.status.text=intent.getStringExtra(status)
        if(intent.getStringExtra(status).equals("Fail")){
            biding.details.status.setTextColor(Color.parseColor("#FF0000"))
        }
        biding.details.okbutton.setOnClickListener {
            finish()
        }
        val notificationManager = ContextCompat.getSystemService(
            this.applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()
    }
companion object{
    const val status="Status"
    const val filename="FileName"
}
}
