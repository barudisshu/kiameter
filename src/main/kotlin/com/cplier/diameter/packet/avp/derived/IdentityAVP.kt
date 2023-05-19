package com.cplier.diameter.packet.avp.derived

import com.cplier.diameter.packet.avp.basic.OctetStringAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData

/**
 * The DiameterIdentity format is derived from the OctetString Basic AVP Format.
 * ```
 *        DiameterIdentity = FQDN/Realm
 * ```
 * The DiameterIdentity value is used to uniquely identify either:
 *
 * - A Diameter node for purposes of duplicate connection and routing loop detection.
 * - A Realm to determine whether messages can be satisfied locally or whether they must be routed or redirected.
 *
 * When a DiameterIdentity value is used to identify a Diameter node, the contents of the string
 * MUST be the Fully Qualified Domain Name (FQDN) of the Diameter node. If multiple Diameter nodes
 * run on the same host, each Diameter node MUST be assigned a unique DiameterIdentity. If a
 * Diameter node can be identified by several FQDNs, a single FQDN should be picked at startup and
 * used as the only DiameterIdentity for that node, whatever the connection on which it is sent. In
 * this document, note that DiameterIdentity is in ASCII form in order to be compatible with
 * existing DNS infrastructure. See [Appendix D](https://www.rfc-editor.org/rfc/rfc6733.html#appendix-D) for interactions
 * between the Diameter protocol and Internationalized Domain Names (IDNs).
 */
class IdentityAVP : OctetStringAVP {

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)
}
