package com.example.projectmanage.Repository

import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.projectmanage.Model.Entity.ImgProduct
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Imgs {
    private val db = FirebaseDatabase.getInstance().getReference("Imgs")

    suspend fun uploadImages(productId: String, imageUris: List<Uri>) {
        for (uri in imageUris) {
            val imageUrl = uploadImageToCloudinary(uri)
            val imgId = db.push().key ?: ""
            val img = ImgProduct(productId, imageUrl)
            db.child(imgId).setValue(img).await()
        }
    }

    private suspend fun uploadImageToCloudinary(uri: Uri): String =
        suspendCancellableCoroutine { cont ->
            MediaManager.get().upload(uri)
                .option("resource_type", "image")
                .unsigned("oyba1hle")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {}
                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                    override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                        val imageUrl = resultData?.get("secure_url").toString()
                        cont.resume(imageUrl)
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        cont.resumeWithException(Exception(error?.description ?: "Upload thất bại"))
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
                })
                .dispatch()
        }
    suspend fun select(id: String): List<String>{
        val snap = db.orderByChild("productId").equalTo(id).get().await()
        return snap.children.mapNotNull { it.getValue(ImgProduct::class.java)?.img }
    }

    suspend fun delete(proId: String) {
        val snap = db.orderByChild("productId").equalTo(proId).get().await()
        for (child in snap.children) {
            child.ref.removeValue().await()
        }
    }
}
