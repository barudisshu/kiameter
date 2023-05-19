package com.cplier.diameter.packet.avp.factory.basic

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.basic.Float32AVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.factory.AVPFactory

object Float32AVPFactory : AVPFactory() {

  override fun createAVP(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP = Float32AVP(avpCode, flags, vendorId)
  override fun createAVP(dictData: AVPDictionaryData): DiameterAVP = Float32AVP(dictData)
}
