package com.cplier.diameter.packet.avp.factory.derived

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.derived.IPFilterRuleAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.factory.AVPFactory

object IPFilterRuleAVPFactory : AVPFactory() {
  override fun createAVP(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP =
    IPFilterRuleAVP(avpCode, flags, vendorId)

  override fun createAVP(dictData: AVPDictionaryData): DiameterAVP = IPFilterRuleAVP(dictData)
}
