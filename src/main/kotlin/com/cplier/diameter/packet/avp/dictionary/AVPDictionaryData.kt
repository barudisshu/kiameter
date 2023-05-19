package com.cplier.diameter.packet.avp.dictionary

open class AVPDictionaryData(
  val name: String,
  val code: Long,
  val flags: Byte,
  val vendorId: Long,
  val dataType: Int
)
