package de.sysitprep.app.ui.lernfelder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.sysitprep.app.R
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.databinding.ItemLernfeldBinding

class LernfelderAdapter(
    private val onItemClick: (LernfeldUiItem) -> Unit
) : ListAdapter<LernfeldUiItem, LernfelderAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: ItemLernfeldBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LernfeldUiItem) {
            val lf = item.lernfeld
            val fachrichtung = item.fachrichtung

            // LF Number badge
            binding.tvLfNumber.text = "LF${lf.number}"
            val bgColor = when (fachrichtung) {
                Fachrichtung.SI -> binding.root.context.getColor(R.color.lf_si)
                Fachrichtung.AE -> binding.root.context.getColor(R.color.lf_ae)
                Fachrichtung.GEMEINSAM -> binding.root.context.getColor(R.color.lf_gemeinsam)
            }
            binding.tvLfNumber.background.setTint(bgColor)

            // Fachrichtung badge
            binding.tvFachrichtungBadge.text = when {
                lf.fachrichtungen.contains(Fachrichtung.GEMEINSAM) -> "Gemeinsam"
                lf.fachrichtungen.contains(Fachrichtung.SI) -> "SI"
                lf.fachrichtungen.contains(Fachrichtung.AE) -> "AE"
                else -> ""
            }
            binding.tvFachrichtungBadge.background.setTint(bgColor)

            // Content
            binding.tvTitle.text = lf.title
            binding.tvDescription.text = lf.description
            binding.tvQuestionCount.text = "${item.questionCount} Fragen"

            // Progress
            binding.progressBar.progress = item.progress
            binding.tvProgressPercent.text = "${item.progress}%"

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLernfeldBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<LernfeldUiItem>() {
        override fun areItemsTheSame(old: LernfeldUiItem, new: LernfeldUiItem) =
            old.lernfeld.number == new.lernfeld.number && old.fachrichtung == new.fachrichtung
        override fun areContentsTheSame(old: LernfeldUiItem, new: LernfeldUiItem) = old == new
    }
}
