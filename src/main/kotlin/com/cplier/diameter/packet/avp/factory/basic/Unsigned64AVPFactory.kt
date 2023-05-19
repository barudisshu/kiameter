package com.cplier.diameter.packet.avp.factory.basic

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.basic.Unsigned64AVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.factory.AVPFactory

object Unsigned64AVPFactory : AVPFactory() {
  override fun createAVP(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP =
    Unsigned64AVP(avpCode, flags, vendorId)

  override fun createAVP(dictData: AVPDictionaryData): DiameterAVP = Unsigned64AVP(dictData)
}
