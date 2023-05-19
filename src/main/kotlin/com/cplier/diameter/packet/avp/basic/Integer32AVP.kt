package com.cplier.diameter.packet.avp.basic

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.readUint24
import com.cplier.diameter.writeUint24
import java.nio.ByteBuffer

/**
 * 32-bit signed value, in network byte order. The AVP Length field MUST be set to 12(16 if the 'V' bit is enabled).
 */
open class Integer32AVP : DiameterAVP {

  companion object {
    private const val LENGTH_BITS = 4
  }

  private var data: Int? = null

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)

  fun setData(data: Int) {
    this.data = data
    addDataLength(LENGTH_BITS)
  }

  override fun setData(data: String) {
    this.data = data.toIntOrNull()
    addDataLength(LENGTH_BITS)
  }


  override fun encodeData(buffer: ByteBuffer) {
    writeUint24(buffer, data!!)
  }

  override fun decodeData(buffer: ByteBuffer, length: Int) {
    data = readUint24(buffer)
    addDataLength(length)
  }
}
