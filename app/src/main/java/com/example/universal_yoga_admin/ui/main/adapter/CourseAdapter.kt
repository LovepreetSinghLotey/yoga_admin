package com.example.universal_yoga_admin.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.universal_yoga_admin.R
import com.example.universal_yoga_admin.databinding.ItemCourseBinding
import com.example.universal_yoga_admin.models.Course
import com.example.universal_yoga_admin.models.TypeOfClass

class CourseAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface CoursesProtocol{
        fun onCoursePressed(course: Course)
    }

    private val courses: ArrayList<Course> = ArrayList()
    var delegate: CoursesProtocol? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemCourseBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_course, parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as CourseViewHolder
        viewHolder.binding.title.text = "${courses[position].title} | ${courses[position].duration} min | €${courses[position].pricePerClass.toInt()}"

        viewHolder.binding.desc.text = courses[position].description
        viewHolder.binding.desc.visibility = if (courses[position].description.isEmpty()) View.GONE else ViewGroup.VISIBLE

        viewHolder.binding.capacity.text = courses[position].capacity.toString()
        viewHolder.binding.type.text = courses[position].typeOfClass
        viewHolder.binding.dateTime.text = "${courses[position].dayOfWeek.substring(0, 3)} - ${courses[position].timeOfDay}"

        when(courses[position].typeOfClass){
            TypeOfClass.FLOW.name -> viewHolder.binding.exerciseImage.setImageResource(R.drawable.yoga_one)
            TypeOfClass.AERIAL.name -> viewHolder.binding.exerciseImage.setImageResource(R.drawable.yoga_two)
            TypeOfClass.FAMILY.name -> viewHolder.binding.exerciseImage.setImageResource(R.drawable.yoga_three)
            TypeOfClass.PRIVATE.name -> viewHolder.binding.exerciseImage.setImageResource(R.drawable.yoga_four)
            TypeOfClass.CORPORATE.name -> viewHolder.binding.exerciseImage.setImageResource(R.drawable.yoga_five)
            TypeOfClass.KIDS.name -> viewHolder.binding.exerciseImage.setImageResource(R.drawable.yoga_six)
        }

        viewHolder.binding.root.setOnClickListener {
            try {
                delegate?.onCoursePressed(courses[position])
            } catch (ignored: Exception){}
        }
        viewHolder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    fun getItem(position: Int): Course? {
        return try {
            courses[position]
        }catch (ignored: Exception){
            null
        }
    }

    fun add(course: Course) {
        courses.add(course)
        notifyItemInserted(courses.size - 1)
    }

    fun addAll(courseList: List<Course>) {
        courseList.forEach { add(it) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        courses.clear()
        notifyDataSetChanged()
    }

    class CourseViewHolder(val binding: ItemCourseBinding): RecyclerView.ViewHolder(binding.root)
}