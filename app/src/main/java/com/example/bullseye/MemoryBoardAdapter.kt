package com.example.bullseye

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bullseye.models.BoardSize
import com.example.bullseye.models.MemoryCard
import com.squareup.picasso.Picasso

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener: CardClickListener
) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object{
        private const val  MARGIN_SIZE = 10
        private const val TAG = "MemoryBoardAdapter"
    }

    interface  CardClickListener{
        fun onCardClicked(position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var cardWidth = parent.width/ boardSize.getWidth() - (2 * MARGIN_SIZE)
        var cardHeight = parent.height/ boardSize.getHeight() - (2 * MARGIN_SIZE)
        var cardSideLength = kotlin.math.min(cardWidth, cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        val layoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }

    override fun getItemCount() = boardSize.numCards

    override fun onBindViewHolder(holder: MemoryBoardAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        fun bind(position: Int) {
            val card = cards[position]
            if (card.isFaceUp){
                if (card.imageUrl != null){
                    Picasso.get().load(card.imageUrl).into(imageButton)
                }else{
                    imageButton.setImageResource(card.identifier)

                }
            }else{
                imageButton.setImageResource(R.drawable.ic_launcher_background )
            }


            imageButton.alpha = if (card.isMatched) .4f else 1.0f

            val colorStateList = if (card.isMatched) ContextCompat.getColorStateList(context, R.color.color_gray) else null

             ViewCompat.setBackgroundTintList(imageButton, colorStateList)

            imageButton.setOnClickListener{
                Log.i(TAG,"Clicked on position $position" )
                cardClickListener.onCardClicked(position)
            }
        }
    }



    }

