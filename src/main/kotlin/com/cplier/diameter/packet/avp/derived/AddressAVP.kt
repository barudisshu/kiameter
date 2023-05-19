package com.cplier.diameter.packet.avp.derived

import com.cplier.diameter.ADDRESS_TYPE_IPv4
import com.cplier.diameter.ADDRESS_TYPE_IPv6
import com.cplier.diameter.exception.DiameterRuntimeException
import com.cplier.diameter.packet.avp.basic.OctetStringAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteBuffer

/**
 * The Address format is derived from the OctetString Basic AVP Format. It is a discriminated union representing, for example, a 32-bit(IPv4) [RFC0791](https://www.rfc-editor.org/rfc/rfc0791) or 128-bit (IPv6) [RFC4291](https://www.rfc-editor.org/rfc/rfc4291) address, most significant octet first.
 *
 * The first two octets of the Address AVP represent the AddressType, which contains an Address Family, defined in [IANAADFAM](https://www.rfc-editor.org/rfc/rfc6733.html#ref-IANAADFAM). The AddressType is used to discriminate the content and format of the remaining octets.
 */
class AddressAVP : OctetStringAVP {

  companion object {
    private const val IPv4_LENGTH_BITS = 6
    private const val IPv6_LENGTH_BITS = 18
  }

  var addressType: Short? = null
  var address: InetAddress? = null

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)


  override fun decodeData(buffer: ByteBuffer, length: Int) {
    addDataLength(length)
    addressType = buffer.getShort()
    val addr = ByteArray(length - 2)
    buffer.get(addr)
    try {
      this.address = InetAddress.getByAddress(addr)
    } catch (e: UnknownHostException) {
      throw DiameterRuntimeException("fail to decode inet address", e)
    }
    byteData = addr
  }

  fun setData(data: InetAddress) {
    when (data) {
      is Inet4Address -> {
        this.addressType = ADDRESS_TYPE_IPv4
        this.address = data
        byteData = ByteArray(IPv4_LENGTH_BITS)
        byteData[1] = ADDRESS_TYPE_IPv4.toByte()
        System.arraycopy(data.address, 0, byteData, 2, 4)
        addDataLength(IPv4_LENGTH_BITS)
      }

      is Inet6Address -> {
        this.addressType = ADDRESS_TYPE_IPv6
        this.address = data
        byteData = ByteArray(IPv6_LENGTH_BITS)
        byteData[1] = ADDRESS_TYPE_IPv6.toByte()
        System.arraycopy(data.address, 0, byteData, 2, 16)
        addDataLength(IPv6_LENGTH_BITS)
      }

      else -> throw DiameterRuntimeException("unknown/unsupported ip address")
    }
  }

  override fun setData(data: String) {
    this.octetData = data
    try {
      val addr = InetAddress.getByName(data)
      setData(addr)
    } catch (e: UnknownHostException) {
      throw DiameterRuntimeException("fail to parse address form data", e)
    }
  }
}
