package com.cplier.diameter.application.client

import com.cplier.diameter.application.DiameterApplication
import com.cplier.diameter.application.DiameterStack
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DiameterClient {

  companion object {
    private val LOGGER: Logger = LoggerFactory.getLogger(DiameterClient::class.java)
  }

  private val stack: DiameterStack
  private val remoteIp: String
  private val remotePort: Int
  private lateinit var localIp: String
  private var localPort: Int = 0

  constructor(application: DiameterApplication, remoteIp: String, remotePort: Int) {
    this.stack = DiameterStack(application)
    this.remoteIp = remoteIp
    this.remotePort = remotePort
  }

  fun bindToLocalAddress(localIp: String, localPort: Int) {
    this.localIp = localIp
    this.localPort = localPort
  }

  fun send(message: ByteArray) {
    LOGGER.info("Sending Diameter Message")
    if (!this.stack.isConnected()) {
      this.stack.startWithAttempt(this.remoteIp, this.remotePort, this.localIp, this.localPort)
    }
    this.stack.sendMessage(message)
  }

  fun destroy() {
    LOGGER.debug("Client Shutdown")
    this.stack.shutdown()
  }
}
