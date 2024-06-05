package com.example.proyectoinnovacionpdm2024_gt01_grupo3

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "medicamentoRecordatorio.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_PILLS = "Pastillas"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "nombre"
        const val COLUMN_DESCRIPTION = "descripcion"
        const val COLUMN_START_DATE = "fecha_inicial"
        const val COLUMN_END_DATE = "fecha_final"
        const val COLUMN_FREQUENCY = "frecuencia"
        const val COLUMN_START_TIME = "hora_inicio"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PILLS_TABLE = ("CREATE TABLE " + TABLE_PILLS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_START_DATE + " TEXT,"
                + COLUMN_END_DATE + " TEXT,"
                + COLUMN_FREQUENCY + " INTEGER,"
                + COLUMN_START_TIME + " TEXT" + ")")
        db.execSQL(CREATE_PILLS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PILLS")
        onCreate(db)
    }

    //Funcion para eliminar una pastilla
    fun deletePill(id: Int): Boolean {
        val db = this.writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val deletedRows = db.delete(TABLE_PILLS, selection, selectionArgs)
        db.close()
        return deletedRows > 0
    }
}