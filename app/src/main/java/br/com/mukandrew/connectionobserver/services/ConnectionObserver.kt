package br.com.mukandrew.connectionobserver.services

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

object ConnectionObserver {

    private var toast: Toast? = null
    private var hasConnection: Boolean = false
    private var mLifecycle: Lifecycle? = null
    private var appContext: WeakReference<Context>? = null
    private val connectionManagerReceiver = ConnectionObserverReceiver()

    @SuppressLint("ShowToast")
    fun start(applicationContext: Context, lifecycle: Lifecycle) {
        // Init broadReceiver and create a WeakReference for AppContext
        appContext = applicationContext.let {
            it.registerReceiver(
                connectionManagerReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
            WeakReference(it)
        }
        // set the lifecycleObserver for kill observer when app is closed
        mLifecycle = lifecycle.apply {
            addObserver(lifecycleObserver)
        }

        // This can be removed, just to init a toast for use later
        toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT)
    }

    // use this or another case to show connection message
    fun showMessage(applicationContext: Context) {
        when {
            !isConnected(applicationContext) -> {
                if (hasConnection) {
                    hasConnection = false
                    toast?.run {
                        setText("NO CONNECTION")
                        show()
                    }
                }
            }
            isWifi(applicationContext) -> {
                hasConnection = true
                toast?.run {
                    setText("WIFI CONNECTION")
                    show()
                }
            }
            isMobile(applicationContext) -> {
                hasConnection = true
                toast?.run {
                    setText("MOBILE CONNECTION")
                    show()
                }
            }
        }
    }

    private val lifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResumeListener() {
            appContext?.get()?.registerReceiver(
                connectionManagerReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPauseListener() {
            appContext?.get()?.unregisterReceiver(connectionManagerReceiver)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyListener() {
            mLifecycle?.removeObserver(this)
        }
    }

    private fun getConnectivityService(applicationContext: Context): ConnectivityManager? {
        return (applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
    }

    private fun isConnected(applicationContext: Context): Boolean {
        return getConnectivityService(applicationContext)?.activeNetworkInfo?.isConnected == true
    }

    private fun isWifi(applicationContext: Context): Boolean {
        return getConnectivityService(applicationContext)?.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
    }

    private fun isMobile(applicationContext: Context): Boolean {
        return getConnectivityService(applicationContext)?.activeNetworkInfo?.type == ConnectivityManager.TYPE_MOBILE
    }
}