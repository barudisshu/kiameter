package com.cplier.diameter.packet.message

import com.cplier.diameter.*
import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

/**
 * ```txt
 *  0                   1                   2                   3
 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |    Version    |                 Message Length                |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * | command flags |                  Command-Code                 |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                         Application-ID                        |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                      Hop-by-Hop Identifier                    |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                      End-to-End Identifier                    |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |  AVPs ...
 * +-+-+-+-+-+-+-+-+-+-+-+-+-
 *
 * Command Flags
 *     0 1 2 3 4 5 6 7
 *    +-+-+-+-+-+-+-+-+  R(equest), P(roxiable), E(rror)
 *    |R P E T r r r r|  T(Potentially re-transmitted message), r(eserved)
 *    +-+-+-+-+-+-+-+-+
 * ```
 */
class DiameterHeader(
  val version: Byte = DIAMETER_VERSION,
  var messageLength: Int = DIAMETER_MSG_HDR_LEN,
  var commandFlags: Byte = HEADER_MASK_RESERVED,
  val commandCode: Int,
  val applicationId: Long,
  val hopByHopId: Long = createHopByHopId(),
  val endToEndId: Long = createEndToEndId()
) {

  private fun flagsGetter(flag: Byte): Boolean = (commandFlags and flag) != ZERO_BYTE
  private fun flagsSetter(flag: Byte, bool: Boolean): Unit = if (bool) {
    commandFlags = commandFlags or flag
  } else {
    commandFlags = commandFlags and flag.inv()
  }

  var request: Boolean
    get() = flagsGetter(HEADER_MASK_BIT_R)
    set(value) = flagsSetter(HEADER_MASK_BIT_R, value)

  var proxiable: Boolean
    get() = flagsGetter(HEADER_MASK_BIT_P)
    set(value) = flagsSetter(HEADER_MASK_BIT_P, value)

  var error: Boolean
    get() = flagsGetter(HEADER_MASK_BIT_E)
    set(value) = flagsSetter(HEADER_MASK_BIT_E, value)


  var retransmit: Boolean
    get() = flagsGetter(HEADER_MASK_BIT_T)
    set(value) = flagsSetter(HEADER_MASK_BIT_T, value)


  fun addLengthToMessage(length: Int) {
    this.messageLength += length
  }

  fun encode(buffer: ByteBuffer) {
    writeUint8(buffer, version)  // 1 byte
    writeUint24(buffer, messageLength) // 3 bytes
    writeUint8(buffer, commandFlags) // 1 byte
    writeUint24(buffer, commandCode) // 3 bytes
    writeUint32(buffer, applicationId) // 4 bytes
    writeUint32(buffer, hopByHopId)  // 4 bytes
    writeUint32(buffer, endToEndId)  // 4 bytes
  }
}
