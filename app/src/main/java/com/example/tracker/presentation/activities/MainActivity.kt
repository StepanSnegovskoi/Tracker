package com.example.tracker.presentation.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tracker.R
import com.example.tracker.databinding.ActivityMainBinding
import com.example.tracker.domain.repository.Repository
import com.example.tracker.presentation.App
import com.example.tracker.presentation.fragments.FragmentAddGroup
import com.example.tracker.presentation.viewmodelfactory.ViewModelFactory
import com.example.tracker.presentation.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as App).component
    }

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
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                viewModel.log()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, FragmentAddGroup.newInstance("1", "2"), null).commit()

    }
}

/*
        var flag = false
        binding.linearLayoutCard.setOnClickListener {
            when (flag) {

                false -> {
                    binding.textViewDescription.visibility = View.GONE
                    flag = true
                }

                true -> {
                    binding.textViewDescription.visibility = View.VISIBLE
                    flag = false
                }
            }
        }
 */
