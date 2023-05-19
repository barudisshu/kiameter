package com.cplier.diameter.packet.avp.derived

import com.cplier.diameter.packet.avp.basic.OctetStringAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

/**
 * The UTF8String format is derived from the OctetString Basic AVP Format. This is a human-readable
 * string represented using the ISO/IEC IS 10646-1 character set, encoded as an OctetString using
 * the UTF-8 transformation format [RFC3629](https://www.rfc-editor.org/rfc/rfc3629).
 *
 * Since additional code points are added by amendments to the 10646 standard from time to time,
 * implementations MUST be prepared to encounter any code point from 0x00000001 to 0x7fffffff. Byte
 * sequences that do not correspond to the valid encoding of a code point into UTF-8 charset or are
 * outside this range are prohibited.
 *
 * The use of control codes SHOULD be avoided. When it is necessary to represent a new line, the
 * control code sequence CR LF SHOULD be used.
 *
 * The use of leading or trailing white space SHOULD be avoided.
 *
 * For code points not directly supported by user interface hardware or software, an alternative
 * means of entry and display, such as hexadecimal, MAY be provided.
 *
 * For information encoded in 7-bit US-ASCII, the UTF-8 charset is identical to the US-ASCII
 * charset.
 *
 * UTF-8 may require multiple bytes to represent a single character / code point; thus, the
 * length of a UTF8String in octets may be different from the number of characters encoded.
 *
 * Note that the AVP Length field of an UTF8String is measured in octets not characters.
 */
class UTF8StringAVP : OctetStringAVP {

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)

  override fun decodeData(buffer: ByteBuffer, length: Int) {
    byteData = ByteArray(length)
    buffer[byteData]
    octetData = String(byteData, StandardCharsets.UTF_8)
    addDataLength(length)
  }
}
