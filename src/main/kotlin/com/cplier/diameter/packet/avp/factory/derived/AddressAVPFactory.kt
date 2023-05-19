package com.cplier.diameter.packet.avp.factory.derived

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.derived.AddressAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.factory.AVPFactory

object AddressAVPFactory : AVPFactory() {
  override fun createAVP(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP = AddressAVP(avpCode, flags, vendorId)

  override fun createAVP(dictData: AVPDictionaryData): DiameterAVP = AddressAVP(dictData)
}
