package com.example.tracker.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.tracker.databinding.FragmentAddGroupBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.sealed.framentAddGroup.Error
import com.example.tracker.presentation.sealed.framentAddGroup.GroupName
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentAddGroupViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class FragmentAddGroup : Fragment() {

    private val component by lazy {
        (activity?.application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding: FragmentAddGroupBinding? = null

    @Inject
    lateinit var viewModel: FragmentAddGroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        FragmentAddGroupBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            binding = this
            return this.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding!!.buttonAddGroup.setOnClickListener {
            lifecycleScope.launch {
                viewModel.addGroup(binding!!.tietHint.text.toString())
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when(it){
                is Error -> showToast(it.text)
                is GroupName -> showToast("Группа ${it.groupName} успешно добавлена")
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}
