package ru.ipimenov.informationboard.utils

import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix

object ImagePicker {

    const val REQUEST_CODE_GET_IMAGES = 999

    fun getImages(context: AppCompatActivity) {
        val options = Options.init()
            .setRequestCode(REQUEST_CODE_GET_IMAGES)                       //Request code for activity results
            .setCount(3)                                                   //Number of images to restrict selection count
            .setFrontfacing(false)                                         //Front Facing camera on start
            .setMode(Options.Mode.Picture)                                 //Option to select only pictures or videos or both
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientation
            .setPath("/pix/images")                                        //Custom Path For media Storage

        Pix.start(context, options)
    }
}