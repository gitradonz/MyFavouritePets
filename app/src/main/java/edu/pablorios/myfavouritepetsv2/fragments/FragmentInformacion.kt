package edu.pablorios.myfavouritepetsv2

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.util.Linkify
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import edu.pablorios.myfavouritepetsv2.activities.PetsList
import edu.pablorios.myfavouritepetsv2.databinding.FragmentInformacionBinding
import edu.pablorios.myfavouritepetsv2.model.Pets
import edu.pablorios.myfavouritepetsv2.utils.MiSQL


class FragmentInformacion(pet: Pets, contexto: Context) : Fragment() {

    private lateinit var binding: FragmentInformacionBinding
    private lateinit var db: MiSQL

    val miContexto: Context = contexto
    var petRecibido = pet

    // Inflamos la vista con nuestro layout de historico
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInformacionBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            // Asignamos la imagen mediante Glide
            Glide.with(this)
                .load(petRecibido.img)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .into(binding.ivImg)

            // Mostramos en nuestras vistas la informacion obtenida mediante los gets de nuestra activity
            binding.tvNombreMostrado.text = petRecibido.nom
            binding.tvNombreCientMostrado.text = petRecibido.nomCie
            binding.tvPelajeMostrado.text = petRecibido.pel
            binding.tvClaseMostrado.text = petRecibido.clase
            binding.tvLinkMostrado.text = petRecibido.link
            binding.rbAmorosidadMostrado.rating= petRecibido.amor!!

            // Con Linkify obtendremos un link clickable de nuestras URL añadidas
            Linkify.addLinks(binding.tvLinkMostrado, Linkify.WEB_URLS);


        // Funcion en el click del boton flotante
        binding.btFlotante.setOnClickListener { view ->
            val builder = AlertDialog.Builder(binding.root.context)

            builder.setMessage(getString(R.string.dialogBorrado,petRecibido.nom))
                //Si pulsamos Ok -->
                .setPositiveButton(R.string.ok) { dialog, which ->
                    Snackbar.make(binding.root, R.string.mascotaBorrada, Snackbar.LENGTH_SHORT)
                        .show()
                    // Borramos la mascota de la base de datos y volvemos a la lista
                    db = MiSQL(miContexto)
                    db.borrarMascota(petRecibido.id!!)
                    db.close()
                    val intent = Intent(miContexto, PetsList::class.java)
                    startActivity(intent)
                    Toast.makeText(context,getString(R.string.mascotaXBorrada,petRecibido.nom),Toast.LENGTH_SHORT).show()
                }
                //Si pulsamos cancelar -->
                .setNegativeButton(R.string.cancelar)
                { dialog, which ->
                    Snackbar.make(binding.root, getString(R.string.cancelarJK,petRecibido.nom), Snackbar.LENGTH_SHORT).show()

                }
            builder.show()

        }

    }








}