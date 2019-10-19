package com.clevmania.idcreator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itextpdf.text.pdf.PdfWriter
import android.graphics.Bitmap
import android.R.layout
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.view.View.MeasureSpec
import android.webkit.MimeTypeMap
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val tag = MainActivity::class.java.simpleName
    private val storageRequestCode: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_generate.setOnClickListener {
            checkStoragePermission(this)
        }
    }

    private fun createPDF(){
/*        val document = Document()

        try {
            PdfWriter.getInstance(
                document,
                FileOutputStream("Image.pdf")
            )
            document.open()

            val image1 = Image.getInstance("watermark.png")
            document.add(image1)


            val imageUrl = "http://jenkov.com/images/" + "20081123-20081123-3E1W7902-small-portrait.jpg"

            val image2 = Image.getInstance(URL(imageUrl))
            document.add(image2)

            document.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

    private fun generateID(){
        val b = Bitmap.createBitmap(rl_front_card.width,rl_front_card.height,Bitmap.Config.ARGB_8888)
        val can = Canvas(b)
        val bgDrawable = rl_front_card.background
        if (bgDrawable!=null)
            bgDrawable.draw(can)
        else
            can.drawColor(Color.WHITE)
        rl_front_card.draw(can)

        storeImage(b)

//        val b = rl_front_card.drawToBitmap(Bitmap.Config.ARGB_8888)
//        storeImage(b)

        // Creating back card
        val backImage = Bitmap.createBitmap(rl_back_card.layoutParams.width,rl_back_card.layoutParams.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(backImage)
        rl_back_card.layout(rl_back_card.left,rl_back_card.top,rl_back_card.right,rl_back_card.bottom)
        if(rl_back_card.background != null) rl_back_card.background.draw(canvas) else canvas.drawColor(Color.WHITE)
        rl_back_card.draw(canvas)

//        val backImage = v_id_back.drawToBitmap(Bitmap.Config.ARGB_8888)
        storeImage(backImage)

    }

    private fun getOutputMediaFile():File? {
        // Create a media file name
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmm").format(Date())
        val mediaFile:File
        mediaFile = File.createTempFile("ID_$timeStamp",".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        return mediaFile
    }

    private fun storeImage(image: Bitmap) {
        val pictureFile = getOutputMediaFile()
        if (pictureFile == null) {
            Log.d(tag, "Error creating media file, check storage permissions: ")
            return
        }
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d(tag, "File not found: " + e.message)
        } catch (e: IOException) {
            Log.d(tag, "Error accessing file: " + e.message)
        }
    }

    private fun checkStoragePermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), storageRequestCode)
            } else {
                // permission granted, do the download
                generateID()
            }
        } else {
            // runtime permission not needed, so perform download
            generateID()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            storageRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generateID()
                } else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

//    fun getBitmapFromView(view: View, activity: Activity, callback: (Bitmap) -> Unit) {
//        activity.window?.let { window ->
//            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//            val locationOfViewInWindow = IntArray(2)
//            view.getLocationInWindow(locationOfViewInWindow)
//            try {
//                PixelCopy.request(window, Rect(locationOfViewInWindow[0], locationOfViewInWindow[1],
//                    locationOfViewInWindow[0] + view.width,
//                    locationOfViewInWindow[1] + view.height), bitmap, { copyResult ->
//                    if (copyResult == PixelCopy.SUCCESS) {
//                        callback(bitmap)
//                    }
//                    // possible to handle other result codes ...
//                }, Handler())
//            } catch (e: IllegalArgumentException) {
//                // PixelCopy may throw IllegalArgumentException, make sure to handle it
//                e.printStackTrace()
//            }
//        }
//    }

}
