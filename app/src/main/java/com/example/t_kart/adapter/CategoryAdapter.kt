package com.example.t_kart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.t_kart.R
import com.example.t_kart.activity.CategoryActivity
import com.example.t_kart.databinding.LayoutCategoryItemBinding
import com.example.t_kart.fragments.CategoryModel
import java.net.Inet4Address

class CategoryAdapter(var context : Context, val list : ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var binding = LayoutCategoryItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.textViewItem.text = list[position].cate
        Glide.with(context).load(list[position].img).into(holder.binding.imageViewItem)
        holder.itemView.setOnClickListener{
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("cat",list[position].cate)
            context.startActivity(intent)
        }
    }
}