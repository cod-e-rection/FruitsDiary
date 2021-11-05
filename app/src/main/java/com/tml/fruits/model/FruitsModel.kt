package com.tml.fruits.model

import java.io.Serializable

/**
 * Model regarding api-s
 */
data class FruitsModel(
    var id: Int?,
    var type: String?,
    var vitamins: Int,
    var image: String?) : Serializable


data class EntriesModel(
    var id: Int?,
    var date: String?,
    var fruit: List<FruitModel>?) : Serializable


data class FruitModel(
    var fruitId: Int?,
    var fruitType: String?,
    var amount: Int) : Serializable
