package edu.pablorios.myfavouritepetsv2

import android.provider.MediaStore
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import edu.pablorios.myfavouritepetsv2.databinding.ActivityAddPetsBinding
import edu.pablorios.myfavouritepetsv2.model.Pets
import edu.pablorios.myfavouritepetsv2.utils.MiSQL
import edu.pablorios.myfavouritepetsv2.utils.Permisos


class AddPetsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPetsBinding
    private val MY_PERMISSIONS_REQUEST_CODE = 1234
    var imgUri: Uri? = null



        @SuppressLint("QueryPermissionsNeeded")
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

            // Obtenemos nuestros arrays de strings con las clases y pelaje
            val arrayPelaje = resources.getStringArray(R.array.pelaje)
            val arrayClase = resources.getStringArray(R.array.clases)

        binding.btAddImg.isVisible = true

        // Pet recibido del intent
        val pet = intent.getSerializableExtra(MainActivity.PET) as? Pets
        imgUri = pet?.img?.toUri();

        // Si nombre no es nulo/vacio y estamos editando, asignamos los campos de nuestro animal
        // para editarlos con los datos ya asignados
        if(pet?.nom.toString().isNotEmpty()) {
            binding.entryNom.setText(pet?.nom)
            binding.entryCie.setText(pet?.nomCie)
            binding.entryLink.setText(pet?.link)
            imgUri= pet?.img?.toUri()
            binding.btAdd.setText(R.string.lbSaveEdit)
            when(pet?.pel){
                arrayPelaje[0]-> binding.spPelaje.setSelection(0)
                arrayPelaje[1]-> binding.spPelaje.setSelection(1)
                arrayPelaje[2]-> binding.spPelaje.setSelection(2)
                arrayPelaje[3]-> binding.spPelaje.setSelection(3)
            }
            when(pet?.clase){
                arrayClase[0]-> binding.spClase.setSelection(0)
                arrayClase[1]-> binding.spClase.setSelection(1)
                arrayClase[2]-> binding.spClase.setSelection(2)
                arrayClase[3]-> binding.spClase.setSelection(3)
            }
        }


        // Botón para añadir mascotas
        binding.btAdd.setOnClickListener {
            // Comprobamos que los campos no esten vacios
            if (!binding.entryNom.text.isBlank()
                && binding.entryNom.text.length < 10
                && imgUri!=null
                && binding.entryLink.text.isNotBlank()){
                    guardarPet()
                Toast.makeText(this,R.string.mascotaGuardada, Toast.LENGTH_SHORT).show()
                }else{
                Snackbar.make(binding.root, R.string.rellenarCampo, Snackbar.LENGTH_SHORT).show()
            }

        }


        // Botón para añadir imagenes
        binding.btAddImg.setOnClickListener {
            // Comprobamos permisos mediante checkSelfPermission()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    if (Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).resolveActivity(packageManager) != null)
                    {
                        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), MY_PERMISSIONS_REQUEST_CODE)
                    }
                }else{
                    var permiso = Permisos(this,Manifest.permission.READ_EXTERNAL_STORAGE,MY_PERMISSIONS_REQUEST_CODE)
                    permiso.permisos()
                }
        }
    }

    // Funcion para guardar nuestras mascotas en la base de datos
    fun guardarPet(){
            val db = MiSQL(this)
                db.addMascota(
                    binding.entryNom.text.toString(),
                    binding.entryCie.text.toString(),
                    binding.spPelaje.selectedItem.toString(),
                    binding.spClase.selectedItem.toString(),
                    binding.entryLink.text.toString(),
                    imgUri.toString(),
                    binding.rbAmor.rating,
                    0)
                db.close()

            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
    }

    // Asignamos la foto recibida a nuestra variable de foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (intent != null) imgUri = intent.data
                Toast.makeText(this,R.string.imgAdded, Toast.LENGTH_LONG).show()
                binding.btAddImg.isVisible = false
                super.onActivityResult(requestCode, resultCode, intent)
            }else{
                Toast.makeText(this,R.string.imgNotAdded,Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Sobreescribimos para guardar mascotas en caso de cancelar cambios
    override fun onBackPressed() {
        Toast.makeText(this, R.string.cancelado2, Toast.LENGTH_LONG).show()
        if (!binding.entryNom.text.isBlank()
            && binding.entryNom.text.length < 10
            && imgUri!=null
            && binding.entryLink.text.isNotBlank()){
        guardarPet()
        }
        super.onBackPressed()
    }

}

