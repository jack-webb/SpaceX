package me.jackwebb.spacex.network.dto

interface Mapper<T> {
    fun toModel(): T
}