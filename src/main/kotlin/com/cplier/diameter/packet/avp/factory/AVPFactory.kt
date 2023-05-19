package com.cplier.diameter.packet.avp.factory

import com.cplier.diameter.*
import com.cplier.diameter.exception.DiameterDictionaryException
import com.cplier.diameter.exception.DiameterException
import com.cplier.diameter.packet.avp.DiameterAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.packet.avp.dictionary.getDictionaryData
import com.cplier.diameter.packet.avp.factory.basic.*
import com.cplier.diameter.packet.avp.factory.derived.*

abstract class AVPFactory {

  companion object {

    operator fun get(dataType: Int): AVPFactory = when (dataType) {
      DT_OCTET_STRING -> OctetStringAVPFactory
      DT_INTEGER_32 -> Integer32AVPFactory
      DT_INTEGER_64 -> Integer64AVPFactory
      DT_UNSIGNED_32 -> Unsigned32AVPFactory
      DT_UNSIGNED_64 -> Unsigned64AVPFactory
      DT_FLOAT_32 -> Float32AVPFactory
      DT_FLOAT_64 -> Float64AVPFactory
      DT_GROUPED -> GroupedAVPFactory
      DT_ADDRESS -> AddressAVPFactory
      DT_TIME -> TimeAVPFactory
      DT_UTF8STRING -> UTF8StringAVPFactory
      DT_DIAMETER_IDENTITY -> IdentityAVPFactory
      DT_DIAMETER_URI -> URIAVPFactory
      DT_ENUMERATED -> EnumeratedAVPFactory
      DT_IP_FILTER_RULE -> IPFilterRuleAVPFactory
      DT_QoS_FILTER_RULE -> QoSFilterRuleAVPFactory
      else -> GenericAVPFactory
    }

    fun create(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP {
      val dictData = getDictionaryData(avpCode, vendorId)
      return AVPFactory[dictData.dataType].createAVP(avpCode, flags, vendorId)
    }

    @Throws(DiameterDictionaryException::class)
    fun createAVPFromDictionary(avpCode: Long, vendorId: Long): DiameterAVP {
      val dictData = getDictionaryData(avpCode, vendorId)
      if (dictData.dataType == DT_UNKNOWN) {
        throw DiameterDictionaryException(DiameterException.DIAMETER_DICTIONARY_EXCEPTION, "No such AVP in dictionary")
      }
      val avp = AVPFactory[dictData.dataType].createAVP(dictData)
      avp.name = dictData.name
      return avp
    }
  }

  abstract fun createAVP(avpCode: Long, flags: Byte, vendorId: Long): DiameterAVP

  abstract fun createAVP(dictData: AVPDictionaryData): DiameterAVP
}
