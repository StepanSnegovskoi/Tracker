package com.example.tracker.presentation.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.tracker.R
import com.example.tracker.databinding.ActivityMainBinding
import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.useCases.AddCardUseCase
import com.example.tracker.presentation.App
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val component by lazy {
        (application as App).component
    }

    @Inject
    lateinit var addCardUseCase: AddCardUseCase

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: MainViewModel

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

        component.inject(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController: NavController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)



    }
}

/* Добавить карты
lifecycleScope.launch {
            withContext(Dispatchers.IO){
                addCardUseCase(
                    Card(
                        name = "Card1",
                        groupName = "gyy",
                        description = "description1",
                        deadline = 2
                    )
                )
                addCardUseCase(
                    Card(
                        name = "Card2",
                        groupName = "gyy",
                        description = "description2",
                        deadline = 1
                    )
                )
                addCardUseCase(
                    Card(
                        name = "Card3",
                        groupName = "gyy",
                        description = "description3",
                        deadline = 3
                    )
                )
            }
        }
 */

