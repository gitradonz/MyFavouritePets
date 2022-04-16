package edu.pablorios.myfavouritepetsv2.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import edu.pablorios.myfavouritepetsv2.model.Pets

class MiSQL(context: Context): SQLiteOpenHelper(context, "pets.db",null,1) {

    companion object{
        val NOMBRE_TABLA = "pets"
        val CAMPO_ID = "_id"
        val CAMPO_NOMBRE = "nombre"
        val CAMPO_NOMBRE_CIE = "nombre_cientifico"
        val CAMPO_PELAJE = "pelaje"
        val CAMPO_CLASE = "clase"
        val CAMPO_IMG = "imagen"
        val CAMPO_LINK = "link"
        val CAMPO_AMOR = "amor"
        val CAMPO_FAV = "fav"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val create = "CREATE TABLE $NOMBRE_TABLA "+
                "($CAMPO_ID INTEGER PRIMARY KEY AUTOINCREMENT, $CAMPO_NOMBRE TEXT, $CAMPO_NOMBRE_CIE TEXT," +
                " $CAMPO_PELAJE TEXT, $CAMPO_CLASE TEXT, $CAMPO_IMG TEXT, $CAMPO_LINK TEXT, " +
                "$CAMPO_AMOR INTEGER, $CAMPO_FAV BOOL)"

        p0!!.execSQL(create)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA")
        onCreate(p0)
    }

    fun addMascota(nombre: String, nomCie: String, pelaje: String, clase: String,
                   imagen: String, link: String, amor: Float, fav: Int){
        val datos = ContentValues()
        datos.put(CAMPO_NOMBRE,nombre)
        datos.put(CAMPO_NOMBRE_CIE,nomCie)
        datos.put(CAMPO_PELAJE,pelaje)
        datos.put(CAMPO_CLASE,clase)
        datos.put(CAMPO_IMG,imagen)
        datos.put(CAMPO_LINK,link)
        datos.put(CAMPO_AMOR,amor)
        datos.put(CAMPO_FAV,fav)

        val db = this.writableDatabase
        db.insert(NOMBRE_TABLA,null,datos)
        db.close()
    }

    fun borrarMascota(id: Int): Int{
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val borrado = db.delete(NOMBRE_TABLA,"$CAMPO_ID = ?",args)
        db.close()
        return borrado
    }

    @SuppressLint("Recycle")
    fun obtenerMascotas(): MutableList<Pets> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $NOMBRE_TABLA",null
        )

        val list: MutableList<Pets> = ArrayList()

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Pets(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getFloat(7),
                        cursor.getInt(8)
                    )
                )
            } while (cursor.moveToNext())
        }
        db.close()

        return list
    }

    fun modificarFav(id: Int, b: Boolean){

        val db = this.writableDatabase

        if (b){
            val dataToInsert = ContentValues()
            dataToInsert.put(CAMPO_FAV,1)
            db.update(NOMBRE_TABLA, dataToInsert, "$CAMPO_ID=$id",null)
        }else{
            val dataToInsert = ContentValues()
            dataToInsert.put(CAMPO_FAV,0)
            db.update(NOMBRE_TABLA, dataToInsert, "$CAMPO_ID=$id",null)
        }

        db.close()
    }

 }