package com.example.tracker.presentation.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tracker.R
import com.example.tracker.data.repositoryImpl.RepositoryImpl
import com.example.tracker.databinding.ActivityMainBinding
import com.example.tracker.domain.entity.Card
import com.example.tracker.domain.entity.Group
import com.example.tracker.domain.logic.AddCard
import com.example.tracker.domain.logic.AddGroup
import com.example.tracker.domain.logic.DeleteCard
import com.example.tracker.domain.logic.DeleteCardsByGroupName
import com.example.tracker.domain.logic.DeleteGroup
import com.example.tracker.domain.logic.GetAllGroups
import com.example.tracker.domain.logic.GetCard
import com.example.tracker.domain.logic.GetCardsByGroupName
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

    private val deleteGroup by lazy {
        DeleteGroup(repository)
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

    }
}
