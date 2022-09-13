package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.udacity.utilits.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var Url=""
    private var Filename=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Url= Glide
        Filename=getString(R.string.Glidetext)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(CHANNEL_ID,getString(R.string.notification_channel_title))
        custom_button.setOnClickListener {
              if(custom_button.buttonState==ButtonState.Completed){
                  download()
                  custom_button.buttonState=ButtonState.Loading
              }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context?.apply{
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                id?.apply {
                    val downloadManager=context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    val query=DownloadManager.Query()
                    query.setFilterById(id)
                    val record=downloadManager.query(query)
                    if (record.moveToFirst()){
                        val clolmnindex=record.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        val status=record.getInt(clolmnindex)
                        val notificationManager = ContextCompat.getSystemService(
                            context,
                            NotificationManager::class.java
                        ) as NotificationManager
                        when(status){
                            DownloadManager.STATUS_SUCCESSFUL->{
                                Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
                                notificationManager.sendNotification(Filename,context,"Success")
                            }
                            else->{
                                Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show()
                                notificationManager.sendNotification(Filename,context,"Fail")
                            }
                        }

                    }
                }
            }
            custom_button.buttonState=ButtonState.Completed
        }
    }
    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_description)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(Url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }
    fun buttonclicked(view: View){
        when(view.id){
            R.id.Glideoption->{
                Url= Glide
                Filename=getString(R.string.Glidetext)
            }
            R.id.LoadOption->{
                Url= LoadApp
                Filename=getString(R.string.loadingtext)
            }
            R.id.Retrofitoption->{
                Url= Retrofit
                Filename=getString(R.string.rertofittext)
            }
        }
    }
    companion object {
        private const val Glide =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val LoadApp =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
        private const val Retrofit =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"

        const val CHANNEL_ID = "channelId"
    }

}
