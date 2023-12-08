package com.example.universal_yoga_admin.ui.course

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.universal_yoga_admin.R
import com.example.universal_yoga_admin.databinding.ActivityCourseBinding
import com.example.universal_yoga_admin.db.ClassDbHelper
import com.example.universal_yoga_admin.db.CourseDbHelper
import com.example.universal_yoga_admin.extensions.hideLoader
import com.example.universal_yoga_admin.extensions.showLoader
import com.example.universal_yoga_admin.models.Course
import com.example.universal_yoga_admin.models.TypeOfClass
import com.example.universal_yoga_admin.ui.add_class.AddClassActivity
import com.example.universal_yoga_admin.ui.add_course.AddCourseActivity
import com.example.universal_yoga_admin.ui.course.adapter.ClassAdapter
import com.example.universal_yoga_admin.ui.main.adapter.CourseAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CourseDetailActivity : AppCompatActivity() {

    @Inject lateinit var courseDbHelper: CourseDbHelper
    @Inject lateinit var classDbHelper: ClassDbHelper
    private var courseId: String? = null
    lateinit var binding: ActivityCourseBinding
    private val adapter: ClassAdapter = ClassAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course)
        binding.lifecycleOwner = this
        courseId = intent.getStringExtra("courseId")
    }

    override fun onResume() {
        super.onResume()
        handleGui()
    }

    private fun handleGui(){
        if(courseId == null){
            finish()
            return
        }
        val course = courseDbHelper.getCourseById(courseId!!)
        if(course == null){
            finish()
            return
        }
        populateCourseData(course)
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.edit.setOnClickListener {
            startActivity(Intent(this@CourseDetailActivity, AddCourseActivity::class.java).apply {
                putExtra("courseId", intent.getStringExtra("courseId"))
            })
        }
        binding.delete.setOnClickListener {
            courseDbHelper.deleteCourseById(courseId!!)
            classDbHelper.deleteClassByCourseId(courseId!!)
            finish()
        }

        binding.createClass.setOnClickListener {
            startActivity(Intent(this@CourseDetailActivity, AddClassActivity::class.java).apply {
                putExtra("courseId", courseId)
                putExtra("weekDay", course.dayOfWeek)
            })
        }

        adapter.delegate = object: ClassAdapter.ClassProtocol{
            override fun onEditPressed(classId: String) {
                startActivity(Intent(this@CourseDetailActivity, AddClassActivity::class.java).apply {
                    putExtra("courseId", courseId)
                    putExtra("classId", classId)
                    putExtra("weekDay", course.dayOfWeek)
                })
            }

            override fun onDeletePressed(classId: String) {
                classDbHelper.deleteByClassId(classId)
                adapter.remove(classId)
            }
        }

        binding.classesView.adapter = adapter

        showLoader()
        loadClasses()
    }

    private fun loadClasses(){
        val data = classDbHelper.getAllClassesByCourseId(courseId!!)
        adapter.clear()
        adapter.addAll(data)
        binding.noClass.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        hideLoader()
    }

    private fun populateCourseData(course: Course){
        binding.title.text = "${course.title} | ${course.duration} min | €${course.pricePerClass.toInt()}"

        binding.desc.text = course.description
        binding.desc.visibility = if (course.description.isEmpty()) View.GONE else ViewGroup.VISIBLE

        binding.capacity.text = course.capacity.toString()
        binding.type.text = course.typeOfClass
        binding.dateTime.text = "${course.dayOfWeek.substring(0, 3)} - ${course.timeOfDay}"

        when(course.typeOfClass){
            TypeOfClass.FLOW.name -> binding.exerciseImage.setImageResource(R.drawable.yoga_one)
            TypeOfClass.AERIAL.name -> binding.exerciseImage.setImageResource(R.drawable.yoga_two)
            TypeOfClass.FAMILY.name -> binding.exerciseImage.setImageResource(R.drawable.yoga_three)
            TypeOfClass.PRIVATE.name -> binding.exerciseImage.setImageResource(R.drawable.yoga_four)
            TypeOfClass.CORPORATE.name -> binding.exerciseImage.setImageResource(R.drawable.yoga_five)
            TypeOfClass.KIDS.name -> binding.exerciseImage.setImageResource(R.drawable.yoga_six)
        }
    }
}