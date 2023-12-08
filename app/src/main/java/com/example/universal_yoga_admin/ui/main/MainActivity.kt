package com.example.universal_yoga_admin.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.universal_yoga_admin.R
import com.example.universal_yoga_admin.databinding.ActivityMainBinding
import com.example.universal_yoga_admin.db.ClassDbHelper
import com.example.universal_yoga_admin.db.CourseDbHelper
import com.example.universal_yoga_admin.extensions.hideLoader
import com.example.universal_yoga_admin.extensions.showLoader
import com.example.universal_yoga_admin.extensions.showSnackbar
import com.example.universal_yoga_admin.models.Class
import com.example.universal_yoga_admin.models.Course
import com.example.universal_yoga_admin.models.RequestPayload
import com.example.universal_yoga_admin.models.ResponsePayload
import com.example.universal_yoga_admin.preferences.PreferenceUtil
import com.example.universal_yoga_admin.ui.add_course.AddCourseActivity
import com.example.universal_yoga_admin.ui.course.CourseDetailActivity
import com.example.universal_yoga_admin.ui.login.LoginActivity
import com.example.universal_yoga_admin.ui.main.adapter.CourseAdapter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter: CourseAdapter = CourseAdapter()
    @Inject lateinit var courseDbHelper: CourseDbHelper
    @Inject lateinit var classDbHelper: ClassDbHelper
    @Inject lateinit var preferenceUtil: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
    }

    override fun onResume() {
        super.onResume()
        handleGui()
    }

    private fun handleGui(){
        adapter.delegate = object: CourseAdapter.CoursesProtocol{
            override fun onCoursePressed(course: Course) {
                startActivity(Intent(
                    this@MainActivity,
                    CourseDetailActivity::class.java).apply {
                    putExtra("courseId", course.id)
                })
            }
        }

        binding.coursesView.adapter = adapter
        
        binding.createCourse.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddCourseActivity::class.java))
        }

        binding.logout.setOnClickListener {
            preferenceUtil.clearAllPrefs()
            finish()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        binding.swipeRefresh.setOnRefreshListener {
            adapter.clear()
            binding.swipeRefresh.isRefreshing = true
            loadCourses()
        }

        binding.syncData.setOnClickListener {
            syncDataWithServer()
        }

        showLoader()
        loadCourses()
    }

    private fun loadCourses(){
        val data = courseDbHelper.getAllCourses()
        adapter.clear()
        adapter.addAll(data)
        binding.noCourse.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        binding.swipeRefresh.isRefreshing = false
        hideLoader()
    }

    private fun syncDataWithServer(){
        showLoader()
        val courses = mutableListOf<Course>()
        val courseData = courseDbHelper.getAllCourses()
        courseData.forEach {
            val classes = mutableListOf<Class>()
            classDbHelper.getAllClassesByCourseId(it.id).forEach { classData ->
                classes.add(classData)
            }
            if(classes.isNotEmpty())
                courses.add(it.copy(classList = classes))
        }
        val request = RequestPayload(preferenceUtil.getUserId(), courses)
        Log.i("MainActivity", "Request: $request")

        Thread {
            try {
                // Convert data to JSON using Gson
                val gson = Gson()
                val jsonData = gson.toJson(request)

                // Create request
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = jsonData.toRequestBody(mediaType)
                val request = Request.Builder()
                    .url("https://yoga-class-p6wx.onrender.com/api/user/course") // Replace with your server endpoint
                    .post(requestBody)
                    .build()

                // Execute request
                val client = OkHttpClient()
                val response = client.newCall(request).execute()
                runOnUiThread {
                    hideLoader()
                }
                // Handle response
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val gson = Gson()
                    val responsePayload = gson.fromJson(responseBody, ResponsePayload::class.java)
                    Log.i("MainActivity", "Response: $responsePayload")
                    runOnUiThread {
                        showSnackbar("Data synced successfully :" +
                                "\nmessage : ${responsePayload.message}" +
                                "\ncourses: ${responsePayload.courses}" +
                                "\nuserId: ${responsePayload.userId}" +
                                "\ncode: ${responsePayload.uploadResponseCode}" +
                                "\nnumber: ${responsePayload.number}")
                    }
                } else {
                    Log.i("MainActivity", "Error-response: ${response.body.toString()}")
                    runOnUiThread {
                        showSnackbar("Something went wrong, please try again later")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                hideLoader()
            }
        }.start()
    }
}