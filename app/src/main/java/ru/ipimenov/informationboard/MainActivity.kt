package ru.ipimenov.informationboard

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import ru.ipimenov.informationboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
    }

    private fun init() {
        with(binding) {
            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                idDrawerLayout,
                idMainContent.idToolbar,
                R.string.open,
                R.string.close
            )
            idDrawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            idNavView.setNavigationItemSelectedListener(this@MainActivity)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.id_my_ads -> {
                Toast.makeText(this, "Нажали id_my_ads", Toast.LENGTH_SHORT).show()
            }
            R.id.id_cars -> {
                Toast.makeText(this, "Нажали id_cars", Toast.LENGTH_SHORT).show()
            }
            R.id.id_pcs -> {
                Toast.makeText(this, "Нажали id_pcs", Toast.LENGTH_SHORT).show()
            }
            R.id.id_smartphones -> {
                Toast.makeText(this, "Нажали id_smartphones", Toast.LENGTH_SHORT).show()
            }
            R.id.id_dms_appliances -> {
                Toast.makeText(this, "Нажали id_dms_appliances", Toast.LENGTH_SHORT).show()
            }
            R.id.id_sign_up -> {
                Toast.makeText(this, "Нажали id_sign_up", Toast.LENGTH_SHORT).show()
            }
            R.id.id_sign_in -> {
                Toast.makeText(this, "Нажали id_sign_in", Toast.LENGTH_SHORT).show()
            }
            R.id.id_sign_out -> {
                Toast.makeText(this, "Нажали id_sign_out", Toast.LENGTH_SHORT).show()
            }
        }
        binding.idDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}