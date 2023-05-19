package com.cplier.diameter.packet.avp.factory.derived

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.derived.TimeAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.factory.AVPFactory

object TimeAVPFactory : AVPFactory() {
  override fun createAVP(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP = TimeAVP(avpCode, flags, vendorId)

  override fun createAVP(dictData: AVPDictionaryData): DiameterAVP = TimeAVP(dictData)
}
