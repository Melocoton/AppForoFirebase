package com.example.oscar.appforofirebase.Activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.oscar.appforofirebase.Adapter.PostAdapter
import com.example.oscar.appforofirebase.Model.Post
import com.example.oscar.appforofirebase.Model.PostResult
import com.example.oscar.appforofirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.app_bar_post.*
import kotlinx.android.synthetic.main.content_post.*
import org.jetbrains.anko.*

class PostActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private val TAG = "###"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()

        getUser()
        getPost()

        fab.setOnClickListener { addPost() }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun getPost() {
        var listaPost = mutableListOf<PostResult>()
        //traer coleccion 1 vez
//        db.collection("Post")
//                .get()
//                .addOnCompleteListener({
//                    if (it.isSuccessful){
//                        for (result in it.result){
//                            Log.d("ResultFirestore", "ResultID -> ${result.id} : Data -> ${result.data}")
//                            Log.d("ResultFiresotre", "TituloPost: ${result.data["titulo"]}")
//                            val post = PostResult(result.id, result.data["autor"].toString(), result.data["titulo"].toString())
//                            listaPost.add(post)
//                        }
//                        pintarLayout(listaPost)
//                    }else{
//                        Log.d("ResultFirestore", "Error: ${it.exception}")
//                    }
//                })

        //poner listener y traer coleccion cada vez que cambie
        db.collection("Post").addSnapshotListener({ snapshot, _ ->
            listaPost.clear()
            snapshot.forEach {
                Log.d("ResultFirestore", "ResultID -> ${it.id} : Data -> ${it.data}")
                listaPost.add(PostResult(it.id, it.data["autor"].toString(), it.data["titulo"].toString()))
            }
            pintarLayout(listaPost)
        })
    }

    private fun pintarLayout(listaPost: List<PostResult>) {
        var adapter = PostAdapter(this, R.layout.row_post, listaPost)
        rvListaPost.layoutManager = LinearLayoutManager(this)
        rvListaPost.adapter = adapter
    }

    private fun addPost() {
        alert {
            customView {
                verticalLayout{
                    val etTitulo = editText{
                        hint = "titulo"
                    }
                    positiveButton("Aceptar"){
                        val post: Post = Post(user.email!!,etTitulo.text.toString())
                        guardarPost(post)
                    }
                    negativeButton("Cancelar"){}
                }
            }
        }.show()
    }

    private fun guardarPost(post: Post) {
        db.collection("Post").add(post)
    }

    private fun getUser(){
        if (mAuth.currentUser != null) {
            user = mAuth.currentUser!!
        }else{
            //volver a la pantalla de login
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
