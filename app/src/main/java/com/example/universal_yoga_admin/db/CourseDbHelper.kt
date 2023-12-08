package com.example.universal_yoga_admin.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.universal_yoga_admin.models.Course

class CourseDbHelper(
    context: Context,
    databaseName: String,
    databaseVersion: Int
) : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

    companion object {
        private const val DATABASE_VERSION = 1

        private const val TABLE_COURSES = "courses"

        private const val KEY_COURSE_ID = "id"
        private const val KEY_COURSE_TITLE = "title"
        private const val KEY_COURSE_DESCRIPTION = "description"
        private const val KEY_COURSE_IMAGE = "courseImage"
        private const val KEY_COURSE_DAY_OF_WEEK = "dayOfWeek"
        private const val KEY_COURSE_TIME_OF_DAY = "timeOfDay"
        private const val KEY_COURSE_CREATED_BY = "createdBy"
        private const val KEY_COURSE_CREATED_ON = "createdOn"
        private const val KEY_COURSE_UPDATED_BY = "updatedBy"
        private const val KEY_COURSE_UPDATED_ON = "updatedOn"
        private const val KEY_COURSE_CAPACITY = "courseCapacity"
        private const val KEY_COURSE_DURATION = "courseDuration"
        private const val KEY_COURSE_PRICE_PER_CLASS = "coursePricePerClass"
        private const val KEY_COURSE_TYPE_OF_CLASS = "courseTypeOfClass"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createCoursesTable = """
            CREATE TABLE $TABLE_COURSES (
                $KEY_COURSE_ID TEXT PRIMARY KEY,
                $KEY_COURSE_TITLE TEXT,
                $KEY_COURSE_DESCRIPTION TEXT,
                $KEY_COURSE_IMAGE TEXT,
                $KEY_COURSE_DAY_OF_WEEK TEXT,
                $KEY_COURSE_TIME_OF_DAY TEXT,
                $KEY_COURSE_CAPACITY INTEGER,
                $KEY_COURSE_DURATION REAL,
                $KEY_COURSE_PRICE_PER_CLASS REAL,
                $KEY_COURSE_TYPE_OF_CLASS TEXT,
                $KEY_COURSE_CREATED_BY TEXT,
                $KEY_COURSE_CREATED_ON TEXT,
                $KEY_COURSE_UPDATED_BY TEXT,
                $KEY_COURSE_UPDATED_ON TEXT
            )
        """.trimIndent()
        db.execSQL(createCoursesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COURSES")
        onCreate(db)
    }

    fun addCourse(course: Course) {
        val values = ContentValues().apply {
            put(KEY_COURSE_ID, course.id)
            put(KEY_COURSE_TITLE, course.title)
            put(KEY_COURSE_DESCRIPTION, course.description)
            put(KEY_COURSE_IMAGE, course.courseImage)
            put(KEY_COURSE_DAY_OF_WEEK, course.dayOfWeek)
            put(KEY_COURSE_TIME_OF_DAY, course.timeOfDay)
            put(KEY_COURSE_CAPACITY, course.capacity)
            put(KEY_COURSE_DURATION, course.duration)
            put(KEY_COURSE_PRICE_PER_CLASS, course.pricePerClass)
            put(KEY_COURSE_TYPE_OF_CLASS, course.typeOfClass)
            put(KEY_COURSE_CREATED_BY, course.createdBy)
            put(KEY_COURSE_CREATED_ON, course.createdOn)
            put(KEY_COURSE_UPDATED_BY, course.updatedBy)
            put(KEY_COURSE_UPDATED_ON, course.updatedOn)
        }

        writableDatabase.use { db ->
            db.insertOrThrow(TABLE_COURSES, null, values)
        }
    }

    fun getCourseById(courseId: String): Course? {
        readableDatabase.use { db ->
            val cursor = db.query(
                TABLE_COURSES,
                null,
                "$KEY_COURSE_ID = ?",
                arrayOf(courseId),
                null,
                null,
                null
            )
            cursor.use {
                if (it.moveToFirst()) {
                    return Course(
                        id = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_ID)),
                        title = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_TITLE)),
                        description = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_DESCRIPTION)),
                        courseImage = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_IMAGE)),
                        dayOfWeek = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_DAY_OF_WEEK)),
                        timeOfDay = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_TIME_OF_DAY)),
                        capacity = it.getInt(it.getColumnIndexOrThrow(KEY_COURSE_CAPACITY)),
                        duration = it.getDouble(it.getColumnIndexOrThrow(KEY_COURSE_DURATION)),
                        pricePerClass = it.getDouble(it.getColumnIndexOrThrow(KEY_COURSE_PRICE_PER_CLASS)),
                        typeOfClass = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_TYPE_OF_CLASS)),
                        createdBy = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_CREATED_BY)),
                        createdOn = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_CREATED_ON)),
                        updatedBy = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_UPDATED_BY)),
                        updatedOn = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_UPDATED_ON))
                    )
                }
            }
        }
        return null
    }

    fun getAllCourses(): List<Course> {
        val courses = mutableListOf<Course>()
        readableDatabase.use { db ->
            val cursor = db.rawQuery("SELECT * FROM $TABLE_COURSES", null)
            cursor.use {
                while (it.moveToNext()) {
                    courses.add(
                        Course(
                            id = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_ID)),
                            title = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_TITLE)),
                            description = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_DESCRIPTION)),
                            courseImage = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_IMAGE)),
                            dayOfWeek = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_DAY_OF_WEEK)),
                            timeOfDay = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_TIME_OF_DAY)),
                            capacity = it.getInt(it.getColumnIndexOrThrow(KEY_COURSE_CAPACITY)),
                            duration = it.getDouble(it.getColumnIndexOrThrow(KEY_COURSE_DURATION)),
                            pricePerClass = it.getDouble(it.getColumnIndexOrThrow(KEY_COURSE_PRICE_PER_CLASS)),
                            typeOfClass = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_TYPE_OF_CLASS)),
                            createdBy = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_CREATED_BY)),
                            createdOn = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_CREATED_ON)),
                            updatedBy = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_UPDATED_BY)),
                            updatedOn = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_UPDATED_ON))
                        )
                    )
                }
                return courses
            }
        }
        return listOf()
    }

    fun updateCourse(course: Course) {
        val values = ContentValues().apply {
            put(KEY_COURSE_TITLE, course.title)
            put(KEY_COURSE_DESCRIPTION, course.description)
            put(KEY_COURSE_IMAGE, course.courseImage)
            put(KEY_COURSE_DAY_OF_WEEK, course.dayOfWeek)
            put(KEY_COURSE_TIME_OF_DAY, course.timeOfDay)
            put(KEY_COURSE_CAPACITY, course.capacity)
            put(KEY_COURSE_DURATION, course.duration)
            put(KEY_COURSE_PRICE_PER_CLASS, course.pricePerClass)
            put(KEY_COURSE_TYPE_OF_CLASS, course.typeOfClass)
            put(KEY_COURSE_CREATED_BY, course.createdBy)
            put(KEY_COURSE_CREATED_ON, course.createdOn)
            put(KEY_COURSE_UPDATED_BY, course.updatedBy)
            put(KEY_COURSE_UPDATED_ON, course.updatedOn)
        }

        writableDatabase.use { db ->
            db.update(TABLE_COURSES, values, "$KEY_COURSE_ID = ?", arrayOf(course.id))
        }
    }

    fun deleteCourseById(courseId: String) {
        writableDatabase.use { db ->
            db.delete(TABLE_COURSES, "$KEY_COURSE_ID = ?", arrayOf(courseId))
        }
    }

}
