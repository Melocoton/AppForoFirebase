package com.example.oscar.appforofirebase.Model

/**
 * Created by Oscar on 13/03/2018.
 */
data class Usuario(var id: Int, var nombre: String, var email: String)
data class Post(var id: Int, var titulo: String, var autor: String)
data class Comentario(var id: Int, var autor: String, var comentario: String)