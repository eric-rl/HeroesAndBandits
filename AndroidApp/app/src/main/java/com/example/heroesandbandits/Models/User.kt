package com.example.heroesandbandits.Models

data class User (val email: String = "test@gmail.com",
                 val password: String,
                 val favorites: ArrayList<Hero>){

}