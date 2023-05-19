package com.cplier.diameter.application

import com.cplier.diameter.exception.DiameterRuntimeException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.InputStream
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.atomic.AtomicBoolean

class Receiver(private val sock: Socket, private val stack: DiameterStack) : Runnable {

  companion object {
    private val LOGGER: Logger = LoggerFactory.getLogger(Receiver::class.java)

    private const val BUFFER_SIZE = 4096
  }

  private val stopWorking: AtomicBoolean = AtomicBoolean(false)
  private var inputStream: InputStream? = null

  override fun run() {
    val buffer = ByteArray(BUFFER_SIZE)
    var startOff = 0
    try {
      if (this.inputStream == null) {
        this.inputStream = sock.getInputStream()
      }
      while (!this.stopWorking.get()) {
        val len = this.inputStream!!.read(buffer, startOff, (BUFFER_SIZE - startOff))
        if (len < 0) {
          break
        }
        val newBuffer = stack.messageReceived(buffer, len + startOff)
        /*
         * since TCP is a stream-based protocol no message boundaries are
         * followed/provided. Therefore, after the processing there either - message is
         * not completed yet with the received part, or - there are byte more than a
         * message (that is related to the another message) in both cases, the message
         * handler gives back all/some part of the message. We should put it into the
         * buffer back to wait it to be competed. Note that, message handler is
         * responsible for removing the bytes that are not related to a message that is
         * expected (such as an unknown protocol message)
         *
         */
        startOff = if (newBuffer == null) {
          /* assume all part of buffer processed */
          0
        } else {
          /*
             * there is a remaining part, put it into buffer and wait for remaining part
             */
          System.arraycopy(newBuffer, 0, buffer, 0, newBuffer.size)
          newBuffer.size + 1
        }
      }
    } catch (e: IOException) {
      if (!stopWorking.get()) {
        LOGGER.error("Exception on receiver: {}", sock.toString(), e)
        if (e is SocketException) {
          stack.connectionDisconnected(sock)
        }
      }
    } catch (e: StringIndexOutOfBoundsException) {
      LOGGER.error("Exception on receiver")
    }
    try {
      this.sock.close()
    } catch (e: IOException) {
      throw DiameterRuntimeException("Error closing receiver socket", e)
    }
  }

  @Synchronized
  fun stop() {
    this.stopWorking.set(true)
    LOGGER.debug("Stopping the receiver ... {}", this.stopWorking.get())
    LOGGER.debug("Stopping the receiver ... {}", this.sock.toString())
    if (!this.sock.isClosed) {
      try {
        this.sock.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }
}
