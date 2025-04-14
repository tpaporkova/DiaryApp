package com.example.diaryapp.ui.moment

import android.os.Bundle
import android.view.*
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.diaryapp.databinding.FragmentMomentListBinding
import androidx.recyclerview.widget.LinearLayoutManager

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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = adapter

        viewModel.allMoments.observe(viewLifecycleOwner) { moments ->
            adapter.submitList(moments)
            Log.d("MomentListFragment", "Moments list updated: ${moments.size}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
