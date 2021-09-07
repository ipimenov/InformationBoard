package ru.ipimenov.informationboard.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.databinding.ActivityEditAdsBinding

class EditAdsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}