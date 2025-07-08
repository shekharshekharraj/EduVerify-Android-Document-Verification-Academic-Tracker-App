package com.example.eduverify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eduverify.databinding.ItemDocumentBinding

class DocumentsAdapter(private val urls: List<String>) :
    RecyclerView.Adapter<DocumentsAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(val binding: ItemDocumentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val binding = ItemDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val url = urls[position]
        Glide.with(holder.binding.root.context)
            .load(url)
            .into(holder.binding.ivDocument)
    }

    override fun getItemCount(): Int = urls.size
}
