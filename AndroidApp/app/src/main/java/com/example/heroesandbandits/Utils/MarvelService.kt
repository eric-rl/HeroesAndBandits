package com.example.heroesandbandits.Utils

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {

    @GET("characters?limit=1")

    fun getAllCharacters(

        @Query("nameStartsWith") nameStartsWith: String? = null,

        @Query("name") byExactName: String? = null,

        @Query("orderBy") orderBy: String? = null,

        @Query("limit") limit: Int? = null,

        @Query("offset") offset: Int? = null

    ): Single<CharacterDataWrapper>

}