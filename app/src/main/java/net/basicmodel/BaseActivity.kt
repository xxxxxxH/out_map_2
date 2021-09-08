package net.basicmodel

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.weeboos.permissionlib.PermissionRequest
import net.fragment.InterFragment
import net.fragment.MapFragment
import net.fragment.NearByFragment
import net.fragment.StreetFragment
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    var mapFragment: MapFragment? = null
    var interFragment: InterFragment? = null
    var nearByFragment: NearByFragment? = null
    var streetFragment: StreetFragment? = null

    protected fun requestPermissions() {
        PermissionRequest.getInstance().build(this)
            .requestPermission(object : PermissionRequest.PermissionListener {
                override fun permissionGranted() {
                   showPosition(0)
                }

                override fun permissionDenied(permissions: ArrayList<String>?) {
                    finish()
                }

                override fun permissionNeverAsk(permissions: ArrayList<String>?) {
                    finish()
                }

            }, permissions)
    }

    protected fun showPosition(position: Int) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        hideAll(ft)
        if (position == 0) {
            mapFragment = fm.findFragmentByTag("map") as MapFragment?
            if (mapFragment == null) {
                mapFragment = MapFragment()
                ft.add(R.id.content, mapFragment!!, "map")
            } else {
                ft.show(mapFragment!!)
            }
        }

        if (position == 1) {
            nearByFragment = fm.findFragmentByTag("nearby") as NearByFragment?
            if (nearByFragment == null) {
                nearByFragment = NearByFragment()
                ft.add(R.id.content, nearByFragment!!, "nearby")
            } else {
                ft.show(nearByFragment!!)
            }
        }

        if (position == 2) {
            streetFragment = fm.findFragmentByTag("streetview") as StreetFragment?
            if (streetFragment == null) {
                streetFragment = StreetFragment()
                ft.add(R.id.content, streetFragment!!, "streetview")
            } else {
                ft.show(streetFragment!!)
            }
        }

        if (position == 3) {
            interFragment = fm.findFragmentByTag("interactive") as InterFragment?
            if (interFragment == null) {
                interFragment = InterFragment()
                ft.add(R.id.content, interFragment!!, "interactive")
            } else {
                ft.show(interFragment!!)
            }
        }
        ft.commit()
    }

    private fun hideAll(ft: FragmentTransaction) {
        if (mapFragment != null) {
            ft.hide(mapFragment!!)
        }
        if (nearByFragment != null) {
            ft.hide(nearByFragment!!)
        }
        if (streetFragment != null) {
            ft.hide(streetFragment!!)
        }
        if (interFragment != null) {
            ft.hide(interFragment!!)
        }
    }
}