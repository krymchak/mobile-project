package com.example.mobilneprojekt

class CarsInfo (name: String, type: String, price: Float)
{
    private val name:String
    private val type: String
    private val price: Float

    init
    {
        this.name=name
        this.type=type
        this.price=price
    }

    fun getName(): String
    {
        return name
    }

    fun getType(): String
    {
        return type
    }
    fun getPrice(): Float
    {
        return price
    }

}