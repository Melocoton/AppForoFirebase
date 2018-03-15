package com.example.oscar.appforofirebase.Model

/**
 * Created by Oscar on 13/03/2018.
 */
data class Usuario(var uid: String, var nombre: String, var email: String, var img: String)
data class Post(var autor: String, var titulo: String)
data class PostResult(var id: String, var autor: String, var titulo: String)
data class Comentario(var autor: String, var comentario: String)