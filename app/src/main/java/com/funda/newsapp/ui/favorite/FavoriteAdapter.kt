package com.funda.newsapp.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.funda.newsapp.common.loadImage
import com.funda.newsapp.data.model.ArticleUI
import com.funda.newsapp.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val articleListener: ArticleListener
) : ListAdapter<ArticleUI, FavoriteAdapter.ArticleViewHolder>(ArticleDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            articleListener
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ArticleViewHolder(
        private val binding: ItemFavoriteBinding,
        private val articleListener: ArticleListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleUI) = with(binding) {
            textViewTitle.text = article.title
            textViewSummary.text = article.summary
            imgViewArticle.loadImage(article.imageUrl)

            root.setOnClickListener {
                articleListener.onNewsClick(article.id)
            }

            imgDelete.setOnClickListener {
                articleListener.onDeleteClick(article)
            }
        }
    }

    class ArticleDiffCallBack : DiffUtil.ItemCallback<ArticleUI>() {
        override fun areItemsTheSame(oldItem: ArticleUI, newItem: ArticleUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArticleUI, newItem: ArticleUI): Boolean {
            return oldItem == newItem
        }
    }

    interface ArticleListener {
        fun onNewsClick(id: Int)
        fun onDeleteClick(article: ArticleUI)
    }

}