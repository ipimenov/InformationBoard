package ru.ipimenov.informationboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.ipimenov.informationboard.accounthelper.AccountHelper
import ru.ipimenov.informationboard.activities.EditAdsActivity
import ru.ipimenov.informationboard.adapters.AdvertListRVAdapter
import ru.ipimenov.informationboard.databinding.ActivityMainBinding
import ru.ipimenov.informationboard.dialoghelper.DialogHelper
import ru.ipimenov.informationboard.viewmodel.FirebaseViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tvAccountEmail: TextView
    private val dialogHelper = DialogHelper(this)
    val myAuth = Firebase.auth
    val adapter = AdvertListRVAdapter(myAuth)
    private val firebaseViewModel: FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        initRecyclerView()
        initViewModel()
        firebaseViewModel.loadAllAdverts()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_new_ads) {
            val intent = Intent(this, EditAdsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AccountHelper.SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    Log.d("MyLog", "Api 0")
                    dialogHelper.accountHelper.signInFirebaseWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.d("MyLog", "Api error: ${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        uIUpdate(myAuth.currentUser)
    }

    private fun initViewModel() {
        firebaseViewModel.advertLiveData.observe(this, {
            adapter.updateAdapter(it)
        })
    }

    private fun init() {
        with(binding) {
            setSupportActionBar(idMainContent.idToolbar)
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
            tvAccountEmail = idNavView.getHeaderView(0).findViewById(R.id.id_tv_account_email)
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            idMainContent.rvAdvertisements.layoutManager = LinearLayoutManager(this@MainActivity)
            idMainContent.rvAdvertisements.adapter = adapter
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
                dialogHelper.createSignUpDialog()
            }
            R.id.id_sign_in -> {
                dialogHelper.createSignInDialog()
            }
            R.id.id_sign_out -> {
                uIUpdate(null)
                myAuth.signOut()
                dialogHelper.accountHelper.signOutGoogle()
            }
        }
        binding.idDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uIUpdate(user: FirebaseUser?) {
        tvAccountEmail.text = if (user == null) {
            getString(R.string.acc_not_reg)
        } else {
            user.email
        }
    }
}