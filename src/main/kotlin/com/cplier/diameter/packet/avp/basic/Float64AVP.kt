package com.cplier.diameter.packet.avp.basic

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import java.nio.ByteBuffer

/**
 * This represents floating point values of double precision as described by [FLOATPOINT](https://www.rfc-editor.org/rfc/rfc6733.html#ref-FLOATPOINT). The 64-bit values is transmitted in network byte order. The AVP Length field MUST be set to 16(20 if the 'V' bit is enabled).
 */
class Float64AVP : DiameterAVP {

  companion object {
    private const val LENGTH_BITS = 8
  }

  private var data: Double? = null

  constructor(code: Long, flags: Byte, vendorId: Long) : super(code, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)

  fun setData(data: Double) {
    this.data = data
    addDataLength(LENGTH_BITS)
  }

  override fun setData(data: String) {
    this.data = data.toDoubleOrNull()
    addDataLength(LENGTH_BITS)
  }

  override fun encodeData(buffer: ByteBuffer) {
    buffer.putDouble(data!!)
  }

  override fun decodeData(buffer: ByteBuffer, length: Int) {
    this.data = buffer.getDouble()
    addDataLength(length)
  }
}
