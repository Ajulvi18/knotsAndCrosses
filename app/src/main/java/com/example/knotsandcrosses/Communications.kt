package com.example.knotsandcrosses

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.android.volley.*
import com.android.volley.toolbox.*
import com.example.knotsandcrosses.data.fromJson
import com.example.knotsandcrosses.data.game
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/*
class customRequest<T>(
    url: String,
    private val headers: MutableMap<String, String>?,
    private val listener: Response.Listener<T>,
    errorListener: Response.ErrorListener
) : Request<T>(Method.GET, url, errorListener) {
    private val gson = Gson()


    override fun getHeaders(): MutableMap<String, String> = headers ?: super.getHeaders()

    override fun deliverResponse(response: T) = listener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Response.success(
                gson.fromJson(json, String),
                HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }
}
*/
class Communications(){

    private val initialGameList = mutableListOf<game>()
    var gameList = MutableLiveData<game>()

    public fun startGame(context: Context) {
        val queue = Volley.newRequestQueue(context)
        val url = "https://generic-game-service.herokuapp.com/Game"
        lateinit var emptyGame: game

        val jsonObjectRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                emptyGame = fromJson(response)
                emptyGame.players = mutableListOf("player1")
                val emptyRow = mutableListOf<String>("0", "0", "0")
                val emptyList = mutableListOf(emptyRow, emptyRow, emptyRow)
                emptyGame.state = emptyList
                gameList.postValue(emptyGame)

            },
            Response.ErrorListener { response ->
                val toast = Toast.makeText(context, response.toString(), Toast.LENGTH_LONG)
                toast.show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Game-Service-Key"] = "gjF9ebA3Ix"
                headers["Content-Type"] = "application/json"
                return headers
            }
            /*
            override fun getBodyContentType(): String {
                return "application/json"
            }
            */

            /*
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val params2 = HashMap<String, String>()
                params2.put("player", "hello")
                params2.put("state", "[null]")
                return JSONObject(params2 as Map<*, *>).toString().toByteArray()
            }
            */
        };
        var header = jsonObjectRequest.getHeaders()
        var body = jsonObjectRequest.body
        queue.add(jsonObjectRequest)
    }
    fun getGameLists(): MutableLiveData<game> {
        return gameList
    }

    public fun joinGame(context: Context) {
        val queue = Volley.newRequestQueue(context)
        val url = "https://generic-game-service.herokuapp.com/Game"
    }
    /*
    val emptyRow = mutableListOf<String>("0", "0", "0")
    val emptyList = mutableListOf(emptyRow, emptyRow, emptyRow)
    var emptyGame = game(mutableListOf("player1"), "null", emptyList)
    */
}
/*
class GsonRequest<T>(
    url: String,
    private val headers: MutableMap<String, String>?,
    private val listener: Response.Listener<T>,
    errorListener: Response.ErrorListener
) : Request<T>(Method.GET, url, errorListener) {
    private val gson = Gson()


    override fun getHeaders(): MutableMap<String, String> = headers ?: super.getHeaders()

    override fun deliverResponse(response: T) = listener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Response.success(
                gson.fromJson(json, ),
                HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }
}
*/

/*
fun requestWithSomeHttpHeaders() {
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = "http://www.somewebsite.com";
    StringRequest getRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                // response
                Log.d("Response", response);
            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d("ERROR","error => "+error.toString());
            }
        }
    ) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("User-Agent", "Nintendo Gameboy");
        params.put("Accept-Language", "fr");

        return params;
    }
    };
    queue.add(getRequest);

*/

