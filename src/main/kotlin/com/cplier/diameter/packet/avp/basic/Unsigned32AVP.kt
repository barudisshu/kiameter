package com.cplier.diameter.packet.avp.basic

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.readUint32
import java.nio.ByteBuffer

/**
 * 32-bit unsigned value, in network byte order. The AVP Length field MUST be set to 12(16 if the 'V' bit is enabled).
 */
class Unsigned32AVP : DiameterAVP {

  companion object {
    private const val LENGTH_BITS = 4
  }

  private var data: Long? = null

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)

  fun setData(data: Long) {
    this.data = data
    addDataLength(LENGTH_BITS)
  }

  override fun setData(data: String) {
    this.data = data.toLongOrNull()
    addDataLength(LENGTH_BITS)
  }

  override fun encodeData(buffer: ByteBuffer) {
    buffer.putInt(data!!.toInt())
  }

  override fun decodeData(buffer: ByteBuffer, length: Int) {
    this.data = readUint32(buffer)
    addDataLength(length)
  }
}
