package de.sysitprep.app.ui.lernfelder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.sysitprep.app.R
import de.sysitprep.app.data.model.ExamType
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.data.model.SessionType
import de.sysitprep.app.databinding.FragmentLernfelderBinding

class LernfelderFragment : Fragment() {

    private var _binding: FragmentLernfelderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LernfelderViewModel by viewModels()
    private lateinit var adapter: LernfelderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLernfelderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupChipFilter()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = LernfelderAdapter { item ->
            // Navigate to quiz with this Lernfeld
            findNavController().navigate(
                R.id.quizFragment,
                Bundle().apply {
                    putString("sessionType", SessionType.LERNFELD.name)
                    putInt("lernfeld", item.lernfeld.number)
                    putString("fachrichtung", item.fachrichtung.name)
                    putString("examType", ExamType.AP1.name)
                }
            )
        }
        binding.rvLernfelder.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLernfelder.adapter = adapter
    }

    private fun setupChipFilter() {
        binding.chipGemeinsam.setOnCheckedChangeListener { _, checked ->
            if (checked) viewModel.loadLernfelder(Fachrichtung.GEMEINSAM)
        }
        binding.chipSi.setOnCheckedChangeListener { _, checked ->
            if (checked) viewModel.loadLernfelder(Fachrichtung.SI)
        }
        binding.chipAe.setOnCheckedChangeListener { _, checked ->
            if (checked) viewModel.loadLernfelder(Fachrichtung.AE)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshCurrentTab()
    }

    private fun observeViewModel() {
        viewModel.lernfelder.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
