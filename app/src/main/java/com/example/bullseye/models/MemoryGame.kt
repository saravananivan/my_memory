package com.example.bullseye.models

import com.example.bullseye.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, private val customImages: List<String>?){
    val cards : List<MemoryCard>
    private  var indexOfSingleSelectedCard : Int? = null
    var numPairsFound = 0
    private  var numCardFlips = 0



    init{
        if (customImages == null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it) }
        }else{
            val randomizedImages = (customImages + customImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it.hashCode(), it) }
        }
    }


    fun flipCard(position: Int): Boolean {
        numCardFlips++
        var foundMatch = false
        val card = cards[position]
        if (indexOfSingleSelectedCard == null){
            restoreCards()
            indexOfSingleSelectedCard = position
        }else{
           foundMatch =  checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
         return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int) :Boolean {
        if (cards[position1].identifier != cards[position2].identifier ){
            return false
        }

        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards){
            if(!card.isMatched){
                card.isFaceUp = false
            }

        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceup(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips/2
    }
}