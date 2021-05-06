package com.example.knotsandcrosses

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.android.volley.*
import com.android.volley.toolbox.*
import com.example.knotsandcrosses.data.fromJson
import com.example.knotsandcrosses.data.game
import com.example.knotsandcrosses.data.toJson
import org.json.JSONObject

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
    var gameLiveData = MutableLiveData<game>()


    public fun startGame(context: Context, player: String) {
        val queue = Volley.newRequestQueue(context)
        val url = "https://generic-game-service.herokuapp.com/Game"
        lateinit var emptyGame: game
/*
        val jsonORequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                textView.text = "Response: %s".format(response.toString())
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )


 */
        val jsonObjectRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                emptyGame = fromJson(response)
                val emptyRow0 = mutableListOf<String>("0", "0", "0")
                val emptyRow1 = mutableListOf<String>("0", "0", "0")
                val emptyRow2 = mutableListOf<String>("0", "0", "0")
                val emptyList = mutableListOf(emptyRow0, emptyRow1, emptyRow2)
                emptyGame.state = emptyList
                gameLiveData.postValue(emptyGame)

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
            override fun getBodyContentType(): String {
                return "application/json"
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val params2 = HashMap<String, String>()
                params2.put("player", player)
                return JSONObject(params2 as Map<*, *>).toString().toByteArray()
            }
        };
        var header = jsonObjectRequest.getHeaders()
        var body = jsonObjectRequest.body.toString()
        queue.add(jsonObjectRequest)
    }
    fun getGame(): MutableLiveData<game> {
        return gameLiveData
    }

    public fun joinGame(context: Context, gameId: String, player: String) {

        lateinit var emptyGame: game
        val queue = Volley.newRequestQueue(context)
        val url = "https://generic-game-service.herokuapp.com/Game/" + gameId + "/join"
        val jsonObjectRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                emptyGame = fromJson(response)
                val emptyRow0 = mutableListOf<String>("0", "0", "0")
                val emptyRow1 = mutableListOf<String>("0", "0", "0")
                val emptyRow2 = mutableListOf<String>("0", "0", "0")
                val emptyList = mutableListOf(emptyRow0, emptyRow1, emptyRow2)
                emptyGame.state = emptyList
                gameLiveData.postValue(emptyGame)

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
            override fun getBodyContentType(): String {
                return "application/json"
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val params2 = HashMap<String, String>()
                params2.put("player", player)
                params2.put("gameId", gameId)
                return JSONObject(params2 as Map<*, *>).toString().toByteArray()
            }
        }
        queue.add(jsonObjectRequest)
    }

    public fun getGameState(context: Context) {

        lateinit var currentGame: game
        val queue = Volley.newRequestQueue(context)
        val url = "https://generic-game-service.herokuapp.com/Game/" + gameLiveData.value!!.gameId + "/poll"
        val jsonObjectRequest = object : StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val previousGameState = gameLiveData.value
                currentGame = fromJson(response)
                if (currentGame != previousGameState){
                    gameLiveData.postValue(currentGame)
                }


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
        }

        queue.add(jsonObjectRequest)
    }

    public fun postGameState(context: Context, gameId: String) {

        lateinit var currentGame: game
        val queue = Volley.newRequestQueue(context)
        val url = "https://generic-game-service.herokuapp.com/Game/" + gameId + "/update"
        val lookatjson = toJson(gameLiveData.value!!)
        val jsonObjectRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
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
            override fun getBodyContentType(): String {
                return "application/json"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return toJson(gameLiveData.value!!).toByteArray()
            }
        }
        queue.add(jsonObjectRequest)
    }
    public fun setGameState(rowIndex:Int, squareIndex:Int, value:String, context:Context){
        var currentState = gameLiveData.value
        if (currentState != null) {
            currentState.state[rowIndex][squareIndex] = value
            gameLiveData.postValue(currentState)
            postGameState(context, currentState.gameId)
        }
    }
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

