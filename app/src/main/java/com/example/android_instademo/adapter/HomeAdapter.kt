package com.example.android_instademo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_instademo.R
import com.example.android_instademo.fragment.HomeFragment
import com.example.android_instademo.model.Post
import com.google.android.material.imageview.ShapeableImageView

class HomeAdapter(var fragment: HomeFragment, var items: ArrayList<Post>): BaseAdapter() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_post_home, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post: Post = items[position]
        if (holder is PostViewHolder) {
            var iv_post = holder.iv_post
            val tv_fullname = holder.tv_fullname
            val iv_profile = holder.iv_profile
            val tv_caption = holder.tv_caption

            tv_fullname.text = post.fullname
            tv_caption.text = post.caption
            Glide.with(fragment)
                .load(post.userImg)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(iv_profile)
            Glide.with(fragment).load(post.postImg).into(iv_post)
        }
    }

    class PostViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var iv_profile: ShapeableImageView
        var iv_post: ShapeableImageView
        var tv_fullname: TextView
        var tv_time: TextView
        var tv_caption: TextView
        var iv_more: ImageView
        var iv_like: ImageView
        var iv_share: ImageView

        init {
            iv_profile = view.findViewById(R.id.iv_profile)
            iv_post = view.findViewById(R.id.iv_post)
            tv_fullname = view.findViewById(R.id.tv_fullname)
            tv_time = view.findViewById(R.id.tv_time)
            tv_caption = view.findViewById(R.id.tv_caption)
            iv_more = view.findViewById(R.id.iv_more)
            iv_like = view.findViewById(R.id.iv_like)
            iv_share = view.findViewById(R.id.iv_share)
        }
    }

}

