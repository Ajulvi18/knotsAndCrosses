package com.example.knotsandcrosses

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

const val GAME_ID = "gameId"

class MainActivity : AppCompatActivity() {

    private lateinit var playerName: String
    private lateinit var enteredName: TextInputEditText
    private lateinit var createGameButton: Button
    private lateinit var joinGameButton: Button
    private lateinit var joinCodeEntry: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enter_name)
        val enterButton: Button = findViewById(R.id.enter_button)
        enteredName = findViewById(R.id.enter_name)
        enterButton.setOnClickListener{
            onEnterClick()
        }
    }

    private fun onEnterClick() {
        if (enteredName.text.isNullOrEmpty()) {
            val myToast = Toast.makeText(applicationContext,"Please enter a player name",Toast.LENGTH_SHORT)
            myToast.show()
            return
        } else {
            playerName = enteredName.text.toString()
            setContentView(R.layout.activity_main)
            joinGameButton = (findViewById(R.id.join_game_button))
            createGameButton = (findViewById(R.id.create_game_button))
            joinCodeEntry = (findViewById(R.id.enter_id))
            createGameButton.setOnClickListener{
                onCreateGameClick()
            }
            joinGameButton.setOnClickListener{
                onJoinGameClick()
            }

        }
    }

    private fun onCreateGameClick() {
        val intent = Intent(this, GamePlay()::class.java)
        intent.putExtra("player", playerName)
        startActivity(intent)
    }
    private fun onJoinGameClick() {
        if (joinCodeEntry.text.isNullOrEmpty()) {
            val myToast =
                Toast.makeText(applicationContext, "Please enter a join code", Toast.LENGTH_SHORT)
            myToast.show()
            return
        } else {
            val intent = Intent(this, GamePlay()::class.java)
            intent.putExtra("player", playerName)
            intent.putExtra(GAME_ID, joinCodeEntry.text.toString())
            startActivity(intent)
        }
    }
}