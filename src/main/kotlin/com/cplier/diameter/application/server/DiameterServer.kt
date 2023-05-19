package com.cplier.diameter.application.server

import com.cplier.diameter.application.DiameterApplication
import com.cplier.diameter.application.DiameterStack
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

class DiameterServer {

  companion object {
    private val LOGGER: Logger = LoggerFactory.getLogger(DiameterServer::class.java)
  }

  private val application: DiameterApplication
  private val stack: DiameterStack

  private val localIp: String
  private val localPort: Int

  private var sessionStarted: Boolean = false

  constructor(application: DiameterApplication, ipAddress: String, port: Int) {
    this.stack = DiameterStack(application, ipAddress, port)
    this.application = application
    this.localIp = ipAddress
    this.localPort = port
  }

  fun start() {
    if (!this.sessionStarted) {
      try {
        LOGGER.debug("Starting Server.")
        this.stack.startWithListening()
      } catch (e: IOException) {
        LOGGER.error(e.message)
      }
      sessionStarted = true
    } else {
      LOGGER.warn("Server already started")
    }
  }

  fun destroy() {
    LOGGER.debug("Server Shutdown")
    this.stack.shutdown()
  }
}
