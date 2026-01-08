package kpdev.rustlingleaves.model.skeleton

import jakarta.persistence.Embeddable

@Embeddable
data class Point(val x: Int, val y: Int)