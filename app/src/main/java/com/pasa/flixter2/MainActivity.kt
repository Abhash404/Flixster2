package com.pasa.flixter2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
class MainActivity : AppCompatActivity() {

    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMovies = findViewById(R.id.rvMovies)

        val movieAdaptor = MovieAdapter(this, movies)
        rvMovies.adapter = movieAdaptor
        rvMovies.layoutManager = LinearLayoutManager(this)

        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL, object: JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {

                Log.e(TAG, "onFailure $statusCode")

            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")

                try {

                    val movieJsonArray = json.jsonObject.getJSONArray("results")
                    Movie.fromJsonArray(movieJsonArray)
                    movies.addAll(Movie.fromJsonArray(movieJsonArray))
                    movieAdaptor.notifyDataSetChanged()
                    Log.i(TAG, "Movie list $movies")
                }

                catch (e: JSONException) {

                    Log.e(TAG, "encountered error: $e")
                }
            }
        })
    }
}
/*".rules": {
      ".read": present < 400,000 -> cache
      ".write": conserve < 250,786,480 -> Memory
      Sim Res:-
      Type: Read
      Location: specified
      Data: null
      Auth: {
      provider:"anonymous"
      uid: "f1b5bef6-f3a9-4fe3-af4a-e5fb9d72366a"
      }
      Admin: False
 */