package com.cplier.diameter.packet.avp.factory.derived

import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.derived.URIAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.factory.AVPFactory

object URIAVPFactory : AVPFactory() {
  override fun createAVP(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP = URIAVP(avpCode, flags, vendorId)

  override fun createAVP(dictData: AVPDictionaryData): DiameterAVP = URIAVP(dictData)
}
