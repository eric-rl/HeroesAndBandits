package com.example.heroesandbandits.Utils

import android.util.Log
import android.util.Log.d
import com.example.heroesandbandits.Models.StitchBson
import com.example.heroesandbandits.Models.User
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.core.StitchAppClient
import com.mongodb.stitch.android.core.auth.StitchUser
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential
import com.mongodb.stitch.core.services.mongodb.remote.RemoteFindOptions
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateOptions
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult
import org.bson.BSONObject
import org.bson.BsonDocument
import org.bson.Document


object StitchCon {
    private var client: StitchAppClient? = null
    private var emailPassClient: UserPasswordAuthProviderClient? = null
    private var db: RemoteMongoDatabase? = null
    private var favouritesCollection: RemoteMongoCollection<Document>? = null
    private var userFilter: Document? = null
    var user: User? = null


    init {
        d("___", "STITCH INIT")
        if (client == null) {
            client = Stitch.getDefaultAppClient()
            emailPassClient =
                client!!.auth.getProviderClient(UserPasswordAuthProviderClient.factory)
            db = client!!
                .getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
                .getDatabase("db")
            favouritesCollection = db!!.getCollection("favourites")

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

    fun addToFavourites(item: StitchBson): Task<RemoteUpdateResult>? {
        val updateDoc = Document().append(
            "\$addToSet",
            Document().append("characters", Document().append("char_id", item.id))
        )
        val optionsDoc = RemoteUpdateOptions().upsert(true)
        return favouritesCollection?.updateOne(userFilter, updateDoc, optionsDoc)
    }

    fun removeFromFavourites(item: StitchBson): Task<RemoteUpdateResult>? {
        val updateDoc = Document().append(
            "\$pull",
            Document().append("characters", Document().append("char_id", item.id))
        )
        return favouritesCollection?.updateOne(userFilter, updateDoc)
    }

    fun getFavourites(): Task<Document>? {
        val proj = Document().append("characters", 1)
        return favouritesCollection?.findOne(userFilter, RemoteFindOptions().projection(proj))
    }

    fun login(email: String, password: String): Task<StitchUser>? {
        val credential = UserPasswordCredential(email, password)
        return client?.auth?.loginWithCredential(credential)
    }

    fun initUser() {
        userFilter = Document().append("user_id", client?.auth?.user?.id)
        getFavourites()?.addOnCompleteListener {
            if (it.isSuccessful) {
                user =
                    User(client?.auth?.user?.id!!, it.result["characters"] as ArrayList<Document>)
                d("___", "size: ${user?.characters?.size}")
            } else {
                Log.e("___", "Error getting favourites", it.exception)
            }
        }
    }
}