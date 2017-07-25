package jp.gr.java_conf.item.recizo.module

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection



class Http(private val url: String) : AsyncTask<Void, Void, String>() {
  enum class ErrorCode {
    HTTP_ERROR,
    EMPTY_BODY,
    CONNECTION_ERROR,
    UNSUPPORTED_ENCODING,
  }
  interface Callback {
    fun onSuccess(body: String)
    fun onError(code: ErrorCode)
  }
  private var callback: Callback? = null
  fun setCallback(cb: Callback) {
    callback = cb
  }
  private var errCode: ErrorCode? = null


  override fun doInBackground(vararg args: Void?): String {
    val url: URL = URL(this.url)
    try {
      val connection = url.openConnection() as HttpURLConnection
      connection.connectTimeout = 10000
      connection.readTimeout = 10000
      connection.connect()
      val status = connection.responseCode
      if(status == HttpsURLConnection.HTTP_OK) {
        Log.d("http", "ok")
        var encoding = connection.contentEncoding
        if (encoding == null) {
          encoding = "UTF-8"
        }
        val streamReader = InputStreamReader(connection.inputStream, encoding)
        val buff = BufferedReader(streamReader)
        val sb = StringBuffer()
        var line: String? = buff.readLine()
        while(line != null) {
          sb.append(line)
          line = buff.readLine()
        }
        buff.close()
        streamReader.close()
        connection.inputStream.close()
        return sb.toString()
      }
    } catch (e: IOException) {
      e.printStackTrace()
      this.errCode = ErrorCode.CONNECTION_ERROR
    } catch (e: UnsupportedEncodingException) {
      e.printStackTrace()
      this.errCode = ErrorCode.UNSUPPORTED_ENCODING
    }
    return ""
  }

  override fun onPostExecute(result: String) {
    super.onPostExecute(result)
    if(result == "") {
      if(this.errCode == null) this.errCode = ErrorCode.EMPTY_BODY
      callback?.onError(this.errCode!!)
    }
    else callback?.onSuccess(result)
  }

}
