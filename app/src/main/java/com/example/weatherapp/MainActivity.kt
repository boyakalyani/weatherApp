package com.example.weatherapp

import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.w3c.dom.Text
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val lat=intent.getStringExtra("lat")
        val long=intent.getStringExtra("long")

window.statusBarColor=Color.parseColor("#1383c3")
        Toast.makeText(this, "$lat $long", Toast.LENGTH_SHORT).show()
        getJsonData(lat,long)
    }

    private fun getJsonData(lat: String?, long: String?) {
            val queue = Volley.newRequestQueue(this)
            val API_KEY="47abf6e95124e501347f76927e7b1b24"
            val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"
//        val url="https://themealdb.com/api/json/v1/1/categories.php"
// Request a string response from the provided URL.
            val jsonRequest = JsonObjectRequest(
                Request.Method.GET, url,null,
                { response ->
                    Log.d("tag",response.toString())
                    setValues(response)

                },
                {
                    Toast.makeText(this,"Error",Toast.LENGTH_LONG).show() })
            queue.add(jsonRequest)
        }
//    val speed=findViewById<TextView>(R.id.speed_id)
//    val cityname=findViewById<TextView>(R.id.city_id)
//    val weather=findViewById<TextView>(R.id.weather_id)
//    val num1=findViewById<TextView>(R.id.num1_id)
//    val num2=findViewById<TextView>(R.id.num2_id)
    private fun setValues(response: JSONObject?) {
    val cityname=findViewById<TextView>(R.id.city_id)
        val temp=findViewById<TextView>(R.id.temp_id)
val maxmin=findViewById<TextView>(R.id.maxMin_id)
    val coordinates=findViewById<TextView>(R.id.coord_id)
        val weather=findViewById<TextView>(R.id.weather_id)
    val pressure=findViewById<TextView>(R.id.prs_id)
    val windD=findViewById<TextView>(R.id.wspD_id)
    val humidut=findViewById<TextView>(R.id.hum_id)

        cityname.text=response?.getString("name")
        var lat= response?.getJSONObject("coord")?.getString("lat")
        var long= response?.getJSONObject("coord")?.getString("lon")
        coordinates.text="${lat}  ,  ${long}"
        weather.text= response?.getJSONArray("weather")?.getJSONObject(0)?.getString("main")
        var temporary=response?.getJSONObject("main")?.getString("temp")
        temporary=((((temporary)!!.toFloat()-273.15)).toInt()).toString()
        temp.text="${temporary}°C"
        var mintemp=response?.getJSONObject("main")?.getString("temp_min")
        mintemp=((kotlin.math.ceil((mintemp)!!.toFloat()-273.15)).toInt()).toString()
        maxmin.text="${mintemp}°C"
        var maxtemp=response?.getJSONObject("main")?.getString("temp_max")
        maxtemp=((kotlin.math.ceil((mintemp).toFloat()-273.15)).toInt()).toString()
        maxmin.text="${maxtemp}°C"
        pressure.text=response?.getJSONObject("main")?.getString("pressure")
        humidut.text=response?.getJSONObject("main")?.getString("humidity")+"%"
        windD.text=response?.getJSONObject("wind")?.getString("speed")
        windD.text="Degree : "+response?.getJSONObject("wind")?.getString("deg")+"°"
//    gust.text="Bust:"+response.getJSONObject("wind").getString("gust")
    }
}