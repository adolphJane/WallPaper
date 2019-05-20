package com.magicalrice.adolph.wallpaper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import java.io.*

object BitmapUtils {
    fun imgToBase64(bitmap: Bitmap): String? {
        var out: ByteArrayOutputStream? = null
        try {
            out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()

            val imgBytes = out.toByteArray()
            return Base64.encodeToString(imgBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            return null
        } finally {
            try {
                out!!.flush()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    @Throws(FileNotFoundException::class, IOException::class)
    fun getBitmapFormUri(ac: FragmentActivity, uri: Uri?): Bitmap? {
        var input = ac.contentResolver.openInputStream(uri)
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()
        val originalWidth = onlyBoundsOptions.outWidth
        val originalHeight = onlyBoundsOptions.outHeight
        if (originalWidth == -1 || originalHeight == -1)
            return null
        //图片分辨率以480x800为标准
        val hh = 800f//这里设置高度为800f
        val ww = 480f//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var be = 1//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (originalWidth / ww).toInt()
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (originalHeight / hh).toInt()
        }
        if (be <= 0)
            be = 1
        //比例压缩
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = be//设置缩放比例
        bitmapOptions.inDither = true//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//optional
        input = ac.contentResolver.openInputStream(uri!!)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input?.close()

        return compressImage(bitmap)//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    fun compressImage(image: Bitmap?): Bitmap? {
        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image?.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10//每次都减少10
        }
        val isBm = ByteArrayInputStream(baos.toByteArray())//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null)
    }

//    fun getBitmapBase64(
//        activity: FragmentActivity,
//        uri: Uri?
//    ): Observable<BaseObjectResponse<String>>? {
//        val bitmap = BitmapUtils.getBitmapFormUri(activity, uri)
//        bitmap?.let {
//            val params = HashMap<String, String>()
//            params["fileData"] = BitmapUtils.imgToBase64(it) ?: ""
//            params["fileName"] = System.currentTimeMillis().toString() + ".png"
//            val bodyBuilder = FormBody.Builder()
//            val it = params.entries.iterator()
//            while (it.hasNext()) {
//                val pair = it.next() as Map.Entry<*, *>
//                bodyBuilder.add(pair.key as String, pair.value as String)
//                it.remove() // avoids a ConcurrentModificationException
//            }
//            val requestBody = bodyBuilder.build()
//            return PaydayLoanApi.getInstance().commonApi().uploadImage(requestBody)
//        }
//        return null
//    }
//
//    fun uploadMultiImages(
//        activity: FragmentActivity,
//        uriList: ArrayList<String>
//    ): Flowable<BaseArrayResponse<String>> {
//        return Flowable.just(uriList)
//            .subscribeOn(Schedulers.io())
//            .map {
//                return@map Luban.with(activity).load(uriList).get()
//            }
//            .map {
//                val parts = arrayListOf<MultipartBody.Part>()
//                it.forEach {file ->
//                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//                    val part = MultipartBody.Part.createFormData(
//                        "images",
//                        System.currentTimeMillis().toString() + ".jpg",
//                        requestFile
//                    )
//                    parts.add(part)
//                }
//                return@map parts
//            }
//            .flatMap {
//                return@flatMap PaydayLoanApi.getInstance().commonApi().uploadImageMuli(it.toTypedArray())
//            }
//            .observeOn(AndroidSchedulers.mainThread())
//    }

    @SuppressLint("CheckResult")
    fun saveImageToGallery(context: Context,childDir: String, bmp: Bitmap, name: String, listener: ImageFinishListener) {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        Observable.create<String> {
            val appDir = File(context.getExternalFilesDir(null), childDir)
            if (!appDir.exists()) {
                appDir.mkdir()
            }
            val fileName = "$name.jpg"
            val file = File(appDir, fileName)
            try {
                val fos = FileOutputStream(file)
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    file.absolutePath,
                    fileName,
                    null
                )
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            it.onNext(file.path)
        }.compose(RxUtils.io_main())
            .subscribe({
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(it))))
                listener.imgFinish(it)
            }, {
                listener.imgFinish("")
            })

    }
}