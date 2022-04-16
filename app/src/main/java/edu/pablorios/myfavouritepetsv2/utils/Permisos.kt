package edu.pablorios.myfavouritepetsv2.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// Clase para pedir permiso de cámara (en nuestro caso,podria ser cualquier otro) y
// mostrar un dialog previo a pedir el permiso
class Permisos(val activity: Activity, val permiso: String, val code: Int) {
    fun permisos(): Boolean {
        if (ContextCompat.checkSelfPermission(activity, permiso) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permiso)) {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Se necesita acceso a la galeria")
                builder.setMessage("Se requieren permisos para acceder a la galeria")
                builder.setPositiveButton("ACEPTO") { _, _ ->
                    ActivityCompat.requestPermissions(activity, arrayOf(permiso), code)
                }
                builder.setNeutralButton("NO QUIERO DAR PERMISOS", null)
                builder.show()
            } else ActivityCompat.requestPermissions(activity, arrayOf(permiso),code)
        }
        return ContextCompat.checkSelfPermission(activity, permiso) == PackageManager.PERMISSION_GRANTED
    }
}