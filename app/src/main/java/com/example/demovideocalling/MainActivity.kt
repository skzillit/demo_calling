package com.example.demovideocalling

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.demovideocalling.lib.RoomOptions
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.io.IOException
import java.net.URI
import java.nio.ByteBuffer


class MainActivity : AppCompatActivity() {
    var socket: Socket? = null
    private val TAG = "MainActivity()"
    private val ON_CONNECTION = "connect"
    private val CONNECTION = "connection"
    private val ON_DISCONNECTION = "disconnect"
    private val ON_PING = "ping"
    private val ON_CONNECT_TIMEOUT = "connect_timeout"
    private val ON_CONNECTION_FAILED = "connect_failed"

    private var mOptions: RoomOptions? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        var webSocketManager = WebSocketManager()
//        webSocketManager.connectWebSocket("wss://127.0.0.1:4")
//
//        // Send a message after 2 seconds
//        Handler(Looper.getMainLooper()).postDelayed({
//            webSocketManager.sendMessage("Hello from Android WebSocket!")
//        }, 2000)
//        connectSocket()

        val serverUri =
            URI("wss://mediasoup-dev.zillit.com/protoo?roomId=ki5s2mbn&peerId=ybgy2mey") // Change this to your Protoo server address
        var protooClient = ProtooClient(serverUri)
        protooClient.connect()
    }
    fun loadRoomConfig(){
        mOptions = RoomOptions()

        mOptions?.setProduce(true);
        mOptions?.setConsume(true);
        mOptions?.setForceTcp(false);
        mOptions?.setUseDataChannel(true);
    }

    private fun connectSocket() {
//        logsForDebug(
//            "initializeSocket()",
//            "Socket Init Called() with moduleData = ${network.encryptedHeadersForSocket()}"
//        )
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor()) // Attach logging interceptor
            .build()
        val mOptions = IO.Options().apply {
            reconnection = true
//            var headers = mutableMapOf<String, String>()
//            headers["Sec-WebSocket-Protocol"] = "protoo"
////            headers[ApiConstants.HEADER_MODULE_DATA] = network.encryptedHeadersForSocket()
//            auth = headers
//            transports = arrayOf("websocket")
            query = "roomId=ki5s2mbn&peerId=ybgy2mey"
//            extraHeaders = mapOf(
//                "Sec-WebSocket-Protocol" to listOf("protoo"),
//                "roomId" to listOf("ki5s2mbn"),
//                "peerId" to listOf("ybgy2mey")
//            )
            callFactory = okHttpClient
            logsForDebug(
                "initializeSocket()",
                "Socket Init Called() with moduleData = "
            )
        }
        socket = IO.socket("https://mediasoup-dev.zillit.com/protoo", mOptions)
        socket!!.connect()
        onConnection()
    }

    /**
     * Set Socket Connection Handler
     */
    @Synchronized
    fun onConnection() {
        socket?.let {
            it.on("connectionrequest") { data ->
                logsForDebug(
                    "onConnection()",
                    "Acknowledge on connectionrequest. Is connection established Successfully = ${it.connected()}. Socket Id = ${it.id()} ON URL->  ${data.contentToString()}"
                )
                if (it.connected()) {

                }

            }
            it.on(CONNECTION) { data ->
                logsForDebug(
                    "onConnection()",
                    "Acknowledge on $CONNECTION. Is connection established Successfully = ${it.connected()}. Socket Id = ${it.id()} ON URL->  ${data.contentToString()}"
                )
                if (it.connected()) {

                }


            }
            it.on(ON_CONNECTION) { data ->
                logsForDebug(
                    "onConnection()",
                    "Acknowledge on $ON_CONNECTION. Is connection established Successfully = ${it.connected()}. Socket Id = ${it.id()} ON URL->  ${data.contentToString()}"
                )
                if (it.connected()) {

                }


            }
            it.on(ON_DISCONNECTION) {

                logsForDebug("onConnection()", "Disconnected...")
            }
            it.on(ON_PING) {
                logsForDebug("onConnection()", "$ON_PING...")
            }
            it.on(ON_CONNECT_TIMEOUT) {
                logsForDebug("onConnection()", "Socket Timeout...")
            }
            it.on(ON_CONNECTION_FAILED) {
                logsForDebug("onConnection()", "Socket Failed To Connect... $it")
            }
        }
    }

    /**
     * Print logs For Debugging
     */
    private fun logsForDebug(funcName: String, message: String) {
        logsDebug(TAG, "$funcName $message")
    }

    /**
     * Log For Debugging
     */
    fun logsDebug(tag: String, message: String) {
        try {
            Log.d(tag, "$message")
        } catch (e: java.lang.Exception) {
            Log.d(tag, message)
        }
    }
}

class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log request headers
        println("🔹 Request URL: ${request.url}")
        println("🔹 Request Headers:")
        request.headers.forEach { header ->
            println("   ${header.first}: ${header.second}")
        }

        return chain.proceed(request)
    }
}


class WebSocketManager {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connectWebSocket(url: String) {
        val request = Request.Builder().url(url)
            .addHeader("Sec-WebSocket-Protocol", "protoo")   // Custom User-Agent
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("WebSocket Connected!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Received Message: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                println("Received Binary Message: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket Closing: $code - $reason")
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("WebSocket Error: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun closeWebSocket() {
        webSocket?.close(1000, "Normal Closure")
    }
}

class ProtooClient(serverUri: URI) :
    WebSocketClient(
        serverUri, Draft_6455(), mapOf(
            "Sec-WebSocket-Protocol" to "protoo",
            "Sec-WebSocket-Version" to "13",
            "Connection" to "Upgrade",
            "Upgrade" to "websocket",
            "Sec-WebSocket-Extensions" to "permessage-deflate; client_max_window_bits"
        )
    ) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("✅ Connected to Protoo Server with protocol: " + handshakedata?.httpStatusMessage)

        // Send a JOIN request
        val requestMessage = """
            {
                "request": true,
                "method": "join",
                "data": { "peerId": "android-user" }
            }
        """.trimIndent()

        send(requestMessage)
    }

    override fun onMessage(message: String?) {
        println("📨 Received message: $message")
    }

    override fun onMessage(bytes: ByteBuffer?) {
        super.onMessage(bytes)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("❌ Connection closed: $reason")
    }

    override fun onError(ex: Exception?) {
        println("⚠ Error: ${ex?.message}")
        ex?.printStackTrace()
    }
}