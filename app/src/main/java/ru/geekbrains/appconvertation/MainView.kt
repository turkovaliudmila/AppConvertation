package ru.geekbrains.appconvertation

import android.graphics.Bitmap
import android.net.Uri

interface MainView {
    fun setSelectedImage(uri: Uri)
    fun setConvertedImage(path: String, image: Bitmap)
    fun getCurrentBitmap() : Bitmap
    fun showMessage(error: Throwable)
    fun setVisibleElements(isConverting: Boolean)
}