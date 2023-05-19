package com.cplier.diameter.application

import com.cplier.diameter.exception.DiameterRuntimeException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicBoolean

class Acceptor(
  private val ipAddrStr: String,
  private val listenPort: Int,
  exitAfter: Boolean,
  private val diameterStack: DiameterStack
) : Runnable {

  private var serverSocket: ServerSocket? = null
  private val stopWorking: AtomicBoolean = AtomicBoolean(false)

  companion object {
    private val LOGGER: Logger = LoggerFactory.getLogger(Acceptor::class.java)
  }

  /**
   * Binds the socket and returns actual local port.
   */
  @Throws(IOException::class)
  fun bind(): Int {
    val inetAddress = InetAddress.getByName(this.ipAddrStr)
    this.serverSocket = ServerSocket(this.listenPort, 0, inetAddress)
    return this.serverSocket!!.localPort
  }

  override fun run() {
    while (!this.stopWorking.get()) {
      if (this.serverSocket == null) throw DiameterRuntimeException("Server socket haven't been init.")
      try {
        val clientSocket = this.serverSocket!!.accept()
        diameterStack.connectionAccepted(clientSocket)
      } catch (e: IOException) {
        if (this.stopWorking.get()) {
          LOGGER.info("Acceptor Stopped.")
          return
        }
        throw DiameterRuntimeException("Error accepting client connection", e)
      }
    }
    LOGGER.info("Acceptor Stopped.")
    try {
      this.serverSocket!!.close()
    } catch (e: IOException) {
      throw DiameterRuntimeException("Error closing acceptor socket", e)
    }
  }

  @Synchronized
  fun stop() {
    this.stopWorking.set(true);
    if (this.serverSocket != null) {
      try {
        this.serverSocket!!.close()
      } catch (e: IOException) {
        LOGGER.error("IOException on Acceptor.stop...", e)
      }
    }
  }
}
