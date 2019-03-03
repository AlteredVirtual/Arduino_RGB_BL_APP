package com.sharpangel.rgb_led

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import kotlinx.android.synthetic.main.content_bluetooth_connect.*
import org.jetbrains.anko.toast

//import kotlinx.android.synthetic.main.activity_bluetooth_connect.*

class BluetoothConnect : AppCompatActivity() {

    var m_bluetoothAdapter: BluetoothAdapter? = null
    lateinit var m_pairedDevices: Set<BluetoothDevice>
    val  REQUEST_ENABLE_BLUETOOTH=1
    companion object {
        val EXTRA_ADDRESS: String="Device_address"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_connect)

        m_bluetoothAdapter= BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter==null){
            toast("this device doesn't support bluethoot")
            return
        }
        if (!m_bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        bluetooth_refresh.setOnClickListener{ pairedDeviceList() }
    }
    //Show bluetooth devices list
    private fun pairedDeviceList(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
