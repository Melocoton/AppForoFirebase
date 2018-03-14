package com.example.oscar.appforofirebase.Model

/**
 * Created by Oscar on 13/03/2018.
 */
data class Usuario(var uid: String, var nombre: String, var email: String)
data class Post(var titulo: String, var autor: String)
data class Comentario(var autor: String, var comentario: String)