package br.com.mukandrew.connectionobserver.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ConnectionObserverReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.run {
            ConnectionObserver.showMessage(this)
        }
    }
}