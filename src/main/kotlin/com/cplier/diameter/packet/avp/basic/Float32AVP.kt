package com.cplier.diameter.packet.avp.basic

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import java.nio.ByteBuffer

/**
 * This represents floating point values of single precision as described by [FLOATPOINT](https://www.rfc-editor.org/rfc/rfc6733.html#ref-FLOATPOINT). The 32-bit value is transmitted in network byte order. The AVP Length field MUST be set to 12 (16 if the 'V' bit is enabled).
 */
class Float32AVP : DiameterAVP {

  companion object {
    private const val LENGTH_BITS = 4
  }

  private var data: Float? = null

  constructor(code: Long, flags: Byte, vendorId: Long) : super(code, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)

  fun setData(data: Float) {
    this.data = data
    addDataLength(LENGTH_BITS)
  }

  override fun setData(data: String) {
    this.data = data.toFloatOrNull()
    addDataLength(LENGTH_BITS)
  }

  override fun encodeData(buffer: ByteBuffer) {
    buffer.putFloat(data!!)
  }

  override fun decodeData(buffer: ByteBuffer, length: Int) {
    this.data = buffer.getFloat()
    addDataLength(length)
  }
}
