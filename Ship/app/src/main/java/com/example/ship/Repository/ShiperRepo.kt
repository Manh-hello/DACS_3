package com.example.ship.Repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.ship.Model.Shiper
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ShiperRepo {
    private val db = FirebaseDatabase.getInstance().getReference("Shipers")

    suspend fun save(name:String, email: String, sdt: String, pass : String, address: String): Boolean{
        val id = db.push().key?:return false
        val sp = Shiper(id,"",name,email,pass,sdt,address,"")
        db.child(id).setValue(sp).await()
        return true
    }

    suspend fun checkEmail(email: String): Boolean{
        val snapshot = db.orderByChild("email").equalTo(email).get().await()
        return snapshot.exists()
    }

    suspend fun login(email: String): Shiper? {
        val sps = db.orderByChild("email").equalTo(email).get().await()
        val sp = sps.children.firstOrNull()
        return sp?.getValue(Shiper::class.java)
    }

    suspend fun update(id: String, name: String, pass: String, sdt: String, address: String, date: String) {
        val updates = mapOf<String, Any>(
            "name" to name,
            "pass" to pass,
            "sdt" to sdt,
            "address" to address,
            "date" to date
        )
        db.child(id).updateChildren(updates).await()
    }

    suspend fun load(id: String) : Shiper? {
        val sp = db.child(id).get().await()
        return sp.getValue(Shiper::class.java)
    }
    suspend fun update(shiperId: String, src: Uri, name: String, sdt: String, address: String, password: String, birth: String){
        var srcimg = uploadImageToCloudinary(src)
        val updates = mapOf<String, Any>(
            "name" to name,
            "pass" to password,
            "sdt" to sdt,
            "address" to address,
            "date" to birth,
            "src" to srcimg
        )
        db.child(shiperId).updateChildren(updates).await()
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