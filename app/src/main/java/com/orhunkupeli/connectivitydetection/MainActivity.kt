package com.orhunkupeli.connectivitydetection

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.orhunkupeli.connectivitydetector.Connectivity
import com.orhunkupeli.connectivitydetector.ConnectivityBase
import com.orhunkupeli.connectivitydetector.ConnectivityBase.Companion.getConnectivityDetector

class MainActivity : AppCompatActivity(), ConnectivityBase.NetworkStateListener {

    private val provider: ConnectivityBase by lazy { getConnectivityDetector(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            /*Snackbar.make(view, provider..toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun Connectivity.hasInternet(): Boolean {
        return (this as? ConnectivityBase.NetworkState.ConnectedState)?.hasInternet == true
    }

    override fun onStart() {
        super.onStart()
        provider.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }

    override fun onStateChange(state: ConnectivityBase.NetworkState) {
        Snackbar.make(findViewById<View>(android.R.id.content).rootView,
            state.toString(), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }
}