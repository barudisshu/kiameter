package com.cplier.diameter.packet.avp.factory.basic

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.basic.Integer64AVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.factory.AVPFactory

object Integer64AVPFactory : AVPFactory() {
  override fun createAVP(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP =
    Integer64AVP(avpCode, flags, vendorId)

  override fun createAVP(dictData: AVPDictionaryData): DiameterAVP = Integer64AVP(dictData)
}
