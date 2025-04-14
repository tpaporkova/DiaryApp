package com.example.diaryapp.ui.home

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.diaryapp.R
import com.example.diaryapp.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            viewModel.updateProfileImage(uri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startDateEditText.setOnClickListener { showDatePicker() }

        binding.profileImageView.setOnClickListener { pickImage.launch("image/*") }

        binding.buttonSaveNames.setOnClickListener {
            val userName = binding.editUserName.text.toString()
            val partnerName = binding.editPartnerName.text.toString()
            val startDate = viewModel.startDate.value ?: ""
            viewModel.updateUserData(userName, partnerName, startDate)
        }

        viewModel.profileImageUri.observe(viewLifecycleOwner, Observer { uri ->
            if (uri != null) {
                binding.profileImageView.setImageURI(uri)
            } else {
                binding.profileImageView.setImageResource(R.drawable.ic_profile)
            }
        })

        binding.buttonAddMoment.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToEditMomentFragment(momentId = 0)
            findNavController().navigate(action)
        }

        binding.buttonViewMoments.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_momentListFragment)
        }
    }

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
