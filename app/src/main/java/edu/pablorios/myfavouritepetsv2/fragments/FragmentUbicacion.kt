package edu.pablorios.myfavouritepetsv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.pablorios.myfavouritepetsv2.model.Pets


class FragmentUbicacion(mascotaRecibida: Pets) : Fragment() {

    // Función a ejecutar cuando se carga el mapa
    private val callback = OnMapReadyCallback {
        googleMap ->
        googleMap.addMarker(MarkerOptions().position(LatLng(47.00, 45.00)).title(
            getString(R.string.tituloUbicacion,mascotaRecibida.nom)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(47.00, 45.00)))
    }

    // Inflamos el mapa
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ubicacion, container, false)
    }

    // Asignamos el fragment del mapa una vez terminado el createView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}



