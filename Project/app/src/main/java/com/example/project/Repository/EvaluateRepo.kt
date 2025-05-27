package com.example.project.Repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.projectmanage.Model.Entity.Evaluate
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class EvaluateRepo {
    private val db = FirebaseDatabase.getInstance().getReference("Evaluates")

    suspend fun save(proId: String, cusId: String,message: String,rating:Int,img: Uri){
        val id = db.child(proId).push().key?:""
        val url = uploadImageToCloudinary(img)
        val evaluate = Evaluate(id,cusId,rating,message,url)
        db.child(proId).child(id).setValue(evaluate).await()
    }
    suspend fun save(proId: String, cusId: String,message: String,rating:Int){
        val id = db.child(proId).push().key?:""
        val evaluate = Evaluate(id,cusId,rating,message,"")
        db.child(proId).child(id).setValue(evaluate).await()
    }

    suspend fun selectOnePro(proId: String): List<Evaluate>{
        val list = db.child(proId).get().await()
        return list.children.mapNotNull { it.getValue(Evaluate::class.java) }
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
}