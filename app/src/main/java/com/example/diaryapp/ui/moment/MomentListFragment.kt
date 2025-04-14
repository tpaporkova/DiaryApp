package com.example.diaryapp.ui.moment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.diaryapp.R
import com.example.diaryapp.databinding.FragmentMomentListBinding

class MomentListFragment : Fragment() {

    private var _binding: FragmentMomentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MomentViewModel by viewModels {
        MomentViewModel.Factory(requireActivity().application)
    }

    private lateinit var adapter: MomentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMomentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = MomentAdapter { moment ->
            val action = MomentListFragmentDirections.actionMomentListToEditMoment(moment.id.toInt())
            findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_momentList_to_editMoment)
        }

        viewModel.allMoments.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}