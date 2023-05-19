package com.cplier.diameter.packet.avp.basic

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

open class OctetStringAVP : DiameterAVP {

  protected var octetData: String? = null

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)

  override fun setData(data: String) {
    this.octetData = data
    this.byteData = data.toByteArray(StandardCharsets.UTF_8)
    addDataLength(data.length)
  }

  override fun encodeData(buffer: ByteBuffer) {
    buffer.put(byteData)
  }

  override fun decodeData(buffer: ByteBuffer, length: Int) {
    byteData = ByteArray(length)
    buffer.put(byteData)
    octetData = String(byteData, StandardCharsets.UTF_8)
    addDataLength(length)
  }
}
