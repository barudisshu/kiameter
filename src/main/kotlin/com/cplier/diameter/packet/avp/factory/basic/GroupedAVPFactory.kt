package com.cplier.diameter.packet.avp.factory.basic

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.basic.GroupedAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.factory.AVPFactory

object GroupedAVPFactory: AVPFactory() {
  override fun createAVP(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP = GroupedAVP(avpCode, flags, vendorId)

  override fun createAVP(dictData: AVPDictionaryData): DiameterAVP = GroupedAVP(dictData)
}
