package com.example.heroesandbandits.Utils

import com.example.heroesandbandits.Models.CharacterDataWrapper
import com.example.heroesandbandits.Models.SeriesDataWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {

    @GET("characters?limit=20")
    fun getAllCharacters(
        @Query("nameStartsWith") nameStartsWith: String? = null,
        @Query("name") byExactName: String? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Single<CharacterDataWrapper>

    @GET("characters?")
    fun searchForCharacter(
        @Query("nameStartsWith") nameStartsWith: String?
    ) : Single<CharacterDataWrapper>

    @GET("series?")
    fun searchForSeries(
        @Query("titleStartsWith") titleStartsWith: String?
    ) : Single<SeriesDataWrapper>

    @GET("characters?")
    fun getOneCharacter(
        @Query("id") id: Int
    )   : Single<CharacterDataWrapper>
}
