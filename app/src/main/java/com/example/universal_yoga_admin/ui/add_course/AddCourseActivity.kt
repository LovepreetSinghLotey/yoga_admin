package com.example.universal_yoga_admin.ui.add_course

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.universal_yoga_admin.R
import com.example.universal_yoga_admin.databinding.ActivityAddCourseBinding
import com.example.universal_yoga_admin.db.CourseDbHelper
import com.example.universal_yoga_admin.extensions.showSnackbar
import com.example.universal_yoga_admin.models.Course
import com.example.universal_yoga_admin.models.TypeOfClass
import com.example.universal_yoga_admin.preferences.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject

enum class DayOfWeek{
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

@AndroidEntryPoint
class AddCourseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCourseBinding
    var chosenType: String = TypeOfClass.FLOW.name
    var day: String = DayOfWeek.MONDAY.name
    var courseId: String? = null
    @Inject lateinit var courseDbHelper: CourseDbHelper
    @Inject lateinit var preferenceUtil: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_course)
        binding.lifecycleOwner = this
        courseId = intent.getStringExtra("courseId")
        handleGui()
    }

    private fun handleGui() {
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.createCourse.setOnClickListener{
            createCourse(
                binding.title.text.toString(),
                binding.desc.text.toString(),
                "${binding.hour.text.toString()}:${binding.minute.text.toString()}",
                binding.capacity.text.toString(),
                binding.duration.text.toString(),
                binding.price.text.toString()
            )
        }

        handleSpinner()
        handleDaySpinner()
        prefillData()
    }

    private fun handleSpinner(){
        binding.typeOfCourse.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                chosenType = TypeOfClass.values()[position].name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            TypeOfClass.values()
        )
        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.typeOfCourse.adapter = ad
    }

    private fun handleDaySpinner(){
        binding.daySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                day = DayOfWeek.values()[position].name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            DayOfWeek.values()
        )
        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.daySpinner.adapter = ad
    }

    private fun prefillData(){
        if(courseId == null){
            return
        }
        val course = courseDbHelper.getCourseById(courseId!!) ?: return
        binding.createCourseText.text = "Update Course"
        binding.title.setText(course.title)
        binding.desc.setText(course.description)
        binding.typeOfCourse.setSelection(TypeOfClass.valueOf(course.typeOfClass.uppercase()).ordinal)
        binding.daySpinner.setSelection(DayOfWeek.valueOf(course.dayOfWeek.uppercase()).ordinal)
        binding.hour.setText(course.timeOfDay.split(":")[0])
        binding.minute.setText(course.timeOfDay.split(":")[1])
        binding.capacity.setText(course.capacity.toString())
        binding.duration.setText(course.duration.toString())
        binding.price.setText(course.pricePerClass.toString())
    }

    private fun createCourse(title: String, desc: String, time: String,
                             capacity: String, duration: String,
                             price: String){

        if(title.isEmpty() || time.isEmpty() ||
            capacity.isEmpty() || duration.isEmpty() || price.isEmpty()){
            showSnackbar("Please fill all the fields")
            return
        }

        if(courseId == null){
            // create new
            courseDbHelper.addCourse(Course(
                Date().time.toString(), title, desc, chosenType, day, time, capacity.toInt(),
                duration.toDouble(), price.toDouble(), chosenType, listOf(),
                preferenceUtil.getUserId(), Date().toString(), null, null
            ))
            onBackPressedDispatcher.onBackPressed()
            return
        }

        //update existing
        val course = courseDbHelper.getCourseById(courseId!!) ?: return
        courseDbHelper.updateCourse(Course(
            courseId!!, title, desc, chosenType, day, time, capacity.toInt(),
            duration.toDouble(), price.toDouble(), chosenType, listOf(),
            course.createdBy, course.createdOn,
            preferenceUtil.getUserId(), Date().toString()
        ))
        onBackPressedDispatcher.onBackPressed()
    }
}