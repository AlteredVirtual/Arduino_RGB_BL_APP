
package com.umut.rgb_led

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main. activity_main.*


import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import android.view.Gravity





class MainActivity:AppCompatActivity() {

    private var mColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        fab.setOnClickListener()
        {
            showColorPickerDialog()

        }

        off.setOnClickListener()
        {

            val toast = Toast.makeText(this@MainActivity, "RGB led kapatildi!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 50)
            toast.show()

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

                    val send_command = "<@$r,$g,$b>"

                    Log.d("VERIABLE",send_command)

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
    /*
    private fun updateToolbar(oldColor:Int, newColor:Int) {
        val transition = TransitionDrawable(arrayOf<ColorDrawable>(ColorDrawable(oldColor), ColorDrawable(newColor)))
        mToolbar.setBackground(transition)
        transition.startTransition(300)
    }
    private fun statusBarHeight():Int {
        val resId = getResources().getIdentifier("status_bar_height", "dimen", "android")
        return if (resId != 0) getResources().getDimensionPixelSize(resId) else 0
    }
    companion object {
        private val KEY_COLOR = "extra_color"
        private val KEY_COLOR_MODE = "extra_color_mode"
    }*/
}