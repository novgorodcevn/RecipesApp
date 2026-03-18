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
import com.example.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private val threadPool = Executors.newFixedThreadPool(10)
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

        threadPool.execute {
            val client: OkHttpClient = OkHttpClient()
            val request: Request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()
            Log.i("!!!", "Выполняю запрос на потоке:${Thread.currentThread().name} ")

            client.newCall(request).execute().use { response ->
                val data = response.body.string()
                val categories = json.decodeFromString<List<Category>>(
                    data
                )
                categories.forEach {
                    Log.i("!!!", "Категори: ${it.id} ${it.title} ${it.description} ${it.imageUrl}")
                }
                val categoriesId: List<Int> = categories.map { it.id }
                //       Log.i("!!!", "categories: $categoriesId")
                //        categoriesId.forEach { id ->
                //           threadPool.execute {
                //               val request: Request = Request.Builder()
                //                    .url("https://recipes.androidsprint.ru/api/category/$id/recipes")
                //                    .build()
                //              try {
                //                   client.newCall(request).execute().use { response ->
                //      val data = response.body.string()
                //                      val recipes = json.decodeFromString<List<Recipe>>(
                //                           data
                //                       )
                //                      recipes.forEach {
                //                           Log.i("!!!", "Рецепт: ${it.title}")
                //                        }
                //                    }
                //                } catch (e: Exception) {
                //                   Log.e("!!!", "Ошибка при парсинге рецептов ID $id: ${e.message}", e)
                //               }
                //           }
                //       }
            }
        }
        with(binding) {
            btnCategories.setOnClickListener {
                findNavController(nav_host_fragment).navigate(R.id.categoriesListFragment)
            }
            btnFavorites.setOnClickListener {
                findNavController(nav_host_fragment).navigate(R.id.favoritesFragment)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }

}