package com.cplier.diameter.packet.avp.derived

import com.cplier.diameter.packet.avp.basic.Integer32AVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData

/**
 * The Enumerated format is derived from the Integer32 Basic AVP Format. The definition contains a
 * list of valid values and their interpretation and is described in the Diameter application
 * introducing the AVP.
 */
class EnumeratedAVP : Integer32AVP {

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)

}
