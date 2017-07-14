package jp.gr.java_conf.item.recizo.model

/**
 * Created by user on 17/07/11.
 */
data class Vegetable(val name: String, val memo: String, val date: String) {
  constructor(name: String, memo: String, year: String, month: String, day: String)
      : this (name, memo, "$year/$month/$day") // TODO year month dayのセパレータを用意する
}