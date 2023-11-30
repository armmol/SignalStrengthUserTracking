package com.example.test.model

data class GridCell(
    val x: Int,
    val y: Int,
    val averageStrengths: Map<String, Int>
)