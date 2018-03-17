package com.example.oscar.appforofirebase.Activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.oscar.appforofirebase.Adapter.CommentAdapter
import com.example.oscar.appforofirebase.Model.Comentario
import com.example.oscar.appforofirebase.Model.CommentResult
import com.example.oscar.appforofirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.content_comment.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.verticalLayout

class CommentActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private val TAG = "###"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()
        postId = intent.getSerializableExtra("postPulsado") as String

        getUser()
        getComment()

        fab.setOnClickListener { addComent() }
    }

    private fun getComment() {
        var listaComentario = mutableListOf<CommentResult>()
        db.collection("Post").document(postId).collection("Comments").addSnapshotListener({snapshot, firebaseFirestoreException ->
            listaComentario.clear()
            snapshot.forEach {
                Log.d("ResultFirestore", "ResultID -> ${it.id} : Data -> ${it.data}")
                listaComentario.add(CommentResult(it.id, it.data["autor"].toString(), it.data["comentario"].toString()))
            }
            pintarLayout(listaComentario)
        })
    }

    private fun pintarLayout(listaComentario: MutableList<CommentResult>) {
        var adapter = CommentAdapter(this, R.layout.row_comment, listaComentario)
        rvComment.layoutManager = LinearLayoutManager(this)
        rvComment.adapter = adapter
    }

    private fun addComent() {
        alert {
            customView {
                verticalLayout{
                    val etComment = editText{
                        hint = "Comentario"
                    }
                    positiveButton("Aceptar"){
                        val comment: Comentario = Comentario(user.email!!, etComment.text.toString())
                        guardarComent(comment)
                    }
                    negativeButton("Cancelar"){it.cancel()}
                }
            }
        }.show()
    }

    private fun guardarComent(comment: Comentario) {
        db.collection("Post").document(postId).collection("Comments").add(comment)
    }

    private fun getUser(){
        if (mAuth.currentUser != null) {
            user = mAuth.currentUser!!
        }else{
            //volver a la pantalla de login
        }
    }

}
