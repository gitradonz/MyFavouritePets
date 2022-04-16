package edu.pablorios.myfavouritepetsv2.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import edu.pablorios.myfavouritepetsv2.AddPetsActivity
import edu.pablorios.myfavouritepetsv2.MainActivity
import edu.pablorios.myfavouritepetsv2.R
import edu.pablorios.myfavouritepetsv2.activities.PetsDetalle
import edu.pablorios.myfavouritepetsv2.model.Pets
import edu.pablorios.myfavouritepetsv2.databinding.VistaPetsBinding
import edu.pablorios.myfavouritepetsv2.utils.MiSQL


class AdaptadorRecyclerView : RecyclerView.Adapter<AdaptadorRecyclerView.ViewHolder>(){

    //Variables para nuestro adaptador
    var datos: MutableList<Pets> = ArrayList()
    lateinit var context: Context
    lateinit var db: MiSQL
    private lateinit var mLongClickListener: ItemLongClickListener

    interface ItemLongClickListener {
        fun onItemLongClick(view: View?, position: Int)
    }

    fun setLongClickListener(itemLongClickListener: ItemLongClickListener){
        mLongClickListener = itemLongClickListener
    }

    // Constructor de nuestro recycler
    fun AdaptadorRecyclerView(datosList: MutableList<Pets>, context1: Context) {
        this.datos = datosList
        this.context = context1
    }

    // Va bindeando los objetos obtenidos por posicion
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = datos.get(position)
        holder.bind(item)
    }

    // Obtenemos tamaño de la lista
    override fun getItemCount(): Int {return datos.size}

    // Infla nuestro viewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(VistaPetsBinding.inflate(layoutInflater,parent, false).root)
    }


    // Clase para inflar las vistas del RecyclerView
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = VistaPetsBinding.bind(view)
        @SuppressLint("NotifyDataSetChanged", "SdCardPath")
        fun bind(petSeleccionado: Pets) {

            val db = MiSQL(context)

            // Mostramos el nombre y su imagen para cada vista
            binding.tvMostrarNombre.text = petSeleccionado.nom
            Glide.with(context).load(petSeleccionado.img)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .into(binding.imgPet)

            // Si la mascota devuelve booleano true le cambiamos la imagen de favorito (Por defecto sin favorito, false)
            if (petSeleccionado.fav > 0) {
                binding.esFavorito.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        android.R.drawable.btn_star_big_on
                    )
                )
            } else
                binding.esFavorito.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        android.R.drawable.btn_star_big_off
                    )
                )

            // Funcion para añadir o borrar de favoritos y guardarlos en el click
            binding.esFavorito.setOnClickListener {
                if (petSeleccionado.fav == 0) {
                    //Si es false lo pasamos a true y cambiamos la imagen
                    binding.esFavorito.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            android.R.drawable.btn_star_big_on
                        )
                    )
                    // Actualizamos los datos
                    datos[adapterPosition].fav = 1
                    db.modificarFav(petSeleccionado.id!!, true)
                    notifyDataSetChanged()
                    Snackbar.make(
                        binding.root,
                        R.string.petAddedFav,
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    //Si es true lo pasamos a false y cambiamos imagen
                    binding.esFavorito.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            android.R.drawable.btn_star_big_off
                        )
                    )
                    // Actualizamos los datos
                    datos[adapterPosition].fav = 0
                    db.modificarFav(petSeleccionado.id!!, false)
                    notifyDataSetChanged()
                    Snackbar.make(
                        binding.root,
                        R.string.petDeletedFav,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }

            // Funciones para mostrar, editar y borrar las mascotas en el ítem
            binding.imgEdit.setOnClickListener      { openActivityEditar(adapterPosition) }
            binding.imgBorrar.setOnClickListener    { dialogBorrar(datos[adapterPosition].nom!!) }
            binding.imgPet.setOnClickListener       { openActivityMostrar(adapterPosition) }
            itemView.setOnClickListener             { openActivityMostrar(adapterPosition) }


            // Funcion para el click largo
            itemView.setOnLongClickListener {
                mLongClickListener.onItemLongClick(it, adapterPosition)
                true }
        }

        // Dialog mostrado al borrar pet
        fun dialogBorrar(nomPet: String) {
            val builder = AlertDialog.Builder(binding.root.context)

            builder.setMessage(context.getString(R.string.dialogBorrado,nomPet))
                //Si pulsamos Ok -->
                .setPositiveButton(R.string.ok) { dialog, which ->
                    Snackbar.make(binding.root, context.getString(R.string.mascotaBorrada,nomPet), Snackbar.LENGTH_SHORT)
                        .show()
                    borrar(adapterPosition)
                }
                //Si pulsamos cancelar -->
                .setNegativeButton(R.string.cancelar)
                { dialog, which ->
                    Snackbar.make(binding.root, R.string.cancelado, Snackbar.LENGTH_SHORT).show()

                }
            builder.show()

        }


    } /// ^^^^ Fin de inner class ^^^^


    // Funcion para borrar mascotas tanto de la BBDD como de la MutableList
    fun borrar(id: Int) {
        db = MiSQL(context)
        db.borrarMascota(datos[id].id!!)
        datos.removeAt(id)
        notifyItemRemoved(id)
    }

    // Enviamos activity con la información de nuestras mascotas para mostrar
    fun openActivityMostrar(id: Int){
        val intent = Intent(context, PetsDetalle::class.java)
        intent.putExtra(MainActivity.PET,datos[id])
        context.startActivity(intent)
    }

    //Enviamos activity con la información de nuestras mascotas para editar
    fun openActivityEditar(id: Int){
        val intent = Intent(context, AddPetsActivity::class.java)
        intent.putExtra(MainActivity.PET,datos[id])

        //Borramos la mascota para reemplazarla en el guardado
        db = MiSQL(context)
        db.borrarMascota(datos.get(id).id!!)
        datos.removeAt(id);
        context.startActivity(intent)
    }
}