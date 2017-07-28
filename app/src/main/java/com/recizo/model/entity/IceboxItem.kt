package com.recizo.model.entity

import java.io.Serializable


data class IceboxItem(val id: Int, val name: String, val memo: String, val date: String, val category: Category): Serializable {
  constructor(id: Int, name: String, memo: String, year: String, month: String, day: String, category: Category)
      : this (id, name, memo, "$year/$month/$day", category)

  enum class Category(val name_jp: String) {
    vegetable("野菜") ,
    fruit("果実"),
    meat("肉類"),
    seafood("魚介類"),
    dairy("乳製品・卵"),
    mushroom("菌糸類"),
    seasoning("調味料"),
  }
}