package com.example.tracker.presentation.sealed.fragmentHome

import com.example.tracker.domain.entities.Card

class LoadCards(val cards: List<Card>) : State()