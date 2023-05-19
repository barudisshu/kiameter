package com.cplier.diameter.packet.message

import com.cplier.diameter.*
import com.cplier.diameter.exception.DiameterDictionaryException
import com.cplier.diameter.exception.DiameterParseException
import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.basic.*
import com.cplier.diameter.packet.avp.derived.*
import com.cplier.diameter.packet.avp.factory.AVPFactory
import java.net.InetAddress
import java.nio.ByteBuffer
import kotlin.experimental.and

class DiameterMessage(val header: DiameterHeader, var avpList: List<DiameterAVP> = arrayListOf()) {

  var packet: ByteArray = ByteArray(header.messageLength)

  val messageLength: Int
    get() = header.messageLength

  fun encodePacket(): ByteArray {
    packet = ByteArray(messageLength)
    val buffer = ByteBuffer.wrap(packet)
    header.encode(buffer)
    for (diameterAVP in avpList) {
      diameterAVP.encode(buffer)
    }
    return packet
  }

  companion object {

    @Throws(DiameterParseException::class)
    fun decodePacket(packet: ByteArray): DiameterMessage {
      val buffer = ByteBuffer.wrap(packet)
      val hdr = decodePacketForHeader(buffer)
      val message = DiameterMessage(hdr)
      decodePacketForAVPs(buffer, message)
      return message
    }

    /**
     * Decode packet header message from [ByteBuffer].
     *
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
    @Throws(DiameterParseException::class)
    fun decodePacketForHeader(buffer: ByteBuffer): DiameterHeader {
      val version = readUint8(buffer)
      val length = readUint24(buffer)
      val flags = readUint8(buffer)
      val commandCode = readUint24(buffer)
      val applicationId = readUint32(buffer)
      val hopByHopId = readUint32(buffer)
      val endToEndId = readUint32(buffer)
      val header = DiameterHeader(version, length, flags, commandCode, applicationId, hopByHopId, endToEndId)

      if (version != DIAMETER_VERSION) {
        throw DiameterParseException(
          RC_DIAMETER_UNSUPPORTED_VERSION,
          rcvHeader = header,
          message = "Unsupported version = $version"
        )
      }
      if (length > buffer.limit()) {
        throw DiameterParseException(
          RC_DIAMETER_INVALID_MESSAGE_LENGTH,
          rcvHeader = header,
          message = "Parsed message length value is higher than the received packet size"
        )
      }
      if ((flags and HEADER_MASK_RESERVED) != ZERO_BYTE) {
        throw DiameterParseException(
          RC_DIAMETER_INVALID_HDR_BITS,
          rcvHeader = header,
          message = "There are no reserved bits in the header with the value zero (0)"
        )
      }
      if (((flags and HEADER_MASK_BIT_R) != ZERO_BYTE) && ((flags and HEADER_MASK_BIT_E) != ZERO_BYTE)) {
        throw DiameterParseException(
          RC_DIAMETER_INVALID_HDR_BITS,
          rcvHeader = header,
          message = "Error bit is set for the Request message"
        )
      }
      return header
    }

    /**
     * Decode AVPs from DiameterMessage.
     * ```txt
     *  0                   1                   2                   3
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                           AVP Code                            |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |V M P r r r r r|                  AVP Length                   |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |                        Vendor-ID (opt)                        |
     * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
     * |    Data ...
     * +-+-+-+-+-+-+-+-+
     *
     * AVP Flags V(endor-specific), M(andatory), 'P' bit indicates the need
     * for encryption for end-to-end security
     * ```
     */
    @Throws(DiameterParseException::class)
    fun decodePacketForAVPs(buffer: ByteBuffer, message: DiameterMessage) {
      var avpCode: Long
      var avpLength: Int
      var flags: Byte
      var vendorId: Long
      var dataLength: Int

      while (buffer.position() < message.messageLength) {
        avpCode = readUint32(buffer)
        flags = readUint8(buffer)

        if (DiameterAVP.isReserved(flags)) {
          throw DiameterParseException(
            RC_DIAMETER_INVALID_AVP_BITS,
            rcvMessage = message,
            message = "Invalid AVP bits for AVP = $avpCode with flags = $flags"
          )
        }

        avpLength = readUint24(buffer)
        if (DiameterAVP.isVendorSpecific(flags)) {
          vendorId = readUint32(buffer)
          dataLength = avpLength - AVP_HDR_LEN_WITH_VENDOR
        } else {
          vendorId = 0
          dataLength = avpLength - AVP_HDR_LEN_WITHOUT_VENDOR
        }

        if ((dataLength > avpLength) || (buffer.limit() - buffer.position()) < dataLength) {
          val len =
            if ((buffer.limit() - buffer.position()) < dataLength) buffer.limit() - buffer.position() else avpLength

          val failedAVPData = ByteArray(len)
          buffer.get(failedAVPData, 0, len)

          throw DiameterParseException(
            RC_DIAMETER_INVALID_AVP_LENGTH,
            rcvMessage = message,
            message = "Not enough AVP data remaining",
            failedAvpData = failedAVPData
          )
        }

        val padding = calculatePadding(avpLength)
        val avp = AVPFactory.create(avpCode, flags, vendorId)
        avp.decodeData(buffer, dataLength)
        buffer.position(buffer.position() + padding)
        message.avpList += avp
      }
    }


    fun createMessage(commandFlags: Byte, commandCode: Int, applicationId: Long): DiameterMessage {
      val header = DiameterHeader(
        DIAMETER_VERSION,
        commandFlags = commandFlags,
        commandCode = commandCode,
        applicationId = applicationId
      )
      return DiameterMessage(header)
    }
  }

  fun addAVPIntoList(avp: DiameterAVP) {
    this.avpList += avp
    this.header.addLengthToMessage(avp.avpLength)
    // Padding
    this.header.addLengthToMessage(calculatePadding(avp.avpLength))
  }

  fun addAVP(avp: DiameterAVP) {
    addAVPIntoList(avp)
  }

  @Throws(DiameterDictionaryException::class)
  fun addAVPFromDictionary(avpCode: Long, vendorId: Long, data: String) {
    val avp = AVPFactory.createAVPFromDictionary(avpCode, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  @Throws(DiameterDictionaryException::class)
  fun addAVPFromDictionary(avpCode: Long, vendorId: Long, data: ByteArray) {
    val avp = AVPFactory.createAVPFromDictionary(avpCode, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addFloat32AVP(avpCode: Long, flags: Byte, vendorId: Long, data: Float) {
    val avp = Float32AVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addFloat64AVP(avpCode: Long, flags: Byte, vendorId: Long, data: Double) {
    val avp = Float64AVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addGroupAVP(avpCode: Long, flags: Byte, vendorId: Long, avpList: List<DiameterAVP>) {
    val avp = GroupedAVP(avpCode, flags, vendorId)
    avp.setList(avpList)
    addAVPIntoList(avp)
  }

  fun addInteger32AVP(avpCode: Long, flags: Byte, vendorId: Long, data: Int) {
    val avp = Integer32AVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addInteger64AVP(avpCode: Long, flags: Byte, vendorId: Long, data: Long) {
    val avp = Integer64AVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addOctetStringAVP(avpCode: Long, flags: Byte, vendorId: Long, data: String) {
    val avp = OctetStringAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addOctetStringAVP(avpCode: Long, flags: Byte, vendorId: Long, data: ByteArray) {
    val avp = OctetStringAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addUnsigned32AVP(avpCode: Long, flags: Byte, vendorId: Long, data: Long) {
    val avp = Unsigned32AVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addUnsigned64AVP(avpCode: Long, flags: Byte, vendorId: Long, data: Long) {
    val avp = Unsigned64AVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addAddressAVP(avpCode: Long, flags: Byte, vendorId: Long, data: InetAddress) {
    val avp = AddressAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addAddressAVP(avpCode: Long, flags: Byte, vendorId: Long, data: ByteArray) {
    val avp = AddressAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addAddressAVP(avpCode: Long, flags: Byte, vendorId: Long, data: String) {
    val avp = AddressAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addIdentityAVP(avpCode: Long, flags: Byte, vendorId: Long, data: String) {
    val avp = IdentityAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addEnumeratedAVP(avpCode: Long, flags: Byte, vendorId: Long, data: Int) {
    val avp = EnumeratedAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addIPFilterRuleAVP(avpCode: Long, flags: Byte, vendorId: Long, data: String) {
    val avp = IPFilterRuleAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addQoSFilterRuleAVP(avpCode: Long, flags: Byte, vendorId: Long, data: String) {
    val avp = QoSFilterRuleAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addTimeAVP(avpCode: Long, flags: Byte, vendorId: Long, data: String) {
    val avp = TimeAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addTimeAVP(avpCode: Long, flags: Byte, vendorId: Long, data: ByteArray) {
    val avp = TimeAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addTimeAVP(avpCode: Long, flags: Byte, vendorId: Long, data: Long) {
    val avp = TimeAVP(avpCode, flags, vendorId)
//    avp.setData(data)
    addAVPIntoList(avp)
  }

  fun addUTF8StringAVP(avpCode: Long, flags: Byte, vendorId: Long, data: String) {
    val avp = UTF8StringAVP(avpCode, flags, vendorId)
    avp.setData(data)
    addAVPIntoList(avp)
  }
}
