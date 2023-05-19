package com.cplier.diameter.packet.avp

import com.cplier.diameter.*
import com.cplier.diameter.exception.DiameterParseException
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

/**
 * A group of Diameter AVP *members*.
 *
 * ```txt
 * AVP Header:
 *   0                   1                   2                   3
 *   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  |                           AVP Code                            |
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  |V M P r r r r r|                  AVP Length                   |
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  |                        Vendor-ID (opt)                        |
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  |    Data ...
 *  +-+-+-+-+-+-+-+-+
 *
 * ```
 *
 *
 * @property code The AVP Code, combined with the Vendor-Id field, identifies the attribute uniquely. AVP numbers 1 through 255 are reserved for reuse of RADIUS attributes, without setting the Vendor-Id field. AVP numbers 256 and above are used for Diameter, which are allocated by [IANA](https://www.rfc-editor.org/rfc/rfc6733.html#section-11.1.1)
 * @property flags The AVP Flags field informs the receiver how each attribute must be handled. New Diameter applications SHOULD NOT define additional AVP Flag bits. However, note that new Diameter applications MAY define additional bits within the AVP header, and an unrecognized bit SHOULD be considered an error. The sender of the AVP MUST set 'R' (reserved) bits to 0 and the receiver SHOULD ignore all 'R' (reserved) bits. The 'P' bit has been reserved for future usage of end-to-end security. At the time of writing, there are no end-to-end security mechanisms specified; therefore, the 'P' bit SHOULD be set to 0. <p>The 'M' bit, known as the Mandatory bit, indicates whether the receiver of the AVP MUST parse and understand the semantics of the AVP including its content. The receiving entity MUST return an appropriate error message if it receives an AVP that has the M-bit set but does not understand it. An exception applies when the AVP is embedded within a Grouped AVP. See <a href="https://www.rfc-editor.org/rfc/rfc6733.html#section-4.4">Section 4.4</a> for details. Diameter relay and redirect agents MUST NOT reject messages with unrecognized AVPs.<p>The 'M' bit MUST be set according to the rules defined in the application specification that introduces or reuses this AVP. Within a given application, the M-bit setting for an AVP is defined either for all command types or for each command type. <p>AVPs with the 'M' bit cleared are informational only; a receiver that receives a message with such an AVP that is not supported, or whose value is not supported, MAY simply ignore the AVP.<p>The 'V' bit, known as the Vendor-Specific bit, indicates whether the optional Vendor-ID field is present in the AVP header. When set, the AVP Code belongs to the specific vendor code address space.
 *
 * @property avpLength The AVP Length field is three octets, and indicates the number of octets in this AVP including the AVP Code field, AVP Length, AVP Flags field, Vendor-ID field (if present), and the AVP Data field. If a message is received with an invalid attribute length, the message MUST be rejected.
 * @property vendorId The Vendor-ID field is present if the 'V' bit is set in the AVP Flags field. The optional four-octet Vendor-ID field contains the IANA-assigned "SMI Network Management Private Enterprise Codes" [ENTERPRISE](https://www.rfc-editor.org/rfc/rfc6733.html#ref-ENTERPRISE) value, encoded in network byte order. Any vendors or standardization organizations that are also treated like vendors in the IANA-managed "SMI Network Management Private Enterprise Codes" space wishing to implement a vendor-specific Diameter AVP MUST use their own Vendor-ID along with their privately managed AVP address space, guaranteeing that they will not collide with any other vendor's vendor-specific AVP(s) or with future IETF AVPs. A Vendor-ID value of zero (0) corresponds to the IETF-adopted AVP values, as managed by IANA. Since the absence of the Vendor-ID field implies that the AVP in question is not vendor specific, implementations MUST NOT use the value of zero (0) for the Vendor-ID field.
 * @property dataLength AVP data length.
 * @property byteData AVP data.
 * @property name AVP name.
 * @property vendorSpecific Indicates whether the optional Vendor-ID field is present in the AVP header.
 * @property mandatory Indicates whether support of the AVP is required. If an AVP with the 'M' bit set is received by a Diameter client, server, proxy, or translation agent and either the AVP or its value is unrecognized, the message MUST be rejected. Diameter Relay and redirect agents MUST NOT reject messages with unrecognized AVPs.
 * @property protected the 'P' bit indicates the need for encryption for end-to-end security. At the time of writing, there's no end-to-end security mechanisms specified; Therefore, it SHOULD be set to 0.
 *
 */
abstract class DiameterAVP(
  var code: Long,
  var flags: Byte,
  var vendorId: Long = VENDOR_ID_NONE,
  var avpLength: Int = if (isVendorSpecific(flags)) AVP_HDR_LEN_WITH_VENDOR else AVP_HDR_LEN_WITHOUT_VENDOR,
  var byteData: ByteArray = ByteArray(0)
) {

  var dataLength: Int = 0
  var name: String? = null

  var mandatory: Boolean
    get() = (flags and AVP_MASK_BIT_M) != ZERO_BYTE
    set(value) = if (value) flags = flags or AVP_MASK_BIT_M else flags = flags and AVP_MASK_BIT_M.inv()

  var vendorSpecific: Boolean
    get() = (flags and AVP_MASK_BIT_V) != ZERO_BYTE
    set(value) = if (value) flags = flags or AVP_MASK_BIT_V else flags = flags and AVP_MASK_BIT_V.inv()

  var protected: Boolean
    get() = (flags and AVP_MASK_BIT_P) != ZERO_BYTE
    set(value) = if (value) flags = flags or AVP_MASK_BIT_P else flags = flags and AVP_MASK_BIT_P.inv()

  constructor(dictData: AVPDictionaryData) : this(dictData.code, dictData.flags, vendorId = dictData.vendorId)


  abstract fun setData(data: String)

  fun setData(data: ByteArray) {
    this.byteData = data
    this.dataLength = data.size
    this.avpLength += this.dataLength
  }

  abstract fun encodeData(buffer: ByteBuffer)

  @Throws(DiameterParseException::class)
  abstract fun decodeData(buffer: ByteBuffer, length: Int)

  @Throws(DiameterParseException::class)
  fun encode(buffer: ByteBuffer) {
    writeUint32(buffer, code)
    writeUint8(buffer, flags)
    writeUint24(buffer, avpLength)
    if (vendorSpecific) {
      writeUint32(buffer, vendorId)
    }
    encodeData(buffer)
    buffer.position(buffer.position() + calculatePadding(avpLength))
  }

  fun addDataLength(length: Int) {
    dataLength = length
    avpLength += dataLength
  }

  companion object {
    fun isReserved(flags: Byte): Boolean = (flags and AVP_MASK_RESERVED) != ZERO_BYTE
    fun isVendorSpecific(flags: Byte): Boolean = (flags and AVP_MASK_BIT_V) != ZERO_BYTE
  }
}
