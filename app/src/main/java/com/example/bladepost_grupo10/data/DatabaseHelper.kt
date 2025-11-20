package com.example.bladepost_grupo10.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "beyblade.db"
        const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase){
        db.execSQL("""
            CREATE TABLE Usuario (
            id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL UNIQUE,
            email TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL
            );
        """)

        db.execSQL("""
            CREATE TABLE Publicaciones (
            id_publicacion INTEGER PRIMARY KEY AUTOINCREMENT,
            id_usuario INTEGER NOT NULL,
            titulo TEXT NOT NULL,
            texto TEXT NOT NULL,
            imagen TEXT,
            fecha_publicacion TEXT DEFAULT (datatime('now', 'localtime')),
            FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
            );
        """)

        db.execSQL("""
            CREATE TABLE Calificaciones (
            id_calificacion INTEGER PRIMARY KEY AUTOINCREMENT,
            id_publicacion INTEGER NOT NULL,
            id_usuario INTEGER NOT NULL,
            puntaje INTEGER NOT NULL CHECK (puntaje BETWEEN 1 AND 5),
            fecha_calificacion TEXT DEFAULT (datetime('now', 'localtime')),
            FOREIGN KEY (id_publicacion) REFERENCES Publicaciones(id_publicacion),
            FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
            UNIQUE (id_publicacion, id_usuario)
            );
        """)

        db.execSQL("""
            CREATE TABLE Comentarios (
            id_comentario INTEGER PRIMARY KEY AUTOINCREMENT,
            id_publicacion INTEGER NOT NULL,
            id_usuario INTEGER NOT NULL,
            texto TEXT NOT NULL,
            fecha_comentario TEXT DEFAULT (datetime('now', 'localtime')),
            FOREIGN KEY (id_publicacion) REFERENCES Publicaciones(id_publicacion),
            FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
            );
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
        db.execSQL("DROP TABLE IF EXISTS Calificaciones")
        db.execSQL("DROP TABLE IF EXISTS Comentarios")
        db.execSQL("DROP TABLE IF EXISTS Publicaciones")
        db.execSQL("DROP TABLE IF EXISTS Usuario")
        onCreate(db)
    }
}