package com.example.recipesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.recipesapp.R.id.nav_host_fragment
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val json = Json {
            ignoreUnknownKeys = true
        }

        Log.i("!!!", "Метод onCreate() выполняется на потоке:${Thread.currentThread().name} ")

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            Log.i("!!!", "Выполняю запрос на потоке:${Thread.currentThread().name} ")
            val data = connection.inputStream.bufferedReader().readText()
            val categories = json.decodeFromString<List<Category>>(
                data
            )
            categories.forEach {
                Log.i("!!!", "Категория: ${it.id} ${it.title} ${it.description} ${it.imageUrl}")
            }
            connection.disconnect()
        }
        thread.start()

        with(binding) {
            btnCategories.setOnClickListener {
                findNavController(nav_host_fragment).navigate(R.id.categoriesListFragment)
            }
            btnFavorites.setOnClickListener {
                findNavController(nav_host_fragment).navigate(R.id.favoritesFragment)
            }
        }
    }
}