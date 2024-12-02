package com.example.tracker.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tracker.databinding.FragmentAddGroupBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.sealed.fragmentAddGroup.Error
import com.example.tracker.presentation.sealed.fragmentAddGroup.GroupName
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentAddGroupViewModel
import javax.inject.Inject


class FragmentAddGroup : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var binding: FragmentAddGroupBinding

    @Inject
    lateinit var viewModel: FragmentAddGroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

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
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddGroup.setOnClickListener {
            viewModel.addGroup(binding.textInputLayoutHint.text.toString())
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when(it){
                is Error -> showToast(it.errorText)
                is GroupName -> showToast("Группа ${it.groupName} успешно добавлена")
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}
