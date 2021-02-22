package com.chw.layoutmanager.compat.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.rv_list).adapter = MyAdapter()
    }
}

class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
    private val list = listOf("1", "2", "3", "4", "5")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_item, parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvValue.text = list[position]
    }

    override fun getItemCount(): Int = list.size
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvValue: TextView = view.findViewById(R.id.tv_value)
}