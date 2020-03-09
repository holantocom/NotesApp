package com.example.rpodmp_lab_1


import android.app.AlarmManager
import android.os.Bundle
import android.widget.TextView
import android.view.View
import android.widget.Button
import java.util.*
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_timer.*
import java.text.SimpleDateFormat


class AddNotificationActivity : AppCompatActivity() {

    var button_date: Button? = null
    var button_time: Button? = null
    var textview_time: TextView? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance(TimeZone.getDefault())
    var title : String? = null
    var id : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_timer)

        val extras = intent.extras

        if (extras != null) {
            title = extras.getString("name")
            id = extras.getInt("ID")
        }

        // get the references from layout file
        textview_date = this.in_date
        textview_time = this.in_time
        button_date = this.btn_date
        button_time = this.btn_time

        button_time!!.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener {
                    timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                cal.set(Calendar.SECOND, 0)
                textview_time!!.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                textview_date!!.text = SimpleDateFormat("dd.MM.yyyy").format(cal.time)
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@AddNotificationActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
    }

    fun addEventNotification(view: View){

        //Toast.makeText(this, "Set correct time", Toast.LENGTH_SHORT).show()
        if(textview_date!!.text.isNullOrEmpty() || textview_time!!.text.isNullOrEmpty()){
            Toast.makeText(this, "Set time", Toast.LENGTH_SHORT).show()
            return
        }

        if( (cal.timeInMillis - System.currentTimeMillis()) < 0){
            Toast.makeText(this, "This time has already passed", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("TIME", SimpleDateFormat("HH:mm:ss").format(cal.timeInMillis))
        val notificationIntent = Intent(this, NotificationPublisher::class.java)
        notificationIntent.putExtra("notification", title)
        notificationIntent.putExtra("ID", id)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id ?: 0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
        finish()
    }


}