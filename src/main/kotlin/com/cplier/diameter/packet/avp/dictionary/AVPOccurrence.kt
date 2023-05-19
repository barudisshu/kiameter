package com.cplier.diameter.packet.avp.dictionary

/**
 * Basically, AVP Occurrence Table specification use to defined custom dictionary to verify failed reason.
 */
data class AVPOccurrence(var min: Int, var max: Int, var occType: Int) {

  var avpName: String? = null

  companion object {
    const val AVPOCC_OPTIONAL = 0
    const val AVPOCC_MANDATORY = 1
    const val AVPOCC_FIXED = 2
  }
}
