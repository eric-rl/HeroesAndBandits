package com.example.heroesandbandits.Models

import com.google.gson.JsonObject
import org.bson.BSONObject
import org.bson.BsonDocument
import org.bson.Document

data class CharacterDataWrapper(    // val copyright: String, // optional): The copyright notice for the returned result.,
    // val attributionText: String, // optional): The attribution notice for this result. Please display either this notice or the contents of the attributionHTML field on all screens which contain data from the Marvel Comics API.,
    // val attributionHTML: String, // optional): An HTML representation of the attribution notice for this result. Please display either this notice or the contents of the attributionText field on all screens which contain data from the Marvel Comics API.,
    // val etag: String // optional): A digest value of the content returned by the call.

    val code: Int, // optional): The HTTP status code of the returned result.,
    val status: String, // optional): A string description of the call status.,
    val data: CharacterDataContainer // optional): The results returned by the call.,
)

data class CharacterDataContainer(
    // val offset: Int, //, optional): The requested offset (number of skipped results) of the call.,
    // val limit: Int, //, optional): The requested result limit.,

    val total: Int, //, optional): The total number of resources available given the current filter set.,
    val count: Int, //, optional): The total number of results returned by this call.,
    val results: Array<Character> //, optional): The list of characters returned by the call.

)

data class Character(
    /*val modified: Date, //, optional): The date the resource was most recently modified.,
    val resourceURI: string, //, optional): The canonical URL identifier for this resource.,
    val urls: Array, //[Url], optional): A set of public web site URLs for the resource.,
    val comics: ComicList, //, optional): A resource list containing comics which feature this character.,
    val stories: StoryList, //, optional): A resource list of stories in which this character appears.,
    val events: EventList, //, optional): A resource list of events in which this character appears.,
    val series: SeriesList, //, optional): A resource list of series in which this character appears.*/

    var name: String, //, optional): The name of the character.
    val description: String, //, optional): A short bio or description of the character.
    override val id: Int, //, optional): The unique ID of the character resource.,
    val thumbnail: ImageModel, //, optional): The representative image for this character.,
    val urls: Array<JsonObject>?
):StitchBson, MarvelId {
    override fun doc(): Document? {
        return Document()
            .append("name", name)
            .append("description", description)
            .append("id", id)
            .append("thumbnail", thumbnail)
            .append("urls", urls)
    }
}


data class ImageModel(
    var path: String,
    var extension: String
)

data class SeriesDataWrapper(
    val code: Int,
    val data: SeriesDataContainer
)

data class SeriesDataContainer(
    val results: Array<Series>
)

data class Series(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: ImageModel,
    val urls: Array<JsonObject>
)

interface StitchBson {
    fun doc():Document?
}
interface MarvelId{
    val id: Int
}

data class Search(
    val query:String,
    val result: ArrayList<Document>
) : StitchBson {
    override fun doc(): Document? {
        return Document()
            .append("query", query)
            .append("result", result)
    }
}


