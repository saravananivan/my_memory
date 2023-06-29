package com.example.bullseye

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.example.bullseye.models.BoardSize
import com.example.bullseye.models.MemoryGame
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var clRoot: ConstraintLayout
    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame
    private lateinit var rvBoard :RecyclerView
    private lateinit var tvNumMoves : TextView
    private lateinit var tvNumPairs :TextView

    private  var boardSize = BoardSize.EASY

    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
       tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

       setupBoard()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        //return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when (item.itemId){
           R.id.mi_refresh -> {
               if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {
                   showAlertDialog("Quit your current game!", null,  View.OnClickListener {
                       setupBoard()
                   })
               } else {
                   setupBoard()
               }
           }
       }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this )
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK"){ _,_ -> positiveClickListener.onClick(null)

            }.show()
    }

    private fun setupBoard() {
        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.progress_none))

        memoryGame = MemoryGame(boardSize)

        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object: MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }
        })

        rvBoard.adapter = adapter
        rvBoard.layoutManager = GridLayoutManager(this,  boardSize.getWidth())
    }



   private fun updateGameWithFlip(position: Int){
       //Error checking
       if (memoryGame.haveWonGame()){
           Snackbar.make(clRoot, "You already have won!", Snackbar.LENGTH_LONG).show()
          return
       }

       if (memoryGame.isCardFaceup(position)){
           Snackbar.make(clRoot, "Invalid move!", Snackbar.LENGTH_SHORT).show()
           return
       }
       if (memoryGame.flipCard(position)){
           Log.i(TAG, "Found a match! Num pairs found: ${memoryGame.numPairsFound}")
           val color = ArgbEvaluator().evaluate(memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(), ContextCompat.getColor(this, R.color.progress_none), ContextCompat.getColor(this, R.color.progress_full)) as Int
           tvNumPairs.setTextColor(color)
           tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
           if (memoryGame.haveWonGame()){
               Snackbar.make(clRoot, "You won congratulation!", Snackbar.LENGTH_LONG).show()
           }
       }
       tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
      adapter.notifyDataSetChanged()
   }
}