package ru.geekbrains.appconvertation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import ru.geekbrains.appconvertation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainView {

    private var vb: ActivityMainBinding? = null
    private val presenter by lazy {
        MainPresenter(this, this)
    }

    companion object {
        const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 124
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb?.root)

        requestPermissionWrite()

        vb?.image?.setOnClickListener { selectImage() }
        vb?.btnConvert?.setOnClickListener {
            presenter.onClickBtnConvert()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpg"))
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.data?.let { uri ->
            presenter.OnSelectImage(uri)
        }
    }

    override fun setSelectedImage(uri: Uri) {
        vb?.image.apply {
            this?.background = null
            this?.setImageURI(uri)
        }
        vb?.path?.text = uri.toString()
    }

    override fun setConvertedImage(path: String, image: Bitmap) {
        vb?.image.apply {
            this?.background = null
            this?.setImageBitmap(image)
        }
        vb?.path2?.text = path
    }

    override fun getCurrentBitmap(): Bitmap {
        return (vb?.image?.drawable as BitmapDrawable).bitmap
    }

    override fun showMessage(error: Throwable) {
        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
    }

    override fun setVisibleElements(isConverting: Boolean) {
        if (isConverting) {
            vb?.loadingBar?.visibility = View.VISIBLE
            vb?.btnConvert?.text = "Cancel"
        } else {
            vb?.loadingBar?.visibility = View.GONE
            vb?.btnConvert?.text = "Convert"
        }
    }

    private fun requestPermissionWrite() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        vb?.btnConvert?.isEnabled =
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

}