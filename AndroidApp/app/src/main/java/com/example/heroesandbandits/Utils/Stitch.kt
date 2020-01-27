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
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult
import org.bson.Document
import java.util.*


private class UserConnection(val stitchAppClient: StitchAppClient) {

}

object StitchCon {
    var user: StitchUser? = null
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

    fun addToFavourites(id: Int): Task<RemoteInsertOneResult>? {

        val doc = Document()
        doc["user_id"] = client?.auth?.user?.id
        doc["character_id"] = id

        return db!!.getCollection("favourites").insertOne(doc)

//        val newItem: DocumentsContract.Document = DocumentsContract.Document.
//            .append("name", "legos")
//            .append("quantity", 10)
//            .append("category", "toys")
//            .append(
//                "reviews", Arrays.asList(
//                    DocumentsContract.Document()
//                        .append("username", "mongolover")
//                        .append("comment", "this is great")
//                )
//            )
    }

    fun login(email: String, password: String): Task<StitchUser>? {
        val credential = UserPasswordCredential(email, password)
        return client?.auth?.loginWithCredential(credential)
    }

//    fun anonymouslogin() {
//        client
//            .auth
//            .loginWithCredential(AnonymousCredential())
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Log.d("___", "logged in anonymously")
//                    user = it.result
//                    Log.d("___", "user: ${user?.id + " " + user?.isLoggedIn}")
//
//                } else {
//                    Log.e("___", "failed to log in anonymously", it.exception);
//                }
//            }
//    }

}