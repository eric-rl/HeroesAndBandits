package com.example.heroesandbandits.Utils

import android.util.Log
import android.util.Log.d
import com.example.heroesandbandits.Models.StitchBson
import com.google.android.gms.tasks.Task
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
import org.bson.Document


object StitchCon {
    class UserData(data: Document){
        val _id:String
        val id:String
        var favourites:Document
        var characters:MutableList<Any>
        init {
            _id = data["_id"].toString()
            id = data["id"].toString()
            favourites =  data["favourites"] as Document
            characters = favourites["characters"] as MutableList<Any>
        }
    }


    private var client: StitchAppClient? = null
    private var emailPassClient: UserPasswordAuthProviderClient? = null
    private var db: RemoteMongoDatabase? = null
    private var userDataCollection: RemoteMongoCollection<Document>? = null
    private var userFilter: Document? = null

//    var user: User? = null
    var userData:UserData? = null


    init {
        d("___", "STITCH INIT")
        if (client == null) {
            client = Stitch.getDefaultAppClient()
            emailPassClient =
                client!!.auth.getProviderClient(UserPasswordAuthProviderClient.factory)
            db = client!!
                .getServiceClient(RemoteMongoClient.factory, "mongodb-atlas")
                .getDatabase("db")
            userDataCollection = db!!.getCollection("user_data")
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
            Document().append("favourites.characters", item.id)
        )
        userData?.characters?.add(item.id)
//        val optionsDoc = RemoteUpdateOptions().upsert(true)
        return userDataCollection?.updateOne(userFilter, updateDoc)
    }

    fun removeFromFavourites(item: StitchBson): Task<RemoteUpdateResult>? {
        val updateDoc = Document().append(
            "\$pull",
            Document().append("favourites.characters", item.id)
        )
        return userDataCollection?.updateOne(userFilter, updateDoc)
    }

//    fun getFavourites(): Task<Document>? {
//        val proj = Document().append("characters", 1)
//        return userDataCollection?.findOne(userFilter, RemoteFindOptions().projection(proj))
//    }

    fun login(email: String, password: String): Task<StitchUser>? {
        val credential = UserPasswordCredential(email, password)
        return client?.auth?.loginWithCredential(credential)
    }

    fun initUser() {
        userFilter =  Document().append("id", client?.auth?.user?.id)
        db!!.getCollection("user_data").findOne(userFilter).addOnCompleteListener {
            if (it.isSuccessful) {
                userData = UserData(it.result)
                d("___", "id: ${userData?.id}  -  size: ${userData?.characters?.size}")

            } else {
                Log.e("___USER", "Error getting user_data", it.exception)
            }
        }
    }
}