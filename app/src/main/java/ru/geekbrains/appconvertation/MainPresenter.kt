package ru.geekbrains.appconvertation

import android.content.Context
import android.net.Uri
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainPresenter(private val view: MainView, private val context: Context) {
    var selectedImage: SelectedImage? = null
    private val disposable = CompositeDisposable()
    private var isConverting = false

    fun OnSelectImage(uri: Uri) {
        view.setSelectedImage(uri)
        val bitmap = view.getCurrentBitmap()
        selectedImage = SelectedImage(uri, bitmap)
    }

    fun onClickBtnConvert() {

        val curDir = context.getDir("Images", Context.MODE_PRIVATE)

        if (isConverting) {
            isConverting = false
            disposable.clear()
            view.setVisibleElements(isConverting)
        } else {
            isConverting = true
            view.setVisibleElements(isConverting)
            selectedImage?.let {
                disposable += ImageConverter.convertJpgToPng(it.bitmap, curDir)
                        .delay(10, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            view.setConvertedImage(it.first, it.second)
                            view.setVisibleElements(false)
                        }, view::showMessage)
            }
        }

    }


}