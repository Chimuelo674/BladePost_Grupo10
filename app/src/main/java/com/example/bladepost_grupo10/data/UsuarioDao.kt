package com.example.bladepost_grupo10.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class UsuarioDao(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun registrarUsuario(username: String, email: String, password: String): Boolean{
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("password", password)
        }

        return try {
            val result = db.insert("Usuario",null, values)
            db.close()
            result != -1L
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    fun verificarUsuarioExistente(username: String, email: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM Usuario WHERE username = ? OR email = ?",
            arrayOf(username, email)
        )
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }

    fun verificarCredenciales(usernameOrEmail: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val query = """
            SELECT * FROM Usuario
            WHERE (username = ? OR email = ?) AND password = ?
        """
        val cursor = db.rawQuery(query, arrayOf(usernameOrEmail.trim(), usernameOrEmail.trim(), password.trim()))
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }
}


