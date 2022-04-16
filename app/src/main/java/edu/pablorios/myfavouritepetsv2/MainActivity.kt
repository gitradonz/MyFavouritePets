package edu.pablorios.myfavouritepetsv2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.core.view.isVisible
import edu.pablorios.myfavouritepetsv2.databinding.ActivityMainBinding
import edu.pablorios.myfavouritepetsv2.activities.PetsList
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    // Constantes para pasar nuestros datos
    companion object {
        val PET: String = "Pet"
    }
    private var inicio: Boolean = false


    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {

        // Volvemos al tema principal
        if (!inicio){
        Thread.sleep(2000)
        setTheme(R.style.Theme_MyFavouritePets)
        inicio=true
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Dejamos por defecto nuestro texto oculto invisible,
        // Se activa con un menú contextual
        binding.textView2.isVisible = false


        // Lanzaremos nuestra activity de PetsList con todos nuestros pets en un RV
        binding.btVerPets.setOnClickListener {
            val myIntent = Intent(this, PetsList::class.java)
            startActivity(myIntent)
        }

        // Botón para lanzar  nuestra activity para añadir mascotas AddPetsActivity
        binding.btAddPet.setOnClickListener {
            val myIntent = Intent(this, AddPetsActivity::class.java)
            startActivity(myIntent)
        }

    }


    // Menú PopUp contextual
    fun popupInfo(v: View){
        var bo = true
        PopupMenu(this, v).apply {
            inflate(R.menu.menu_opciones)
            setOnMenuItemClickListener {
                when(it!!.itemId){
                    R.id.opcion1 ->{
                        if (bo){
                        binding.textView2.isVisible = true
                        binding.textView.isVisible = false
                            bo = false
                            true
                        }
                        else{
                            binding.textView.isVisible = true
                            binding.textView2.isVisible = false
                            bo = true
                            true
                        }
                    }
                    else -> false
                }
            }
        }.show()
    }


}