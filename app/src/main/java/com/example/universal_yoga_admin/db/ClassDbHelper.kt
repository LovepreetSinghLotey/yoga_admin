package com.example.universal_yoga_admin.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.universal_yoga_admin.models.Class

class ClassDbHelper(
    context: Context,
    databaseName: String,
    databaseVersion: Int
) : SQLiteOpenHelper(context, databaseName, null, databaseVersion) {

    companion object {
        private const val TABLE_CLASSES = "classes"

        private const val KEY_CLASS_ID = "id"
        private const val KEY_COURSE_ID = "courseId"
        private const val KEY_DATE = "date"
        private const val KEY_TEACHER = "teacher"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createClassesTable = """
            CREATE TABLE $TABLE_CLASSES (
                $KEY_CLASS_ID TEXT PRIMARY KEY,
                $KEY_COURSE_ID TEXT,
                $KEY_DATE TEXT,
                $KEY_TEACHER TEXT
            )
        """.trimIndent()
        db.execSQL(createClassesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLASSES")
        onCreate(db)
    }

    fun addClass(clazz: Class) {
        val values = ContentValues().apply {
            put(KEY_CLASS_ID, clazz.id)
            put(KEY_COURSE_ID, clazz.courseId)
            put(KEY_DATE, clazz.date)
            put(KEY_TEACHER, clazz.teacher)
        }

        writableDatabase.use { db ->
            try {
                db.insertOrThrow(TABLE_CLASSES, null, values)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getClassById(classId: String): Class? {
        readableDatabase.use { db ->
            val cursor = db.query(
                TABLE_CLASSES,
                null,
                "$KEY_CLASS_ID = ?",
                arrayOf(classId),
                null,
                null,
                null
            )
            cursor.use {
                if (it.moveToFirst()) {
                    return Class(
                        id = it.getString(it.getColumnIndexOrThrow(KEY_CLASS_ID)),
                        courseId = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_ID)),
                        date = it.getString(it.getColumnIndexOrThrow(KEY_DATE)),
                        teacher = it.getString(it.getColumnIndexOrThrow(KEY_TEACHER)),
                    )
                }
            }
        }
        return null
    }

    fun getAllClassesByCourseId(courseId: String): List<Class> {
        val classes = mutableListOf<Class>()
        readableDatabase.use { db ->
            val cursor = db.query(
                TABLE_CLASSES,
                null,
                "$KEY_COURSE_ID = ?",
                arrayOf(courseId),
                null,
                null,
                null
            )
            cursor.use {
                while (it.moveToNext()) {
                    classes.add(
                        Class(
                            id = it.getString(it.getColumnIndexOrThrow(KEY_CLASS_ID)),
                            courseId = it.getString(it.getColumnIndexOrThrow(KEY_COURSE_ID)),
                            date = it.getString(it.getColumnIndexOrThrow(KEY_DATE)),
                            teacher = it.getString(it.getColumnIndexOrThrow(KEY_TEACHER)),
                        )
                    )
                }
            }
        }
        return classes
    }

    fun updateClass(clazz: Class) {
        val values = ContentValues().apply {
            put(KEY_COURSE_ID, clazz.courseId)
            put(KEY_DATE, clazz.date)
            put(KEY_TEACHER, clazz.teacher)
        }

        writableDatabase.use { db ->
            db.update(TABLE_CLASSES, values, "$KEY_CLASS_ID = ?", arrayOf(clazz.id))
        }
    }

    fun deleteClassByCourseId(courseId: String) {
        writableDatabase.use { db ->
            db.delete(TABLE_CLASSES, "$KEY_COURSE_ID = ?", arrayOf(courseId))
        }
    }

    fun deleteByClassId(classId: String) {
        writableDatabase.use { db ->
            db.delete(TABLE_CLASSES, "$KEY_CLASS_ID = ?", arrayOf(classId))
        }
    }


}
