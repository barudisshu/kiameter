package com.cplier.diameter.application

import com.cplier.diameter.DIAMETER_MSG_HDR_LEN
import com.cplier.diameter.exception.DiameterParseException
import com.cplier.diameter.packet.message.DiameterMessage
import com.cplier.diameter.readUint24
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.*
import java.util.concurrent.atomic.AtomicInteger

class DiameterStack {

  companion object {
    /* return codes from the stack */
    const val RC_SUCCESS = 0
    const val RC_CONNECT_TIMEOUT = 1
    const val RC_UNKNOWN_HOST = 2
    const val RC_IO_EXCEPTION = 3

    /* stack working mode */
    const val WM_NONE = 0
    const val WM_SERVER = 1
    const val WM_CLIENT = 2

    val receiverCounter: AtomicInteger = AtomicInteger(0)

    private val LOGGER: Logger = LoggerFactory.getLogger(DiameterStack::class.java)
  }

  protected var connectTimeout = 2_000
  protected var workingMode = WM_NONE

  /* local IP */
  protected var localAddressStr: String
  protected var listenPort: Int

  protected var acceptor: Acceptor? = null
  protected var receiver: Receiver? = null

  /* after connection establishment */
  protected var connectionSocket: Socket? = null
  protected var localPort: Int = 0
  protected var remotePort: Int = 0
  protected lateinit var localAddress: InetAddress
  protected lateinit var remoteAddress: InetAddress

  protected val application: DiameterApplication

  fun isConnected(): Boolean = this.connectionSocket != null

  constructor(application: DiameterApplication, ipAddress: String, listenPort: Int) {
    this.application = application
    this.localAddressStr = ipAddress
    this.listenPort = listenPort
  }

  constructor(application: DiameterApplication) : this(application, InetAddress.getLocalHost().hostAddress, 3868)

  fun shutdown(): Unit {
    if (this.acceptor != null) {
      this.acceptor!!.stop()
    }
    if (this.receiver != null) {
      this.receiver!!.stop()
    }
  }

  @Throws(IOException::class)
  fun startWithListening() {
    this.acceptor = Acceptor(this.localAddressStr, this.listenPort, true, this)
    this.localPort = this.acceptor!!.bind()
    this.workingMode = WM_SERVER
    Thread(this.acceptor, "Acceptor").start()
  }

  /**
   * Accepts remote-address and port Return 0 if success Return 0 if success.
   */
  fun startWithAttempt(remoteAddress: String?, remotePort: Int, localAddress: String?, localPort: Int): Int {
    var result = RC_SUCCESS
    var start = 0L
    try {
      this.remoteAddress = InetAddress.getByName(remoteAddress)
      this.remotePort = remotePort
      start = System.currentTimeMillis()

      val sockaddr = InetSocketAddress(this.remoteAddress, this.remotePort)
      val sockaddr2 = InetSocketAddress(localAddress, localPort)

      this.connectionSocket = Socket()
      if (localAddress != null) {
        this.connectionSocket!!.bind(sockaddr2)
      }
      this.connectionSocket!!.connect(sockaddr, this.connectTimeout)
      this.workingMode = WM_CLIENT
      this.localAddress = this.connectionSocket!!.localAddress
      this.localPort = this.connectionSocket!!.localPort
      LOGGER.info("Connection is attempted successfully: Local Address = ${this.localAddress.hostAddress}: ${this.localPort} - Remote Address = ${this.remoteAddress.hostAddress}: ${this.remoteAddress}")
      this.application.onConnectionSuccess(
        this.localAddressStr,
        this.localPort,
        this.remoteAddress.hostAddress,
        this.remotePort
      )
      this.receiver = Receiver(this.connectionSocket!!, this)
      Thread(this.receiver, ("Receiver-${receiverCounter.getAndIncrement()}")).start()
    } catch (sot: SocketTimeoutException) {
      LOGGER.error("ConnectTimeout: Time = ${System.currentTimeMillis() - start}")
      result = RC_CONNECT_TIMEOUT
    } catch (e: UnknownHostException) {
      this.application.onConnectionFail(this.remoteAddress.hostAddress, this.remotePort)
      LOGGER.error("Unknown-Host --> ", e)
      result = RC_UNKNOWN_HOST
    } catch (e: IOException) {
      this.application.onConnectionFail(this.remoteAddress.hostAddress, this.remotePort)
      LOGGER.error("IO Problem   --> ", e)
      result = RC_IO_EXCEPTION
    }
    return result
  }

  /**
   * Called from application to send message through the established
   * connection/socket established connection/socket
   */
  fun sendMessage(msg: ByteArray, len: Int = msg.size): Int {
    var result = 0
    if (this.connectionSocket == null) {
      result = startWithAttempt(this.remoteAddress.hostAddress, this.remotePort, null, 0)
      if (result != 0) {
        LOGGER.error("Cannot connect to remote with address = ${this.remoteAddress.hostAddress}: ${this.remotePort}")
      }
    }
    try {
      val output = this.connectionSocket!!.getOutputStream()
      output.write(msg)
      this.application.onSendMessage()
    } catch (e: IOException) {
      LOGGER.error("IO Problem", e)
    }
    return result
  }
  /*
   * Callbacks from Acceptor and Receiver
   */

  /* called from Acceptor in the case of a connection accepted */
  fun connectionAccepted(socket: Socket) {
    this.connectionSocket = socket
    this.localPort = socket.localPort
    this.remotePort = socket.port
    this.localAddress = socket.localAddress
    this.remoteAddress = socket.inetAddress

    LOGGER.debug("Connection is accepted: Local Address = ${this.localAddress.hostAddress}: ${this.localPort} | Remote Address = ${this.remoteAddress.hostAddress}: ${this.remotePort}")

    this.application.onConnectionSuccess(
      this.localAddressStr,
      this.listenPort,
      this.remoteAddress.hostAddress,
      this.remotePort
    )
    this.receiver = Receiver(this.connectionSocket!!, this)
    Thread(this.receiver, "Receiver").start()
  }

  fun messageReceived(buffer: ByteArray, length: Int): ByteArray? {
    var rawmsg = buffer
    var remaining: ByteArray? = null
    var rawLength = length

    LOGGER.debug("Received DIAMETER RAW message <--> ")
    while (true) {
      // Message Checks
      if (rawmsg[0] != 1.toByte()) {
        LOGGER.warn("Unexpected version --> {}", rawmsg[0])
        val rem = ByteArray(rawmsg.size - 1)
        System.arraycopy(rawmsg, 1, rem, 0, rawmsg.size - 1)
        return rem
      }

      if (rawLength < DIAMETER_MSG_HDR_LEN) {
        /* no enough bytes even for header */
        remaining = rawmsg
        break
      }
      val msgLength = readUint24(rawmsg, 1) // First index is Diameter Version
      if (rawLength < msgLength) {
        /* no enough bytes for message */
        remaining = rawmsg
        break
      }

      val rl = rawLength - msgLength
      if (rl > 0) {
        /*
         * We have some additional bytes which are belong to another message. Keep them in remaining to retry in next loop
         */
        remaining = ByteArray(rl)
        System.arraycopy(rawmsg, msgLength, remaining, 0, rl)
        LOGGER.debug("There are remaining bytes in Diameter messageReceived with length = {}", remaining.size)
      }
      var message: DiameterMessage? = null

      try {
        message = DiameterMessage.decodePacket(rawmsg)
      } catch (e: DiameterParseException) {
        e.printStackTrace()
      }

      if (message != null) {
        // Pass the message to application level
        application.receiveMessage(this, message)
      } else {
        LOGGER.warn("Cannot produce a Diameter Message from received bytes ===>")
      }

      if (remaining == null) {
        LOGGER.debug("Breaking from Diameter messageReceived since there is no remaining.")
        break
      } else {
        /* continue with the remaining bytes */
        rawmsg = remaining
        rawLength = rawmsg.size
        remaining = null
        LOGGER.debug("Continue with remaining bytes with length = {}", rawLength)
      }
    }
    return remaining
  }

  /* called from receiver */
  fun connectionDisconnected(socket: Socket) {
    LOGGER.info("Connection disconnected")
    socket.close()
    this.application.onDisconnect(0)
  }

  fun setRemoteIp(remIP: String) {
    try {
      this.remoteAddress = InetAddress.getByName(remIP)
    } catch (e: UnknownHostException) {
      LOGGER.error("Unknown host problem: ", e)
    }
  }

  fun getRemoteIP(): String {
    return this.remoteAddress.hostAddress
  }


}
