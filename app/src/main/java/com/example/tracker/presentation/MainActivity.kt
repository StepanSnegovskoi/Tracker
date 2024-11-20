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

    private val addCard by lazy {
        AddCard(CardRepositoryImpl(application))
    }

    private val deleteCard by lazy {
        DeleteCard(CardRepositoryImpl(application))
    }
    private val getCard by lazy {
        GetCard(CardRepositoryImpl(application))
    }
    private val getAllCards by lazy {
        GetAllCards(CardRepositoryImpl(application))
    }

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

        binding.buttonGetCard.setOnClickListener {
            val id = binding.editTextId.text.toString().toInt()
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val idContains = getAllCards().map { it.id }.contains(id)
                    when (idContains) {

                        true -> {
                            val cardString = getCard(binding.editTextId.text.toString().toInt()).toString()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    cardString,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        false -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Элемента с таким айди в базе данных нету",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        binding.buttonAddCardToDb.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    addCard(
                        Card(
                            name = "name",
                            description = "description",
                            deadline = 1
                        )
                    )
                }
            }
        }

        binding.buttonGetAllCards.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    Log.d(TAG, getAllCards().toString())
                    val cardsString = getAllCards().toString()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, cardsString, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.buttonDeleteCardFromDb.setOnClickListener {
            val id = binding.editTextId.text.toString().toInt()
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val idContains = getAllCards().map { it.id }.contains(id)
                    when (idContains) {

                        true -> {
                            deleteCard(id).toString()
                        }

                        false -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Элемента с таким айди в базе данных нету",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivityTestBd"
    }
}


