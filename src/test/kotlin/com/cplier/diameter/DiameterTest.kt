package com.cplier.diameter

import com.cplier.diameter.exception.DiameterDictionaryException
import com.cplier.diameter.exception.DiameterParseException
import com.cplier.diameter.packet.avp.basic.GroupedAVP
import com.cplier.diameter.packet.avp.basic.Integer32AVP
import com.cplier.diameter.packet.avp.basic.Unsigned32AVP
import com.cplier.diameter.packet.avp.derived.EnumeratedAVP
import com.cplier.diameter.packet.avp.factory.AVPFactory
import com.cplier.diameter.packet.message.DiameterHeader
import com.cplier.diameter.packet.message.DiameterMessage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

class DiameterTest {

  companion object {
    private val LOGGER: Logger = LoggerFactory.getLogger(DiameterTest::class.java)
  }

  @Test
  @Throws(DiameterDictionaryException::class, DiameterParseException::class)
  fun `Capabilities-Exchange`() {
    ////////////////////////////////////
    /* How to create Diameter Message */
    ////////////////////////////////////

    // Method 1 - Create Header first and create the message using header.

    // A)
    val header1 = DiameterHeader(commandCode = COMMAND_CER_CEA, applicationId = APP_ID_DIAMETER_COMMON_MESSAGE)
    header1.request = true
    header1.proxiable = true
    val msg1 = DiameterMessage(header1)

    // B)
    val header2 = DiameterHeader(
      DIAMETER_VERSION,
      commandFlags = HEADER_FLAG_R_P,
      commandCode = COMMAND_STR_STA,
      applicationId = APP_ID_DIAMETER_COMMON_MESSAGE
    )
    val msg2 = DiameterMessage(header2)

    val msg3 = DiameterMessage.createMessage(HEADER_FLAG_R_P, COMMAND_STR_STA, APP_ID_DIAMETER_COMMON_MESSAGE)

    /////////////////////////////////////////////
    /* How to create AVP and add it to message */
    /////////////////////////////////////////////

    // Method 1 - Use created message to add your AVP
    msg1.addOctetStringAVP(AC_ORIGIN_REALM, AVP_FLAG_M, VENDOR_ID_NONE, "My_Realm_1111")
    msg1.addAVPFromDictionary(AC_SESSION_ID, VENDOR_ID_NONE, "My Session Id")
    msg1.addInteger32AVP(AC_PORT, AVP_FLAG_M, VENDOR_ID_NONE, 3868)
    msg1.addAddressAVP(AC_IP_ADDRESS, AVP_FLAG_NONE, VENDOR_ID_NONE, "127.0.0.1") // Accepts Inet Address too

    val hex = "52e636f6d0"
    val raw = hex2ByteArray(hex)
    msg1.addOctetStringAVP(AC_CC_SUB_SESSION_ID, AVP_FLAG_NONE, VENDOR_ID_NONE, raw)
    msg1.addUTF8StringAVP(AC_BORDER_ROUTER_NAME, AVP_FLAG_NONE, VENDOR_ID_NONE, "Router X")

    // For the Enumerated AVP values, look into ProtocolDefinitions.java,
    // I have tried to add values for common Enumerated AVPs. You can also simply
    // give the integer value of the AVP.
    // In the below example I have added Auth-Request-Type AVP with the value of
    // 3(Authorize_Authenticate).
    // I have tried to name Enumerated AVP values with initials of the AVP, for
    // example for the Disconnect-Cause AVP,
    // It's corresponding values are;
    // DC_REBOOTING = 0; /* [RFC6733] */
    // DC_BUSY = 1; /* [RFC6733] */
    // DC_DO_NOT_WANT_TO_TALK_TO_YOU = 2; /* [RFC6733] */
    msg1.addEnumeratedAVP(AC_AUTH_REQUEST_TYPE, AVP_FLAG_M, VENDOR_ID_NONE, ART_AUTHORIZE_AUTHENTICATE)
    // or
    val disconnectCause = EnumeratedAVP(AC_DISCONNECT_CAUSE, AVP_FLAG_M, VENDOR_ID_NONE)
    disconnectCause.setData(DC_BUSY)
    msg1.addAVP(disconnectCause)

    // Grouped AVP - Example of Group AVP with depth of 3 inner AVP
    val multipleServicesCreditControl = GroupedAVP(AC_MULTIPLE_SERVICES_CREDIT_CONTROL, AVP_FLAG_M, VENDOR_ID_NONE)
    val grantedServiceUnit = GroupedAVP(AC_GRANTED_SERVICE_UNIT, AVP_FLAG_M, VENDOR_ID_NONE)

    val ccTime = Unsigned32AVP(AC_CC_TIME, AVP_FLAG_M, VENDOR_ID_NONE)
    ccTime.setData(10)

    grantedServiceUnit.addAVP(ccTime)
    multipleServicesCreditControl.addAVP(grantedServiceUnit)
    msg1.addAVP(multipleServicesCreditControl)

    // Method 2 - Create AVP first, and add it to the message

    // A) - Throws error if there is no such avp in dictionary
    val avp1 = AVPFactory.createAVPFromDictionary(AC_EAP_PAYLOAD, VENDOR_ID_NONE)
    val hex2 = "000000000000000000000000000000000000000000000001"
    val eapPayload = hex2ByteArray(hex2)
    avp1.setData(eapPayload)  // You can set data as byte array
    msg1.addAVP(avp1)

    // B)
    val avp2 = AVPFactory.create(AC_RESULT_CODE, AVP_FLAG_NONE, VENDOR_ID_NONE)
    avp2.setData("2") // or you can set data as string and it will convert it to actual data format according to AVP data type
    msg1.addAVP(avp2)

    // C)
    val avp3 = Integer32AVP(AC_CC_REQUEST_TYPE, AVP_FLAG_NONE, VENDOR_ID_NONE)
    avp3.setData(1)
    msg1.addAVP(avp3)

    Assertions.assertEquals(12, msg1.avpList.size)
    val packet = msg1.encodePacket()  // Ready to send
    assertEquals(236, packet.size)

    // Now assume we received a message, how to parse it ?
    val hexData = "010000f40000010100000000000d8d6b9b247e6b0000010c4000000c000007d100000108400000134e50434469616d657465720000000128400000124f70656e536f757263650000000001014000000e00017f00000100000000010ac000001000000000000028af0000010d400000134e70634469616d6574657200000001094000000c000028af00000104400000200000010a4000000c000028af000001024000000c0100003000000104400000200000010a4000000c000028af000001024000000c0100002200000104400000200000010a4000000c000028af000001024000000c010000380000010b0000000c00000001"
    val rawData = hex2ByteArray(hexData)  // rawData is the data we will receive from socket
    val message = DiameterMessage.decodePacket(rawData)


  }
}












































