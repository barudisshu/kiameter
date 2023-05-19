package com.cplier.diameter.packet.avp.derived

import com.cplier.diameter.RC_DIAMETER_INVALID_AVP_LENGTH
import com.cplier.diameter.ZERO_LONG
import com.cplier.diameter.exception.DiameterParseException
import com.cplier.diameter.packet.avp.basic.OctetStringAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import com.cplier.diameter.readUint32
import java.nio.ByteBuffer

/**
 * The Time format is derived from the OctetString Basic AVP Format. The string MUST contain four
 * octets, in the same format as the first four bytes are in the NTP timestamp format. The NTP
 * timestamp format is defined in [Section 3 of [RFC5905]](https://www.rfc-editor.org/rfc/rfc5905#section-3)
 *
 * This represents the number of seconds since 0h on 1 January 1900 with respect to the
 * Coordinated Universal Time (UTC).
 *
 * On 6h 28m 16s UTC, 7 February 2036, the time value will overflow. Simple Network Time Protocol
 * (SNTP) [RFC5905](https://www.rfc-editor.org/rfc/rfc5905) describes a procedure to
 * extend the time to 2104. This procedure MUST be supported by all Diameter nodes.
 */
class TimeAVP : OctetStringAVP {

  companion object {
    const val NTP_TIME_BEGIN = -2208988800000L
    const val NTP_TIME_END = 2085978496000L
  }

  private var timeData: Long = 0

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)

  override fun encodeData(buffer: ByteBuffer) {
    var time: Long = if (timeData < NTP_TIME_END) timeData - NTP_TIME_BEGIN else timeData - NTP_TIME_END
    time = ((time / 1000) and 0xffffffffL)
    buffer.putInt(time.toInt())
  }

  override fun decodeData(buffer: ByteBuffer, length: Int) {
    if (length != 4) {
      throw DiameterParseException(RC_DIAMETER_INVALID_AVP_LENGTH, "Wrong length for Time data")
    }
    val encodedTime = readUint32(buffer)
    timeData = encodedTime * 1000

    timeData += if ((encodedTime and 0x80000000L) == ZERO_LONG) {
      NTP_TIME_BEGIN
    } else {
      NTP_TIME_END
    }
    addDataLength(length)
  }
}
