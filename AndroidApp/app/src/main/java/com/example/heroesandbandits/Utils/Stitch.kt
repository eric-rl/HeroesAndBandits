package com.example.heroesandbandits.Utils

import android.util.Log
import android.util.Log.d
import com.google.android.gms.tasks.Task
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.core.StitchAppClient
import com.mongodb.stitch.android.core.auth.StitchUser
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult
import org.bson.Document


object StitchCon {
    private var client: StitchAppClient? = null
    private var emailPassClient: UserPasswordAuthProviderClient? = null
    private var db: RemoteMongoDatabase? = null



    init {
        d("___", "STITCH INIT")
        if(client == null) {
                client = Stitch.getDefaultAppClient()
                emailPassClient = client!!.auth.getProviderClient(UserPasswordAuthProviderClient.factory)
                db = client!!
                    .getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
                    .getDatabase("db")
        }
    }

    fun registerUser(user: String, pass: String): Task<Void>? {
        return emailPassClient?.registerWithEmail(user, pass)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    Log.d("___", "Successfully sent account confirmation email")
                } else {
                    Log.e(
                        "___",
                        "Error registering new user:",
                        task.exception
                    )
                }
            }
    }

    fun addToFavourites(id: Int): Task<RemoteUpdateResult>? {
        val filterDoc = Document().append("user_id",client?.auth?.user?.id)
        val updateDoc = Document().append("\$addToSet", Document().append("characters", Document().append("char_id",id)))
        val optionsDoc = RemoteUpdateOptions().upsert(true)
        return db!!.getCollection("favourites").updateOne(filterDoc,updateDoc, optionsDoc)
    }

    fun removeFromFavourites(id: Int): Task<RemoteUpdateResult>? {
        val filterDoc = Document().append("user_id",client?.auth?.user?.id)
        val updateDoc = Document().append("\$pull", Document().append("characters", Document().append("char_id",id)))
        return db!!.getCollection("favourites").updateOne(filterDoc,updateDoc)
    }

    fun getFavourites(){
    }

    fun login(email: String, password: String): Task<StitchUser>? {
        val credential = UserPasswordCredential(email, password)
        return client?.auth?.loginWithCredential(credential)
    }

}