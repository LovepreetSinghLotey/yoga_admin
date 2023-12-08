package com.example.universal_yoga_admin.ui.course.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.universal_yoga_admin.R
import com.example.universal_yoga_admin.databinding.ItemClassBinding
import com.example.universal_yoga_admin.models.Class

class ClassAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ClassProtocol{
        fun onEditPressed(classId: String)
        fun onDeletePressed(classId: String)
    }

    private val classes: ArrayList<Class> = ArrayList()
    var delegate: ClassProtocol? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemClassBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_class, parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ClassViewHolder
        viewHolder.binding.teacher.text = classes[position].teacher
        viewHolder.binding.date.text = classes[position].date

        viewHolder.binding.edit.setOnClickListener {
            try {
                delegate?.onEditPressed(classes[position].id)
            } catch (ignored: Exception){}
        }
        viewHolder.binding.delete.setOnClickListener {
            try {
                delegate?.onDeletePressed(classes[position].id)
            } catch (ignored: Exception){}
        }
        viewHolder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return classes.size
    }

    fun getItem(position: Int): Class? {
        return try {
            classes[position]
        }catch (ignored: Exception){
            null
        }
    }

    fun add(classModel: Class) {
        classes.add(classModel)
        notifyItemInserted(classes.size - 1)
    }

    fun addAll(classList: List<Class>) {
        classList.forEach { add(it) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        classes.clear()
        notifyDataSetChanged()
    }

    fun remove(classId: String) {
        classes.removeAll { it.id == classId }
        notifyDataSetChanged()
    }

    class ClassViewHolder(val binding: ItemClassBinding): RecyclerView.ViewHolder(binding.root)
}