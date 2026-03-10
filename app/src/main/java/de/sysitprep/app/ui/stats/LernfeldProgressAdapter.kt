package de.sysitprep.app.ui.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.sysitprep.app.R
import de.sysitprep.app.data.model.LernfeldProgress

class LernfeldProgressAdapter : ListAdapter<LernfeldProgress, LernfeldProgressAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLfNumber: TextView = view.findViewById(R.id.tv_lf_number)
        val tvLfTitle: TextView = view.findViewById(R.id.tv_lf_title)
        val tvProgressText: TextView = view.findViewById(R.id.tv_progress_text)
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar_lf)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lernfeld_progress, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvLfNumber.text = "LF ${item.lernfeld}"
        holder.tvLfTitle.text = item.title
        val progress = if (item.totalQuestions > 0)
            (item.correctAnswers * 100 / item.totalQuestions)
        else 0
        holder.tvProgressText.text = "${item.correctAnswers}/${item.totalQuestions} richtig"
        holder.progressBar.progress = progress
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LernfeldProgress>() {
            override fun areItemsTheSame(old: LernfeldProgress, new: LernfeldProgress) = old.lernfeld == new.lernfeld
            override fun areContentsTheSame(old: LernfeldProgress, new: LernfeldProgress) = old == new
        }
    }
}
