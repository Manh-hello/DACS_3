package com.example.projectmanage.Repository

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.projectmanage.Model.Entity.Admin
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Stores {
    private val db = FirebaseDatabase.getInstance().getReference("Stores")

    suspend fun checkEmail(email: String): Boolean{
        val snapshot = db.orderByChild("email").equalTo(email).get().await()
        return snapshot.exists()
    }

    suspend fun save(name:String,
                     email:String,
                     pass:String,
                     address:String,
                     sdt:String,
                     img:String,
                     created:String): Boolean {
        val id = db.push().key?:return false
        val admin = Admin(id,name,email,pass,address,sdt,img,created)
        db.child(admin.id).setValue(admin).await()
        return true
    }

    suspend fun select(id: String): Admin? {
        val snapshot = db.child(id).get().await()
        return snapshot.getValue(Admin::class.java)
    }

    suspend fun login(email: String): Admin? {
        val snapshot = db.orderByChild("email").equalTo(email).get().await()
        val firstSnap = snapshot.children.firstOrNull()
        return firstSnap?.getValue(Admin::class.java)
    }

    suspend fun update(storeId: String, pass: String, address: String, phone: String, name: String) {
        val updates = mapOf(
            "pass" to pass,
            "address" to address,
            "sdt" to phone,
            "name" to name
        )
        db.child(storeId).updateChildren(updates).await()
    }

    suspend fun update(storeId: String, pass: String, address: String, phone: String, name: String, imageUrl: Uri) {
        val url = uploadImageToCloudinary(imageUrl)
        val updates = mapOf(
            "pass" to pass,
            "address" to address,
            "sdt" to phone,
            "name" to name,
            "img" to url
        )
        db.child(storeId).updateChildren(updates).await()
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
