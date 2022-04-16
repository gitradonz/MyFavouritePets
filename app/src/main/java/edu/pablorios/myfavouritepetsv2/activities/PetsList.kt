package edu.pablorios.myfavouritepetsv2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import edu.pablorios.myfavouritepetsv2.AddPetsActivity
import edu.pablorios.myfavouritepetsv2.MainActivity
import edu.pablorios.myfavouritepetsv2.R
import edu.pablorios.myfavouritepetsv2.adapters.AdaptadorRecyclerView
import edu.pablorios.myfavouritepetsv2.databinding.ActivityPetsListBinding
import edu.pablorios.myfavouritepetsv2.model.Pets
import edu.pablorios.myfavouritepetsv2.utils.MiSQL
import java.util.*

open class PetsList : AppCompatActivity(), AdaptadorRecyclerView.ItemLongClickListener {

        private lateinit var binding: ActivityPetsListBinding
        private lateinit var db: MiSQL
        private lateinit var database: DatabaseReference
        private lateinit var dbMyFavPets: DatabaseReference

        val adapter: AdaptadorRecyclerView = AdaptadorRecyclerView()
        var pets: MutableList<Pets> = arrayListOf()

        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding = ActivityPetsListBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Se establece la referencia a bbdd de Firestore y a su hijo MyFavPets
            database = FirebaseDatabase.getInstance().reference
            dbMyFavPets = database.child(getString(R.string.firestorePath))



            // Seteamos el longClick al adapter
            adapter.setLongClickListener(this)

            // Obtenemos el array de strings con los ordenes
            val misSorts = resources.getStringArray(R.array.sort)

            // Rellenamos nuestra mutableList con los animales
            readLog()
            
            // Settings de nuestro RV
            binding.myRVPets.setHasFixedSize(true)
            binding.myRVPets.layoutManager = LinearLayoutManager(this)
            setupRecycler()

            // Spinner con los filtros de ordenación de nuestras mascotas en el list view
            binding.spnFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(spn: AdapterView<*>,v: View,posicion: Int,id: Long) {
                    when {
                                // FILTRO POR NOMBRE
                        spn.getItemAtPosition(posicion).toString() == misSorts[1] ->  {
                            pets.sortBy { it.nom }
                            setupRecycler()
                            Snackbar.make(binding.root, R.string.ordenNombre, Snackbar.LENGTH_SHORT).show()
                        }
                                // FILTRO POR AMOROSIDAD
                        spn.getItemAtPosition(posicion).toString() == misSorts[2] -> {
                            pets.sortBy { it.amor }
                            setupRecycler()
                            Snackbar.make(binding.root, R.string.ordenAmor, Snackbar.LENGTH_SHORT).show()
                            }
                                // FILTRO POR FAVORITOS
                        spn.getItemAtPosition(posicion).toString() == misSorts[3] ->  {
                            pets.sortByDescending { it.fav }
                            setupRecycler()
                            Snackbar.make(binding.root, R.string.ordenFav, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onNothingSelected(spn: AdapterView<*>?) {}
            }


            binding.btSaveFirestore.setOnClickListener {
                dbMyFavPets.setValue(pets)
                Snackbar.make(binding.root,R.string.saveFirestore, Snackbar.LENGTH_SHORT).show()
            }


        }

        //Preparamos nuestro Recyclerview (O lo refrescamos)
        private fun setupRecycler() {
            adapter.AdaptadorRecyclerView(pets, this)
            binding.myRVPets.adapter = adapter

        }


        // Método para obtener la información persistente de nuestras mascotas
        private fun readLog(){
            val db = MiSQL(this)
            pets = db.obtenerMascotas()
            db.close()
        }

    // Método sobrecargado de LongClick en la posicion del RV seleccionada
    override fun onItemLongClick(view: View?, position: Int) {

        // Menú PopUp contextual
        PopupMenu(this, view).apply {
            inflate(R.menu.menu_longclick)
            setOnMenuItemClickListener {
                when(it!!.itemId){

                    R.id.itemEditar ->{
                        // Lanzamos intent para editar nuestra mascota
                        val intent = Intent(applicationContext, AddPetsActivity::class.java)
                        intent.putExtra(MainActivity.PET,pets[position])

                        // Borramos la mascota para reemplazarla en el guardado
                        db = MiSQL(applicationContext)
                        db.borrarMascota(pets[position].id!!)
                        pets.removeAt(position);
                        startActivity(intent)
                        true
                    }

                    R.id.itemBorrar ->{
                        // Borramos la mascota de la bbdd y de la mutableList del RV
                        db = MiSQL(applicationContext)
                        db.borrarMascota(pets[position].id!!)
                        Snackbar.make(binding.root,getString(R.string.borrarEnMenu, pets[position].nom),Snackbar.LENGTH_SHORT).show()
                        pets.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        true
                    }
                    else -> false
                }

            }
        }.show()
    }



}
