package de.sysitprep.app.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import de.sysitprep.app.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatsViewModel by viewModels()

    private val progressAdapter = LernfeldProgressAdapter()
    private val sessionAdapter = SessionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLernfeldProgress.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = progressAdapter
            isNestedScrollingEnabled = false
        }

        binding.rvRecentSessions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = sessionAdapter
            isNestedScrollingEnabled = false
        }

        observeViewModel()
        viewModel.loadStats()
    }

    private fun observeViewModel() {
        viewModel.userStats.observe(viewLifecycleOwner) { stats ->
            if (stats == null || stats.totalAnswered == 0) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.layoutStats.visibility = View.GONE
                return@observe
            }
            binding.layoutEmpty.visibility = View.GONE
            binding.layoutStats.visibility = View.VISIBLE

            binding.tvTotalQuestions.text = stats.totalAnswered.toString()
            val pct = if (stats.totalAnswered > 0) stats.correctAnswers * 100 / stats.totalAnswered else 0
            binding.tvAccuracyStat.text = "$pct%"
            val hours = stats.totalStudySeconds / 3600
            val mins = (stats.totalStudySeconds % 3600) / 60
            binding.tvStudyTime.text = if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
            binding.tvSessionsCount.text = stats.completedSessions.toString()
        }

        viewModel.lernfeldProgress.observe(viewLifecycleOwner) { list ->
            progressAdapter.submitList(list)
        }

        viewModel.recentSessions.observe(viewLifecycleOwner) { sessions ->
            sessionAdapter.submitList(sessions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
