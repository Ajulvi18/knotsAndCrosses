package com.example.knotsandcrosses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class GamePlay : AppCompatActivity() {
    lateinit var player:String
    var joinCode:String? = null
    val communications = Communications()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
        val gameIdDisplay: TextView = findViewById(R.id.game_id_display)
        val square11: ImageButton = findViewById(R.id.rute11)
        val square12: ImageButton = findViewById(R.id.rute12)
        val square13: ImageButton = findViewById(R.id.rute13)
        val square21: ImageButton = findViewById(R.id.rute21)
        val square22: ImageButton = findViewById(R.id.rute22)
        val square23: ImageButton = findViewById(R.id.rute23)
        val square31: ImageButton = findViewById(R.id.rute31)
        val square32: ImageButton = findViewById(R.id.rute32)
        val square33: ImageButton = findViewById(R.id.rute33)
        var listOfSquares: MutableList<MutableList<ImageButton>> = mutableListOf(mutableListOf(square11, square12, square13), mutableListOf(square21, square22, square23), mutableListOf(square31, square32, square33))

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            joinCode = bundle.getString("joinCode")
            player = bundle.getString("player")!!
        }
        if (joinCode == null){
            communications.startGame(this, player)
        } else {
            communications.joinGame(this, joinCode!!, player)
        }
        val currentGame = communications.getGame()
        currentGame.observe(this, {
            it?.let{
                gameIdDisplay.text = "GameID: " + it.gameId
                for (row in listOfSquares){
                    for (square in row){
                        when(it.state[listOfSquares.indexOf(row)][row.indexOf(square)]) {
                            "O" -> square.setImageDrawable(getResources().getDrawable(R.drawable.circle))
                            "X" -> square.setImageDrawable(getResources().getDrawable(R.drawable.cross))
                            else -> square.setImageDrawable(getResources().getDrawable(R.drawable.empty))
                        }
                    }
                }
            }
        })
        for (row in listOfSquares){
            for (square in row){
                square.setOnClickListener{
                    onSquareClick(listOfSquares.indexOf(row),row.indexOf(square))
                }
            }
        }
    }
    private fun onSquareClick(rowIndex: Int, squareIndex: Int, Value:String="X") {
        communications.setGameState(rowIndex, squareIndex, Value, this)
    }
}