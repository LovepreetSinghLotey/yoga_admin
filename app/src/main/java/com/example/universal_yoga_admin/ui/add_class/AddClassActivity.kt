package com.example.universal_yoga_admin.ui.add_class

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.universal_yoga_admin.R
import com.example.universal_yoga_admin.databinding.ActivityAddClassBinding
import com.example.universal_yoga_admin.databinding.ActivityAddCourseBinding
import com.example.universal_yoga_admin.db.ClassDbHelper
import com.example.universal_yoga_admin.db.CourseDbHelper
import com.example.universal_yoga_admin.extensions.showSnackbar
import com.example.universal_yoga_admin.models.Class
import com.example.universal_yoga_admin.models.Course
import com.example.universal_yoga_admin.models.TypeOfClass
import com.example.universal_yoga_admin.preferences.PreferenceUtil
import com.example.universal_yoga_admin.ui.add_course.DayOfWeek
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AddClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddClassBinding
    private var courseId: String? = null
    private var classId: String? = null
    private var weekDay: String? = null
    @Inject lateinit var classDbHelper: ClassDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_class)
        binding.lifecycleOwner = this
        handleGui()
    }

    private fun handleGui(){
        courseId = intent.getStringExtra("courseId")
        classId = intent.getStringExtra("classId")
        weekDay = intent.getStringExtra("weekDay")

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.date.setOnClickListener {
            pickDate()
        }

        binding.createClass.setOnClickListener {
            createClass(
                binding.teacher.text.toString(),
                binding.date.text.toString()
            )
        }

        prefillData()
    }

    private fun prefillData(){
        if(courseId != null && classId != null){
            val classModel = classDbHelper.getClassById(classId!!)
            binding.teacher.setText(classModel?.teacher)
            binding.date.setText(classModel?.date)
            binding.createClassText.text = "Update class"
        }
    }

    private fun createClass(teacher: String, date: String){

        if(teacher.isEmpty() || date.isEmpty()){
            showSnackbar("Please fill all the fields")
            return
        }

        if(classId == null){
            // create new
            classDbHelper.addClass(
                Class(Date().time.toString(), courseId!!, date, teacher)
            )
            onBackPressedDispatcher.onBackPressed()
            return
        }

        //update existing
        classDbHelper.updateClass(
            Class(classId!!, courseId!!, date, teacher)
        )
        onBackPressedDispatcher.onBackPressed()
    }

    private fun pickDate(){
        binding.date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val desiredDayOfWeek = when(weekDay?.lowercase()){
                "monday" -> Calendar.MONDAY
                "tuesday" -> Calendar.TUESDAY
                "wednesday" -> Calendar.WEDNESDAY
                "thursday" -> Calendar.THURSDAY
                "friday" -> Calendar.FRIDAY
                "saturday" -> Calendar.SATURDAY
                "sunday" -> Calendar.SUNDAY
                else -> Calendar.MONDAY
            }

            val datePickerDialog = DatePickerDialog(this@AddClassActivity, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)

                if (selectedDate.get(Calendar.DAY_OF_WEEK) == desiredDayOfWeek) {
                    // Format the date as needed and set it
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    binding.date.setText(dateFormat.format(selectedDate.time))
                } else {
                    showSnackbar("Please select a $weekDay")
                }
            }, year, month, day)

            datePickerDialog.show()
        }
    }
}