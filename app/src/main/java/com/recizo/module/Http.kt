package com.recizo.module

import android.os.AsyncTask
import android.util.Log
import com.github.mikephil.charting.data.Entry
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

class Http(private val url: String, private val key: String? = null) : AsyncTask<Void, Void, Http.Response>() {
  private var callback: Callback? = null
  private var errCode: ErrorCode? = null

  fun setCallback(cb: Callback) { callback = cb }

  override fun doInBackground(vararg args: Void?): Response? {
    val url = URL(this.url)
    try {
      val connection = url.openConnection() as HttpURLConnection
      connection.connectTimeout = 10000
      connection.readTimeout = 10000
      if(key != null)connection.setRequestProperty("Authorization", key)
      connection.connect()
      val status = connection.responseCode
      if(status == HttpsURLConnection.HTTP_OK) {
        val encoding = connection.contentEncoding ?: "UTF-8"
        val streamReader = InputStreamReader(connection.inputStream, encoding)
        val buff = BufferedReader(streamReader)
        val sb = StringBuffer()
        var line: String? = buff.readLine()
        while(line != null) {
          sb.append(line)
          line = buff.readLine()
        }
        val resUrl = connection.url
        val headers = connection.headerFields.mapValues { it.value.reduce { acc, s -> acc + s } }
        buff.close()
        streamReader.close()
        connection.inputStream.close()
        return Response(headers, sb.toString(), resUrl.toString())
      }
    } catch (e: IOException) {
      e.printStackTrace()
      this.errCode = ErrorCode.CONNECTION_ERROR
    } catch (e: UnsupportedEncodingException) {
      e.printStackTrace()
      this.errCode = ErrorCode.UNSUPPORTED_ENCODING
    }
    return null
  }

  override fun onPostExecute(result: Response?) {
    super.onPostExecute(result)
    if(result == null) {
      if(this.errCode == null) this.errCode = ErrorCode.EMPTY_BODY
      callback?.onError(this.errCode!!)
    }
    else callback?.onSuccess(result)
  }

  class Response(val headers: Map<String, String>, val body: String, val url: String)

  enum class ErrorCode {
    HTTP_ERROR,
    EMPTY_BODY,
    CONNECTION_ERROR,
    UNSUPPORTED_ENCODING,
    INCORRECT_CONTENT_TYPE
  }

  interface Callback {
    fun onSuccess(response: Response)
    fun onError(code: ErrorCode)
  }
}
