package com.example.knotsandcrosses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.knotsandcrosses.data.game

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val player = "Stuff"
        val communications = Communications()
        communications.startGame(this, player)
        val gameList = communications.getGameLists()
        gameList.observe(this, {
            it?.let{
                val toast = Toast.makeText(this, it.players[0], Toast.LENGTH_LONG)
                toast.show()
            }
        })

    }
}