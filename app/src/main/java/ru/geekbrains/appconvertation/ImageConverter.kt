package ru.geekbrains.appconvertation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream

object ImageConverter {

    fun convertJpgToPng(bitmap: Bitmap, curDir: File): Single<Pair<String, Bitmap>> {
        return Single.fromCallable {
            val file = File(curDir, "img.png")
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            val path2 = curDir.path + "/img.png"
            val bitmap2 = BitmapFactory.decodeFile(path2)
            return@fromCallable (file.path.toString() to bitmap2)
        }
    }

}