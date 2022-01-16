package com.kotlin.calibrationtest

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.*
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.pow
import java.util.*
import kotlin.math.sqrt

/**
 * sources:
 *  - Location: https://thesimplycoder.com/396/getting-current-location-on-android-using-kotlin/
 */
class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var mSensor: Sensor
    private lateinit var geomagneticField: GeomagneticField

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var location: Location


    private lateinit var measurement: ArrayList<ArrayList<Float>>
    private lateinit var calibration: ArrayList<ArrayList<Float>>
    private lateinit var sum: ArrayList<Float>

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private var geoFieldMag = 0.0f
//    private var mag = 0.0f
//    private var avg_x = 0.0f
//    private var avg_y = 0.0f
//    private var avg_z = 0.0f
    private var flag_cal1 = false
    private var flag_cal2 = false
    private var flag_cal3 = false

    private var counter = 0

    private var time = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init the 2D arrayList  and the time.
        measurement = arrayListOf(arrayListOf(), arrayListOf(), arrayListOf())
        calibration = arrayListOf(arrayListOf(), arrayListOf(), arrayListOf())
        sum = arrayListOf()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.let {
            this.mSensor = it
        }

        //set time
        time = System.currentTimeMillis()

        // CLickListeners
        btn_location.setOnClickListener {
            getCurrentLocation()
        }
        btn_cali.setOnClickListener {
            activateCal()
        }

        btn_val1.setOnClickListener {
            flag_cal1 = true
        }
        btn_val2.setOnClickListener {
            flag_cal2 = true
        }
        btn_val3.setOnClickListener {
            flag_cal3 = true
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event?.sensor?.type) {
            Sensor.TYPE_MAGNETIC_FIELD -> {
                if ((System.currentTimeMillis() - time) > 500) {

                    val avg_x = sum[0] / measurement[0].size
                    val avg_y = sum[1] / measurement[1].size
                    val avg_z = sum[2] / measurement[2].size

                    tv_avgX.text = avg_x.toString()
                    tv_avgY.text = avg_y.toString()
                    tv_avgZ.text = avg_z.toString()

                    val pow_x = pow(avg_x.toDouble(), 2.0).toFloat()
                    val pow_y = pow(avg_y.toDouble(), 2.0).toFloat()
                    val pow_z = pow(avg_z.toDouble(), 2.0).toFloat()

                    val mag = sqrt(pow_x + pow_y + pow_z)
                    val xy = sqrt(pow_x + pow_y)

                    tv_mag.text = mag.toString()
                    tv_xy.text = xy.toString()

                    resetValues()

                    if (flag_cal1) {
                        if (counter <= 10) {
                            deactivateCal()

                            calibration[0].add(avg_x)
                            calibration[1].add(avg_y)
                            calibration[2].add(avg_z)

                            counter++

                        } else {
                            var x = 0.0f
                            var y = 0.0f
                            var z = 0.0f

                            for ((index, arr) in calibration.withIndex()) {
                                when (index) {
                                    0 -> {
                                        for (item in arr) {
                                            x += item
                                        }
                                    }
                                    1 -> {
                                        for (item in arr) {
                                            y += item
                                        }
                                    }
                                    2 -> {
                                        for (item in arr) {
                                            z += item
                                        }
                                    }
                                }
                            }
                            x = x / calibration[0].size
                            y = y / calibration[1].size
                            z = z / calibration[2].size

                            tv_val1.text = "x= $x \ny= $y \nz= $z"

                            calibration[0].clear()
                            calibration[1].clear()
                            calibration[2].clear()
                            flag_cal1 = false
                            counter = 0
                            activateCal()
                        }

                    } else if (flag_cal2) {
                        if (counter <= 10) {
                            deactivateCal()

                            calibration[0].add(avg_x)
                            calibration[1].add(avg_y)
                            calibration[2].add(avg_z)

                            counter++

                        } else {
                            var x = 0.0f
                            var y = 0.0f
                            var z = 0.0f

                            for ((index, arr) in calibration.withIndex()) {
                                when (index) {
                                    0 -> {
                                        for (item in arr) {
                                            x += item
                                        }
                                    }
                                    1 -> {
                                        for (item in arr) {
                                            y += item
                                        }
                                    }
                                    2 -> {
                                        for (item in arr) {
                                            z += item
                                        }
                                    }
                                }
                            }
                            x = x / calibration[0].size
                            y = y / calibration[1].size
                            z = z / calibration[2].size

                            tv_val2.text = "x= $x \ny= $y \nz= $z"

                            calibration[0].clear()
                            calibration[1].clear()
                            calibration[2].clear()
                            flag_cal2 = false
                            counter = 0;
                            activateCal()
                        }

                    } else if (flag_cal3) {
                        if (counter <= 10) {
                            deactivateCal()

                            calibration[0].add(avg_x)
                            calibration[1].add(avg_y)
                            calibration[2].add(avg_z)

                            counter++

                        } else {
                            var x = 0.0f
                            var y = 0.0f
                            var z = 0.0f

                            for ((index, arr) in calibration.withIndex()) {
                                when (index) {
                                    0 -> {
                                        for (item in arr) {
                                            x += item
                                        }
                                    }
                                    1 -> {
                                        for (item in arr) {
                                            y += item
                                        }
                                    }
                                    2 -> {
                                        for (item in arr) {
                                            z += item
                                        }
                                    }
                                }
                            }
                            x = x / calibration[0].size
                            y = y / calibration[1].size
                            z = z / calibration[2].size

                            tv_val3.text = "x= $x \ny= $y \nz= $z"

                            calibration[0].clear()
                            calibration[1].clear()
                            calibration[2].clear()
                            flag_cal3 = false
                            counter = 0
                            activateCal()
                        }

                    }


                    time = System.currentTimeMillis()

                } else {
                    measurement[0].add(event.values[0])
                    sum += event.values[0]
                    measurement[1].add(event.values[1])
                    sum += event.values[1]
                    measurement[2].add(event.values[2])
                    sum += event.values[2]
                }
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                this.location = location
                tv_latitude.text = location.latitude.toString()
                tv_longitude.text = location.longitude.toString()

                geomagneticField = GeomagneticField(
                    location.latitude.toFloat(),
                    location.longitude.toFloat(),
                    location.altitude.toFloat(),
                    System.currentTimeMillis()
                )

                val x = geomagneticField.x
                val y = geomagneticField.y
                val z = geomagneticField.z
                geoFieldMag = sqrt(x + y + z)
                tv_geoMField.text = geoFieldMag.toString()
                btn_cali.setEnabled(true)

            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "Failed on getting current location",
                    Toast.LENGTH_SHORT
                ).show()
                btn_cali.setEnabled(false)
            }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(
                        this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun activateCal() {
        btn_val1.setEnabled(true)
        btn_val2.setEnabled(true)
        btn_val3.setEnabled(true)
    }

    private fun deactivateCal() {
        btn_val1.setEnabled(false)
        btn_val2.setEnabled(false)
        btn_val3.setEnabled(false)
    }

    private fun resetValues() {
        measurement[0].clear()
        measurement[1].clear()
        measurement[2].clear()
        sum.clear()
    }
}