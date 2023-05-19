package com.cplier.diameter.exception

import com.cplier.diameter.byteArray2Hex
import com.cplier.diameter.packet.message.DiameterHeader
import com.cplier.diameter.packet.message.DiameterMessage
import java.lang.System.lineSeparator
import kotlin.experimental.and

/**
 * @param failedAvpData Support for multiple Failed-AVP.
 * @param rcvHeader The reference for Diameter header in the case of no reference to received message or when the application wants to access only header information. Therefore, it refers to the header of received message when it is set.
 * @param rcvMessage The reference for the message on process.
 */
class DiameterParseException(
  error: Int,
  message: String?,
  cause: Throwable? = null,
  val rcvHeader: DiameterHeader? = null,
  val rcvMessage: DiameterMessage? = null,
  failedAvpData: ByteArray? = null
) : DiameterException(error, message, cause, DIAMETER_PARSE_EXCEPTION) {

  val failedAvpDataList: List<ByteArray> = arrayListOf()

  init {
    if (failedAvpData != null) {
      failedAvpDataList + failedAvpData
    }
  }

  override fun toString(): String {
    return StringBuilder(super.toString()).apply sb@{
      this@sb.append(lineSeparator())
        .append("Error Reason  = ")
        .append(errorReason)
      if (rcvHeader != null) {
        this@sb.append(lineSeparator())
          .append("Command Code  = ")
          .append(rcvHeader.commandCode)
          .append(lineSeparator())
          .append("Flags Byte    = ")
          .append(Integer.toBinaryString((rcvHeader.commandFlags.and(0xFF.toByte()).toInt())))
        failedAvpDataList.forEach {
          this@sb.append(lineSeparator())
            .append("failedAvpData = ")
            .append(byteArray2Hex(it))
        }
      }
    }.toString()
  }
}
