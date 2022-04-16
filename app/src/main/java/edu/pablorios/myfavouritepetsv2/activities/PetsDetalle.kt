package edu.pablorios.myfavouritepetsv2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import edu.pablorios.myfavouritepetsv2.*
import edu.pablorios.myfavouritepetsv2.databinding.ActivityPetsDetalleBinding
import edu.pablorios.myfavouritepetsv2.model.Pets

class PetsDetalle : AppCompatActivity(){
    private lateinit var binding: ActivityPetsDetalleBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetsDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pet = intent.getSerializableExtra(MainActivity.PET) as? Pets


        // Se crea el adapter.
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // Se añaden los fragments y los títulos de pestañas.
        adapter.addFragment(FragmentInformacion(pet!!, applicationContext), "Detalles")
        adapter.addFragment(FragmentUbicacion(pet), "Ubicacion")


        // Se asocia el adapter.
        binding.viewPager.adapter = adapter

        // Se cargan las tabs.
        binding.tabs.setupWithViewPager(binding.viewPager)
        // ----------------------------------------------------------------

    }


}