package com.spare.wifidirecttestapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), WifiP2pManager.PeerListListener {

    private lateinit var wifiP2pManager: WifiP2pManager
    private lateinit var wifiP2pChannel: WifiP2pManager.Channel
    private lateinit var wifiP2pReceiver: BroadcastReceiver

    var isWifiP2pEnabled = false

    private val intentFilter = IntentFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            wifiP2pManager.discoverPeers(wifiP2pChannel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    // discovery has been successfully started
                    Toast.makeText(this@MainActivity, "Discovery Initiated", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(reasonCode: Int) {
                    // discovery failed to start. checkout reason code
                    Toast.makeText(this@MainActivity, "Discovery Failed : " + reasonCode, Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)


        wifiP2pManager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        wifiP2pChannel = wifiP2pManager.initialize(this, mainLooper, null)
    }

    override fun onResume() {
        super.onResume()

        wifiP2pReceiver = WiFiDirectBroadcastReceiver(wifiP2pManager, wifiP2pChannel, this)
        registerReceiver(wifiP2pReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()

        unregisterReceiver(wifiP2pReceiver)

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

    override fun onPeersAvailable(peerList: WifiP2pDeviceList?) {
        peerList?.deviceList?.let {
            it.forEach {
                Log.d(TAG, "device: $it")
            }
        }
    }

    companion object {
        const val TAG = "WifiDirectDemo"
    }
}
