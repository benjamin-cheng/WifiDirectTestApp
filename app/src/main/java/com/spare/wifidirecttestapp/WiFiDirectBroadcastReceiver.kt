package com.spare.wifidirecttestapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pManager.PeerListListener
import android.util.Log


/**
 * A BroadcastReceiver that notifies of important wifi p2p events.
 */
class WiFiDirectBroadcastReceiver
/**
 * @param manager WifiP2pManager system service
 * @param channel Wifi p2p channel
 * @param activity activity associated with the receiver
 */
(private val manager: WifiP2pManager?, private val channel: WifiP2pManager.Channel,
 private val activity: MainActivity) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION == action) {
            // UI update to indicate wifi p2p status.
            val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
            activity.isWifiP2pEnabled = (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)

            Log.d(MainActivity.TAG, "P2P state changed - " + state)
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION == action) {
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            manager?.requestPeers(channel, activity as PeerListListener)
            Log.d(MainActivity.TAG, "P2P peers changed")
        } /*else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION == action) {
            if (manager == null) {
                return
            }
            val networkInfo = intent
                    .getParcelableExtra<Parcelable>(WifiP2pManager.EXTRA_NETWORK_INFO) as NetworkInfo
            if (networkInfo.isConnected) {
                // we are connected with the other device, request connection
                // info to find group owner IP
                val fragment = activity.fragmentManager.findFragmentById(R.id.frag_detail) as DeviceDetailFragment
                manager.requestConnectionInfo(channel, fragment)
            } else {
                // It's a disconnect
                activity.resetData()
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION == action) {
            val fragment = activity.fragmentManager.findFragmentById(R.id.frag_list) as DeviceListFragment
            fragment.updateThisDevice(intent.getParcelableExtra<Parcelable>(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice)
        }*/
    }
}