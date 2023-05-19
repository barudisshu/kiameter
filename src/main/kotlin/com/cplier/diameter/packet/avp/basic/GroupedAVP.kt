package com.cplier.diameter.packet.avp.basic

import com.cplier.diameter.*
import com.cplier.diameter.exception.DiameterParseException
import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.factory.AVPFactory
import java.nio.ByteBuffer

/**
 * The Data field is specified as a sequence of AVPs. These AVPs are concatenated -- including their headers and padding -- in the order in which they are specified and the result encapsulated in the Data field. The AVP Length field is set to 8(12 if the 'V' bit is enabled) plus the total length of all included AVPs, including their headers and padding. Thus, the AVP Length field of an AVP of type Grouped is always a multiple of 4.
 */
class GroupedAVP : DiameterAVP {

  private var avpList: List<DiameterAVP>

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId) {
    avpList = arrayListOf()
  }

  constructor(dictData: AVPDictionaryData) : super(dictData) {
    avpList = arrayListOf()
  }

  fun addAVP(avp: DiameterAVP) {
    this.avpList += avp
    this.dataLength += avp.avpLength
    this.avpLength += avp.avpLength
  }

  override fun setData(data: String) {
    // Useless, can't be used for Grouped AVP
  }

  fun setList(avpList: List<DiameterAVP>) {
    this.avpList = avpList
    for (avp in this.avpList) {
      this.dataLength += avp.avpLength
    }
    this.avpLength += this.dataLength

  }

  override fun encodeData(buffer: ByteBuffer) {
    for (avp in avpList) {
      avp.encode(buffer)
    }
  }

  override fun decodeData(buffer: ByteBuffer, length: Int) {
    var index = 0

    var avpCode: Long
    var avpLength: Int
    var flags: Byte
    var vendorId: Long
    var dataLength: Int

    while (index < length) {
      avpCode = readUint32(buffer)
      flags = readUint8(buffer)
      if (isReserved(flags)) {
        throw DiameterParseException(RC_DIAMETER_INVALID_AVP_BITS, "Invalid AVP bits for the AVP = $avpCode")
      }
      avpLength = readUint24(buffer)

      if (isVendorSpecific(flags)) {
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
        throw DiameterParseException(RC_DIAMETER_INVALID_AVP_LENGTH, "Not enough AVP data remaining")
      }

      val padding = calculatePadding(avpLength)
      val avp = AVPFactory.create(avpCode, flags, vendorId)
      avp.decodeData(buffer, length)
      buffer.position(buffer.position() + padding)

      avpList += avp
      index += avp.avpLength
    }
  }
}
