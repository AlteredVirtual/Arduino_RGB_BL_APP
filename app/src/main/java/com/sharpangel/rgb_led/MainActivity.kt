
package com.sharpangel.rgb_led


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.*

import kotlinx.android.synthetic.main. activity_main.*


import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import java.io.IOException
import java.util.*


class MainActivity:AppCompatActivity() {


    companion object {
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
        var mColor: Int = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        m_address = intent.getStringExtra(BluetoothConnect.EXTRA_ADDRESS)

        ConnectToDevice(this).execute()

        bluetooth.setOnClickListener {
            disconnect()
        }


        fab.setOnClickListener()
        {
            showColorPickerDialog()

        }

        fade.setOnClickListener(){ // Make Fade mode

            val toast = Toast.makeText(this@MainActivity, "Fade Moda Geçildi!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 50)
            toast.show()
            sendCommand("<Q>");

        }
        flash.setOnClickListener(){ // Make Fade mode

            val toast = Toast.makeText(this@MainActivity, "Flash Moda Geçildi!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 50)
            toast.show()
            sendCommand("<F>");
        }

        off.setOnClickListener()
        {

            val toast = Toast.makeText(this@MainActivity, "RGB led kapatildi!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 50)
            toast.show()
            sendCommand("<K>");

        }


        bluetooth.setOnClickListener(){
            val intent = Intent(this, BluetoothConnect::class.java)
            startActivity(intent);
        }

    }
       // showColorPickerDialog()

    private fun showColorPickerDialog() {

        ChromaDialog.Builder()
            .initialColor(mColor)
            .colorMode(ColorMode.RGB)
            .onColorSelected(object: ColorSelectListener {
                override fun onColorSelected(color:Int) {
                    mColor = color

                    val hex = updateTextView(color)

                    val rgb = getRGB(hex)

                    val r = rgb[0].toString()
                    val g = rgb[1].toString()
                    val b = rgb[2].toString()

                    val set_rgb = "<@$r,$g,$b>"
                    bg.setBackgroundColor(color);
                    Log.d("VERIABLE",set_rgb)
                    sendCommand(set_rgb);
                    //convert to r g b and sende command  to bluetooth <@RED,GREEN,BLUE> example as ( <@255,0,0>  is show only red color )



                }
            })
            .create()
            .show(supportFragmentManager, "dialog")

    }


    private fun updateTextView(color:Int) : String {

        val hex = String.format("%06X", 0xFFFFFF and color)
        text_view.text = "#$hex"

        return hex
    }

    fun getRGB(rgb:String):IntArray {
        val ret = IntArray(3)
        for (i in 0..2)
        {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16)
        }
        return ret
    }


    private fun sendCommand(input: String) {
        if (MainActivity.m_bluetoothSocket != null) {
            try{
                MainActivity.m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun disconnect() {
        if (MainActivity.m_bluetoothSocket != null) {
            try {
                MainActivity.m_bluetoothSocket!!.close()
                MainActivity.m_bluetoothSocket = null
                MainActivity.m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (MainActivity.m_bluetoothSocket == null || !MainActivity.m_isConnected) {
                    MainActivity.m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = MainActivity.m_bluetoothAdapter.getRemoteDevice(MainActivity.m_address)
                    MainActivity.m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(MainActivity.m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    MainActivity.m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                MainActivity.m_isConnected = true
            }
        }
    }
}