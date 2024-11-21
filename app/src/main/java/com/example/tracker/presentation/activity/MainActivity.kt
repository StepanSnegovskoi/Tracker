package com.example.tracker.presentation.activity

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tracker.R
import com.example.tracker.data.dbEntity.CardDbModel
import com.example.tracker.data.repositoryImpl.RepositoryImpl
import com.example.tracker.databinding.ActivityMainBinding
import com.example.tracker.domain.entity.Card
import com.example.tracker.domain.entity.Group
import com.example.tracker.domain.logic.AddCard
import com.example.tracker.domain.logic.AddGroup
import com.example.tracker.domain.logic.DeleteCard
import com.example.tracker.domain.logic.GetAllGroups
import com.example.tracker.domain.logic.GetCard
import com.example.tracker.domain.logic.GetCardsByGroupName
import com.example.tracker.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val repository by lazy {
        RepositoryImpl(application)
    }

    private val addCard by lazy {
        AddCard(repository)
    }

    private val addGroup by lazy {
        AddGroup(repository)
    }

    private val deleteCard by lazy {
        DeleteCard(repository)
    }

    private val getCard by lazy {
        GetCard(repository)
    }

    private val getAllGroups by lazy {
        GetAllGroups(repository)
    }

    private val getCardsByGroupName by lazy {
        GetCardsByGroupName(repository)
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
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {

                val card = Card(
                    id = 22,
                    name = "dw",
                    description = "feg",
                    deadline = 3,
                    groupName = "firstGroup",
                )

                addCard(
                    card
                )

                Log.d("MainActivityTestBd", getCard(22).toString())

                addCard(
                    Card(
                        name = "fgffff",
                        description = "fegghhgh",
                        deadline = 3222,
                        groupName = "firstGroup",
                    )
                )

                addGroup(
                    Group(
                        name = "firstGroup"
                    )
                )

                Log.d(
                    "MainActivityTestBd",
                    "Через 10 секунд будет удалена предпоследняя добавленная карточка"
                )

                delay(10000)

                deleteCard(card.id)

                Log.d(
                    "MainActivityTestBd",
                    getAllGroups().toString()
                )

                Log.d(
                    "MainActivityTestBd",
                    getCardsByGroupName("firstGroup").toString()
                )
            }
        }
    }
}
