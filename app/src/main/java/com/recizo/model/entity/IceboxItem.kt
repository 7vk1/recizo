package com.recizo.model.entity

import java.io.Serializable

data class IceboxItem(val id: Int, val name: String, val memo: String, val date: String): Serializable {
  constructor(id: Int, name: String, memo: String, year: String, month: String, day: String)
      : this (id, name, memo, "$year/$month/$day")
}