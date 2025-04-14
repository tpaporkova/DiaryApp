package com.example.diaryapp.ui.home

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.diaryapp.databinding.FragmentHomeBinding
import com.example.diaryapp.R
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        binding.profileImageView.setImageURI(uri)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Привязываем данные ViewModel к DataBinding
        viewModel.daysTogether.observe(viewLifecycleOwner, Observer {
            binding.daysTogether = it
        })

        viewModel.anniversaryIn.observe(viewLifecycleOwner, Observer {
            binding.anniversaryIn = it
        })

        // Привязываем данные из ViewModel
        viewModel.userName.observe(viewLifecycleOwner, Observer {
            binding.userName = it
        })

        viewModel.partnerName.observe(viewLifecycleOwner, Observer {
            binding.partnerName = it
        })

        viewModel.startDate.observe(viewLifecycleOwner, Observer {
            binding.startDate = it
        })

        // Настроить обработку выбора даты
        binding.startDateEditText.setOnClickListener {
            showDatePicker()
        }

        binding.profileImageView.setOnClickListener {
            openImagePicker()
        }

        // Добавляем обработчик для кнопки добавления нового момента
        binding.buttonAddMoment.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEditMomentFragment(momentId = 0)  // передаем momentId
            findNavController().navigate(action)
        }

        // Добавляем обработчик для кнопки просмотра всех моментов
        binding.buttonViewMoments.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_momentListFragment)
        }
    }

    private fun openImagePicker() {
        pickImage.launch("image/*")
    }

    // Показываем диалог для выбора даты начала отношений
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Обновляем дату начала отношений в ViewModel
                viewModel.updateStartDate(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}