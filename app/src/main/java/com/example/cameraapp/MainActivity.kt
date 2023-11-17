package com.example.cameraapp

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cameraapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private val imageLoadLauncher = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()) {
        uriList ->updateImages(uriList)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadImageButton.setOnClickListener {
            Toast.makeText(this, "확인", Toast.LENGTH_LONG).show()
            checkPermission()
        }


    }

    private fun checkPermission(){
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED ->
            {
                loadImage()
            }
            shouldShowRequestPermissionRationale(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestReadExternalStorage()
            }
        }


    }

    private fun showPermissionInfoDialog(){
        AlertDialog.Builder(this).apply{
            setMessage("이미지를 가져오기 위해서, 외부 저장소 읽기 권한이 필요합니다.")
            setNegativeButton("취소", null)
            setPositiveButton("동의"){_,_ ->
                requestReadExternalStorage()
            }
        }.show()
    }

    private fun requestReadExternalStorage(){
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_READ_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if(grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                }
            }
        }
    }

    private fun loadImage(){
        imageLoadLauncher.launch("image/*")
    }

    private fun updateImages(uriList: List<Uri>){
        Log.i("updateImages", "$uriList")
    }

    companion object {
        const val REQUEST_READ_EXTERNAL_STORAGE = 100
    }
}