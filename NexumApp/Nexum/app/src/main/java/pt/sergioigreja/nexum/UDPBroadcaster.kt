package pt.sergioigreja.nexum

import android.content.Context
import android.net.wifi.WifiManager
import android.os.StrictMode
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UDPBroadcaster(private val mContext: Context) {
    private val socket: DatagramSocket
    private val policy: StrictMode.ThreadPolicy
    private val PORT = 8088
    val status = mutableStateOf("WAITING")

    init {
        policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        socket = DatagramSocket()
        socket.broadcast = true
    }

    fun sendBroadcast(message: String) {
        try {
            status.value = "SENDING"
            var data = message.toByteArray()
            var datagramPacket = DatagramPacket(data, data.size, getBroadcastAddress(), PORT)
            socket.send(datagramPacket)
            Log.d("UDP", "Broadcast sent to ${getBroadcastAddress().hostAddress}")
            status.value = "SENT"
        }catch (e: IOException) {
            Log.e(e.toString(), "Error sending broadcast")
            status.value = "ERROR"
        }
    }

    fun getBroadcastAddress(): InetAddress {
        status.value = "GETTING ADDRESS"
        val wifi = mContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifi.dhcpInfo

        val broadcast = (info.ipAddress and info.netmask) or info.netmask.inv();
        val quads = ByteArray(4)
        for (k in 0..3)
            quads[k] = (broadcast shr k * 8).toByte()

        Log.d("UDP", "Broadcast address: ${InetAddress.getByAddress(quads)}")
        return InetAddress.getByAddress(quads)
    }

    fun close() {
        socket.close()
    }

}