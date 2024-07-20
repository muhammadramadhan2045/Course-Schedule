package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity() ,TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        findViewById<ImageButton>(R.id.ib_start_time).setOnClickListener {
            showTimePickerStartDialog(it)
        }

        findViewById<ImageButton>(R.id.ib_end_time).setOnClickListener {
            showTimePickerEndDialog(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.action_insert ->{
                val courseName = findViewById<EditText>(R.id.et_course_name).text.toString()
                val day = findViewById<Spinner>(R.id.spinner_day).selectedItem.toString()
                val spinnerDay= getDayNumberByDayName(day)
                val startTime = findViewById<TextView>(R.id.tv_start_time).text.toString()
                val endTime = findViewById<TextView>(R.id.tv_end_time).text.toString()
                val lecturer = findViewById<EditText>(R.id.et_lecturer).text.toString()
                val note = findViewById<EditText>(R.id.et_note).text.toString()

                viewModel.insertCourse(courseName, spinnerDay, startTime, endTime, lecturer, note)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTimePickerStartDialog(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "startPicker")
    }

    private fun showTimePickerEndDialog(view: View) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "endPicker")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar= Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat=SimpleDateFormat("HH:mm", Locale.getDefault())

        if (tag == "startPicker") {
            findViewById<TextView>(R.id.tv_start_time).text = dateFormat.format(calendar.time)
        } else {
            findViewById<TextView>(R.id.tv_end_time).text = dateFormat.format(calendar.time)
        }

    }


    private fun getDayNumberByDayName(dayName: String): Int {
        val days = resources.getStringArray(R.array.day)
        return days.indexOf(dayName)
    }

}