package com.funda.newsapp.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.funda.newsapp.common.loadImage
import com.funda.newsapp.data.model.ArticleUI
import com.funda.newsapp.databinding.ItemArticleBinding

class NewsAdapter(private val articleListener: ArticleListener
) : ListAdapter<ArticleUI, NewsAdapter.ArticleViewHolder>(ArticleDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            articleListener
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ArticleViewHolder(
        private val binding: ItemArticleBinding,
        private val articleListener: ArticleListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleUI) = with(binding) {
            textViewTitle.text = article.title
            textViewSummary.text = article.summary
            textViewDate.text = article.publishedAt
            imgViewArticle.loadImage(article.imageUrl)

            root.setOnClickListener {
                articleListener.onNewsClick(article.id)
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
    }

}