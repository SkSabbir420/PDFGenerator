package com.covid19.pdfgenerator

import com.covid19.pdfgenerator.databinding.ActivityMainBinding
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.itextpdf.text.Document
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val STORAGE_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_qrcode_reader)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.btnGeneratePdf.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                        PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission,STORAGE_CODE)
                }else{
                    savePDF()
                }
            }else{
                savePDF()
            }
        }

    }

    private fun savePDF() {
        val mDoc = Document()
        val mFileName = SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault())
            .format(System.currentTimeMillis())

        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/"  + mFileName + ".pdf"

        try {
            PdfWriter.getInstance(mDoc,FileOutputStream(mFilePath))
            mDoc.open()
            val data = activityMainBinding.etPdfData.text.toString().trim()
            mDoc.addAuthor("Sabbir")
            mDoc.add(Paragraph(data))
            mDoc.close()
            Toast.makeText(this,"$mFilePath.pdf\n is create to \n$mFilePath",Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            Toast.makeText(this,"" + e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.size >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    savePDF()
                }else{
                    Toast.makeText(this,"Permision denite",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}