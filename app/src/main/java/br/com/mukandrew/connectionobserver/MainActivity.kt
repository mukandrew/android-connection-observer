package br.com.mukandrew.connectionobserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.mukandrew.connectionobserver.services.ConnectionObserver

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * If your app is a singleActivity, call it just one time
         * but if is multiActivity, call always on create a new activity
         */
        ConnectionObserver.start(applicationContext, lifecycle)
    }
}
