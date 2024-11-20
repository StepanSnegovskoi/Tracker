package com.example.tracker.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tracker.R
import com.example.tracker.data.CardDatabase
import com.example.tracker.data.CardDbModel
import com.example.tracker.data.CardRepositoryImpl
import com.example.tracker.databinding.ActivityMainBinding
import com.example.tracker.domain.AddCard
import com.example.tracker.domain.Card
import com.example.tracker.domain.DeleteCard
import com.example.tracker.domain.GetAllCards
import com.example.tracker.domain.GetCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    companion object {
        private const val TAG = "MainActivityTestBd"
    }
}
