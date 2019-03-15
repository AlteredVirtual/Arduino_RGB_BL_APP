package com.sharpangel.rgb_led

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_bluetooth_connect.*

import android.widget.Toast

import java.util.*



class BluetoothConnect : AppCompatActivity() {

    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val  REQUEST_ENABLE_BLUETOOTH=1


    companion object {
        const val EXTRA_ADDRESS: String="Device_address"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_connect)
        this.setTitle(R.string.title_activity_bluetooth_connect)
        m_bluetoothAdapter= BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter==null){

            Toast.makeText(this@BluetoothConnect, "Bu cihaz bluetooth desteklemiyor !", Toast.LENGTH_SHORT).show()

            
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

        m_pairedDevices= m_bluetoothAdapter!!.bondedDevices

        val list: ArrayList<BluetoothDevice> = ArrayList()

        if(!m_pairedDevices.isEmpty()){
            for(device: BluetoothDevice in m_pairedDevices){
                list.add(device)
                Log.i("device",""+device)
            }
        } else{
            Toast.makeText(this@BluetoothConnect, "Bluetooth aygıt bulunamadı !", Toast.LENGTH_SHORT).show()

        }


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)


        bluetooth_devices.adapter = adapter
        bluetooth_devices.onItemClickListener = AdapterView.OnItemClickListener {_, _, position, _ ->
            val device: BluetoothDevice= list[position]
            val address: String = device.address

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode==Activity.RESULT_OK)

                if(m_bluetoothAdapter!!.isEnabled) {
                   Toast.makeText(this@BluetoothConnect, "Bluetooth aktifleştirildi!", Toast.LENGTH_SHORT).show()
                }else{
                   Toast.makeText(this@BluetoothConnect, "Bluetooth devre dışı bırakıldı!", Toast.LENGTH_SHORT).show()
                }

        } else if(resultCode == Activity.RESULT_CANCELED){
           Toast.makeText(this@BluetoothConnect, "Bluetooth bağlanamadı!", Toast.LENGTH_SHORT).show()
        }
    }
}
