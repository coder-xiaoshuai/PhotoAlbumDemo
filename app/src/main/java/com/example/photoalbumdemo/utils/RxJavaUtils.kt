package com.example.photoalbumdemo.utils

import android.annotation.SuppressLint
import android.util.Log
import com.example.photoalbumdemo.bean.MediaBucket
import com.example.photoalbumdemo.callback.CommonCallback
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.Callable

class RxJavaUtils {
    companion object {
        /**
         * 异步处理耗时任务
         */
        @SuppressLint("CheckResult")
        fun <T> asyncDo(task: Callable<T>, successCallback: CommonCallback<T>) {
            val observable = Observable.create(ObservableOnSubscribe<T> {
                try {
                    it.onNext(task.call())
                } catch (e: Exception) {
                    it.onError(e);
                }
                it.onComplete()
            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

            val observer = object : Observer<T> {
                override fun onNext(result: T) {
                    successCallback.call(result)
                    Log.i("zs", "onNext")
                }

                override fun onComplete() {
                    Log.i("zs", "onComplete")
                }

                override fun onSubscribe(p0: Disposable) {
                    Log.i("zs", "onSubscribe")
                }

                override fun onError(p0: Throwable) {
                    Log.i("zs", "onError")
                }

            }
            observable.subscribe(observer)
        }
    }
}