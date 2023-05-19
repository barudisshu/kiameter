package com.cplier.diameter.packet.avp.dictionary

class GroupedAVPDictionaryData(name: String, code: Long, flags: Byte, vendorId: Long, dataType: Int) :
  AVPDictionaryData(name, code, flags, vendorId, dataType) {

  var avpList: Map<AVPKey, AVPOccurrence> = mutableMapOf()
  var containsAny: Boolean = false
  var minAny: Int = 0

  init {
    containsAny = false
  }

  fun add(code: Long, vendorId: Long, occType: Int, min: Int, max: Int) {
    val key = AVPKey(code, vendorId)
    val occurrence = AVPOccurrence(min, max, occType)
    avpList + (key to occurrence)
  }

  fun containsAny(any: Boolean, minAny: Int) {
    this.containsAny = any
    this.minAny = minAny
  }
}
