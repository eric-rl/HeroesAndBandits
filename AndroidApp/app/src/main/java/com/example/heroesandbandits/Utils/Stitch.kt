package com.example.heroesandbandits.Utils

import android.util.Log
import android.util.Log.d
import com.example.heroesandbandits.Models.*
import com.google.android.gms.tasks.Task
import com.mongodb.stitch.android.core.Stitch
import com.mongodb.stitch.android.core.StitchAppClient
import com.mongodb.stitch.android.core.auth.StitchUser
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoDatabase
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential
import com.mongodb.stitch.core.services.mongodb.remote.ChangeEvent
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult
import org.bson.Document
import org.bson.BsonValue


object StitchCon {
    class UserData(data: Document) {
        val _id: String = data["_id"].toString()
        val id: String = data["id"].toString()
        var favourites: Document = data["favourites"] as Document
        var characters: MutableList<Any>
        var series: MutableList<Any>

        init {
            characters = favourites["characters"] as ArrayList<Any>
            series = favourites["series"] as ArrayList<Any>
        }
    }


    private var client: StitchAppClient? = null
    private var emailPassClient: UserPasswordAuthProviderClient? = null
    private var db: RemoteMongoDatabase? = null
    private var userDataCollection: RemoteMongoCollection<Document>? = null
    private var userFilter: Document? = null
    var userData: UserData? = null


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

        userDataCollection?.watch()?.addOnCompleteListener { task ->
            val changeStream = task.result
            changeStream.addChangeEventListener { documentId: BsonValue, event: ChangeEvent<Document> ->
                d("watcher", "${task.result}")
            }
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

    fun imageConverter(path: String, extention: String): String {
        val path = path.substring(0, 4) + "s" + path.substring(4, path.length)
        return "$path/standard_medium.$extention"
    }

    private fun convertItemToDocument(item: Any): Document{
        val obj = Document()
        if (item is Character) {
            val imageUrl = imageConverter(item.thumbnail.path, item.thumbnail.extension)
            obj["thumbnail"] = imageUrl
            obj["name"] = item.name
            obj["id"] = item.id
        } else if (item is FavoriteCharacter) {
            obj["thumbnail"] = item.thumbnail
            obj["name"] = item.name
            obj["id"] = item.id
        } else if (item is Series) {
            val imageUrl = imageConverter(item.thumbnail.path, item.thumbnail.extension)
            obj["thumbnail"] = imageUrl
            obj["title"] = item.title
            obj["id"] = item.id
        } else if (item is FavoriteSeries) {
            obj["thumbnail"] = item.thumbnail
            obj["title"] = item.title
            obj["id"] = item.id
        }
        return obj
    }

    fun addCharacterToFavourites(item: Any): Task<RemoteUpdateResult>? {
        val updateDoc: Document
        val obj = convertItemToDocument(item)   
        updateDoc = Document().append(
            "\$addToSet",
            Document().append("favourites.characters", obj)
        )
        return userDataCollection?.updateOne(userFilter, updateDoc)
    }

    fun addSeriesToFavourites(item: Any): Task<RemoteUpdateResult>? {
        val updateDoc: Document
        val obj = convertItemToDocument(item)
        updateDoc = Document().append(
            "\$addToSet",
            Document().append("favourites.series", obj)
        )
        return userDataCollection?.updateOne(userFilter, updateDoc)
    }

    fun removeCharacterFromFavourites(item: Any): Task<RemoteUpdateResult>? {
        val updateDoc: Document
        val obj = convertItemToDocument(item)
        updateDoc = Document().append(
            "\$pull",
            Document().append("favourites.characters", obj)
        )
        return userDataCollection?.updateOne(userFilter, updateDoc)
    }

    fun removeSeriesFromFavourite(item: Any): Task<RemoteUpdateResult>? {
        val updateDoc: Document
        val obj = convertItemToDocument(item)
        updateDoc = Document().append(
            "\$pull",
            Document().append("favourites.series", obj)
        )
        return userDataCollection?.updateOne(userFilter, updateDoc)

    }

    fun login(email: String, password: String): Task<StitchUser>? {
        val credential = UserPasswordCredential(email, password)
        return client?.auth?.loginWithCredential(credential)
    }

    fun firstTimeLogin() {
        userFilter = Document().append("id", client?.auth?.user?.id)
        db!!.getCollection("user_data").findOne(userFilter).addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                userData = UserData(it.result)
                d("___", "id: ${userData?.id}  -  size: ${userData?.characters?.size}")

            }
        }
    }

    fun initUser() {
        userFilter = Document().append("id", client?.auth?.user?.id)
        db!!.getCollection("user_data").findOne(userFilter).addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                userData = UserData(it.result)
                d("___", "LOGGED IN - id: ${userData?.id}")
            } else {
                userDataCollection?.insertOne(
                    Document()
                        .append("id", client?.auth?.user?.id)
                        .append(
                            "favourites", Document()
                                .append("characters", arrayListOf<Any>())
                                .append("series", arrayListOf<Any>())
                        )
                )
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            firstTimeLogin()
                        } else {

                        }
                    }
                Log.e("___USER", "Error getting user_data", it.exception)
            }
        }
    }
}