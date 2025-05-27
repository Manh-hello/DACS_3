package com.example.project.Repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.project.Model.Entity.Customer
import com.google.android.play.integrity.internal.b
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Customers {
    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("Customers")

    suspend fun checkEmail(email: String): Boolean{
        val snapshot = db.orderByChild("email").equalTo(email).get().await()
        return snapshot.exists()
    }

    suspend fun save(
        srcimg: String,
        name: String,
        email: String,
        sdt: String,
        address: String,
        password: String,
        birth: String
    ): Boolean {
        val id = db.push().key ?: return false
        val customer = Customer(id, srcimg, name, email, sdt, address, password, birth)
        db.child(id).setValue(customer).await()
        return true
    }

    suspend fun select(id:String): Customer?{
        val snapshot = db.child(id).get().await()
        return  snapshot.getValue(Customer::class.java)
    }

    suspend fun login(email: String): Customer?{
        val snapshot = db.orderByChild("email").equalTo(email).get().await()
        val firstSnap = snapshot.children.firstOrNull()
        return firstSnap?.getValue(Customer::class.java)
    }

    suspend fun update(id:String, name: String, sdt: String, address: String, pass:String, birth: String) {
        val updates = mapOf(
            "password" to pass,
            "address" to address,
            "sdt" to sdt,
            "name" to name,
            "birth" to birth
        )
        db.child(id).updateChildren(updates).await()
    }

    suspend fun update(id:String, src: Uri, name: String, sdt: String, address: String, pass:String, birth: String) {
        var srcimg = uploadImageToCloudinary(src)
        val updates = mapOf(
            "password" to pass,
            "address" to address,
            "sdt" to sdt,
            "name" to name,
            "birth" to birth,
            "srcimg" to srcimg
        )
        db.child(id).updateChildren(updates).await()
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