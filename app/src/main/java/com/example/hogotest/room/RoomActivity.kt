package com.example.hogotest.room

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomActivity : AppCompatActivity() {
    val userDao = db.userDao()
    val bookDao = db.bookDao()
    var searchText = ""
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {
                Page(insertUser = { insertUser() }, insertBook = {insertBook()})
                Button(onClick = {
                     lifecycleScope.launch(Dispatchers.IO) {
                       val books  = userDao.getAllBooks()
                         books.forEach {
                             println(it)
                         }
                     }
                }) {
                    Text(text = "查询")
                }

                SearchBox()

            }
        }
    }

    fun insertUser(){
        lifecycleScope.launch(Dispatchers.IO) {
            userDao.insert(User(
                name = "santi",
                email = "liucixin",
                age = 3,
                id = 0
            ))
        }

    }

    fun insertBook(){
        lifecycleScope.launch(Dispatchers.IO) {
            bookDao.insert(Book(
                name = "三体",
                author = "刘慈欣",
                pages = 100,
                id = 0,
                student_id = 1
            ))
        }
    }


}

@Composable
fun InsertUser(onClick: () -> Unit) {

    Button(onClick = onClick) {
        Text(text = "插入用户")
    }
}

@Composable
fun InsertBook(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "插入图书")
    }
}

@Composable
fun SearchBox(){
    var searchText by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    Row() {
       TextField(value = searchText, onValueChange = {
           searchText = it
       })

        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                val sb = StringBuilder()
                sb.append("%")
                sb.append(searchText.replace("\n",""))
                sb.append("%")
                val books =    db.bookDao().getAllBooks(sb.toString())
                books.forEach{
                    println(it)
                }
            }
        }) {
          Text(text = "搜索")
        }
    }

}



@Composable

fun Page(insertUser: () -> Unit,insertBook:()->Unit) {
    Column(Modifier.fillMaxWidth()) {
        Row() {
            InsertUser(onClick = insertUser)

            InsertBook(onClick = insertBook)
        }
        Users()





    }


}


@Composable
fun Users() {

}

@Composable
fun User(user: User) {
    Column {
        Text(text = user.id.toString())
        Text(text = user.name!!)
        Text(text = user.email!!)
        Text(text = user.age.toString())

    }
}


