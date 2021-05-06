package com.example.knotsandcrosses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.knotsandcrosses.GAME_ID
import com.example.knotsandcrosses.data.game

class GamePlay : AppCompatActivity() {
    lateinit var player:String
    lateinit var timer:CountDownTimer
    var joinCode:String? = null
    val communications = Communications()
    var playerTurn: Boolean = true
    var playerValue: String? = null
    var triggeredByPost: Boolean = false

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
        for (row in listOfSquares){
            for (square in row){
                square.setOnClickListener{
                    onSquareClick(listOfSquares.indexOf(row),row.indexOf(square))
                }
            }
        }
        if (bundle != null) {
            joinCode = bundle.getString(GAME_ID)
            player = bundle.getString("player").toString()
        }
        if (joinCode == null){
            communications.startGame(this, player)
            playerValue = "X"
        } else {
            communications.joinGame(this, joinCode!!, player)
            serverpolling()
            playerTurn = false
            playerValue = "O"
        }
        val currentGame = communications.getGame()
        currentGame.observe(this, {
            it?.let{

                var winner = checkForVictory(it)
                if (winner != "no winner yet" && winner != "0"){
                    val toast = Toast.makeText(this, winner + " wins", Toast.LENGTH_LONG)
                    toast.show()
                }
                gameIdDisplay.text = "GameID: " + it.gameId

                for (row in listOfSquares){
                    for (square in row){
                        if (playerValue == null && it.state[listOfSquares.indexOf(row)][row.indexOf(square)] == "X"){
                            playerValue = "O"
                        }
                        when(it.state[listOfSquares.indexOf(row)][row.indexOf(square)]) {
                            "O" -> square.setImageDrawable(getResources().getDrawable(R.drawable.circle))
                            "X" -> square.setImageDrawable(getResources().getDrawable(R.drawable.cross))
                            else -> square.setImageDrawable(getResources().getDrawable(R.drawable.empty))
                        }
                    }
                }
                if (playerValue == null){
                    playerValue = "X"
                    playerTurn = false
                }
                if (triggeredByPost == false){
                    playerTurn = true
                } else {
                    triggeredByPost = false
                }
            }
        })
    }
    private fun onSquareClick(rowIndex: Int, squareIndex: Int) {
        if (playerTurn == true) {
            communications.setGameState(rowIndex, squareIndex, playerValue!!, this)
            playerTurn = false
            triggeredByPost = true
            serverpolling()
        }
    }
    private fun serverpolling(){
        val timeToCountDownInMs = 5000L
        val timeTicks = 1000L
        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                if(communications.gameLiveData.value?.gameId   != null){
                    getStateOfGame()
                }
                if (playerTurn == false){
                    this.start()
                }
            }
            override fun onTick(millisUntilFinished: Long) {
            }
        }
        timer.start()
    }

    private fun getStateOfGame(){
        communications.getGameState(this)
    }

    private fun checkForVictory(gametocheck: game):String{
        for(row in gametocheck.state) {
            if (row[0] == row[1] && row[1] == row[2] && row[0] != "0") {
                return row[0]
            }
        }
        for(i in 0..2) {
            if (gametocheck.state[0][i] == gametocheck.state[1][i] && gametocheck.state[1][i] == gametocheck.state[2][i] && gametocheck.state[0][i] != "0") {
                var winner = gametocheck.state[0][i]
                return gametocheck.state[0][i]
            }
        }
        if (gametocheck.state[0][0] == gametocheck.state[1][1] && gametocheck.state[1][1] == gametocheck.state[2][2] || gametocheck.state[0][2] == gametocheck.state[1][1] && gametocheck.state[1][1] == gametocheck.state[2][0]){
            return gametocheck.state[1][1]
        }
        return "no winner yet"
    }

}