package com.ankit.broadcastproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bambuser.broadcaster.BroadcastStatus
import com.bambuser.broadcaster.Broadcaster
import com.bambuser.broadcaster.CameraError
import com.bambuser.broadcaster.ConnectionError
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    // ...
    private val APPLICATION_ID = "PLEASE INSERT YOUR APPLICATION SPECIFIC ID PROVIDED BY BAMBUSER"

    private val broadcaster by lazy {
        Broadcast(this, APPLICATION_ID, broadcasterObserver)
    }

    override fun onPause() {
        super.onPause()
        broadcaster.onActivityPause()
    }

    public override fun onResume() {
        super.onResume()
        // ... permission checks, see above
        broadcaster.setCameraSurface(previewSurface)
        broadcaster.onActivityResume()
        if (!hasPermission(Manifest.permission.CAMERA) && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 1)
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        broadcaster.onActivityDestroy()
    }
    private val broadcasterObserver = object : Broadcaster.Observer {
        fun onConnectionStatusChange(broadcastStatus: BroadcastStatus) {
            Log.i("Mybroadcastingapp", "Received status change: $broadcastStatus")
        }
        fun onStreamHealthUpdate(i: Int) {}
        fun onConnectionError(connectionError: ConnectionError, s: String?) {
            Log.i("Mybroadcastingapp","Received connection error: $connectionError, $s")
        }
        fun onCameraError(cameraError: CameraError) {
            Log.i("Mybroadcastingapp","Received camera error: $cameraError")
        }
        fun onChatMessage(s: String) {
            Log.i("Mybroadcastingapp","Received chat messsage: $s")
        }
        fun onResolutionsScanned() {}
        fun onCameraPreviewStateChanged() {}
        fun onBroadcastInfoAvailable(s: String, s1: String) {
            Log.i("Mybroadcastingapp","Received broadcast info: $s, $s1")
        }
        fun onBroadcastIdAvailable(id: String) {
            Log.i("Mybroadcastingapp","Received broadcast id: $id")
        }
    }

    // ...

    private fun hasPermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
