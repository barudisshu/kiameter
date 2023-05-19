package com.cplier.diameter.packet.avp.derived

import com.cplier.diameter.packet.avp.basic.OctetStringAVP
import com.cplier.diameter.packet.avp.dictionary.AVPDictionaryData

/**
 * The DiameterURI MUST follow the Uniform Resource Identifiers ([RFC 3986](https://www.rfc-editor.org/rfc/rfc3986)) syntax [RFC3986](https://www.rfc-editor.org/rfc/rfc3986) rules specified below:
 *
 * ```bash
 * "aaa://" FQDN [ port ] [ transport ] [ protocol ]
 *                 ; No transport security
 * "aaas://" FQDN [ port ] [ transport ] [ protocol ]
 *                 ; Transport security used
 * FQDN               = < Fully Qualified Domain Name >
 * port               = ":" 1*DIGIT
 *                 ; One of the ports used to listen for
 *                 ; incoming connections.
 *                 ; If absent, the default Diameter port
 *                 ; (3868) is assumed if no transport
 *                 ; security is used and port 5658 when
 *                 ; transport security (TLS/TCP and DTLS/SCTP)
 *                 ; is used.
 * transport          = ";transport=" transport-protocol
 *                 ; One of the transports used to listen
 *                 ; for incoming connections.  If absent,
 *                 ; the default protocol is assumed to be TCP.
 *                 ; UDP MUST NOT be used when the aaa-protocol
 *                 ; field is set to diameter.
 * transport-protocol = ( "tcp" / "sctp" / "udp" )
 * protocol           = ";protocol=" aaa-protocol
 *                 ; If absent, the default AAA protocol
 *                 ; is Diameter.
 * aaa-protocol       = ( "diameter" / "radius" / "tacacs+" )
 * The following are examples of valid Diameter host identities:
 * aaa://host.example.com;transport=tcp
 * aaa://host.example.com:6666;transport=tcp
 * aaa://host.example.com;protocol=diameter
 * aaa://host.example.com:6666;protocol=diameter
 * aaa://host.example.com:6666;transport=tcp;protocol=diameter
 * aaa://host.example.com:1813;transport=udp;protocol=radius
 * ```
 */
class URIAVP : OctetStringAVP {

  constructor(avpCode: Long, flags: Byte, vendorId: Long) : super(avpCode, flags, vendorId)

  constructor(dictData: AVPDictionaryData) : super(dictData)
}
