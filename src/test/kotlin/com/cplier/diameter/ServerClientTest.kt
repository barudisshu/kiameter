package com.cplier.diameter

import com.cplier.diameter.application.DiameterApplication
import com.cplier.diameter.application.DiameterStack
import com.cplier.diameter.application.client.DiameterClient
import com.cplier.diameter.application.server.DiameterServer
import com.cplier.diameter.exception.DiameterDictionaryException
import com.cplier.diameter.packet.avp.basic.GroupedAVP
import com.cplier.diameter.packet.avp.basic.Unsigned32AVP
import com.cplier.diameter.packet.message.DiameterMessage
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class ServerClientTest {

  @Test
  fun `Capabilities-Exchange`() {
    val serverApplication = SimpleServerApplication()
    val diameterServer = DiameterServer(serverApplication, "127.0.0.1", 3868) // Local Address to bind
    diameterServer.start() // Start Server

    val clientApplication = SimpleClientApplication()
    val diameterClient = DiameterClient(clientApplication, "127.0.0.1", 3868) // Remote address to send
    diameterClient.bindToLocalAddress("127.0.0.1", 3870)  // Optional, if no set, it will bind to random local port

    // Creating message to sent
    val message = DiameterMessage.createMessage(HEADER_FLAG_R_P, COMMAND_CER_CEA, APP_ID_3GPP_S6t)

    message.addIdentityAVP(AC_ORIGIN_HOST, AVP_FLAG_V_M, VENDOR_ID_NONE, "NPCDiameter")
    message.addIdentityAVP(AC_ORIGIN_REALM, AVP_FLAG_M, VENDOR_ID_NONE, "OpenSource")
    message.addAddressAVP(AC_HOST_IP_ADDRESS, AVP_FLAG_M, VENDOR_ID_NONE, "127.0.0.1")
    message.addUnsigned32AVP(AC_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_NONE, VENDOR_ID_3GPP)
    message.addIdentityAVP(AC_PRODUCT_NAME, AVP_FLAG_M, VENDOR_ID_NONE, "NpcDiameter")
    message.addUnsigned32AVP(AC_SUPPORTED_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_NONE, VENDOR_ID_3GPP)

    val vendorSpecificApplicationId = GroupedAVP(AC_VENDOR_SPECIFIC_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
    val vendorId = Unsigned32AVP(AC_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_3GPP)
    vendorId.setData(VENDOR_ID_3GPP)
    vendorSpecificApplicationId.addAVP(vendorId)
    val acAuthAppId = Unsigned32AVP(AC_AUTH_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
    acAuthAppId.setData(APP_ID_3GPP_SWm)
    vendorSpecificApplicationId.addAVP(acAuthAppId)

    message.addAVP(vendorSpecificApplicationId)

    val vendorSpecificApplicationId1 = GroupedAVP(AC_VENDOR_SPECIFIC_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
    val vendorId1 = Unsigned32AVP(AC_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_NONE)
    vendorId1.setData(VENDOR_ID_3GPP)
    vendorSpecificApplicationId1.addAVP(vendorId1)
    val acAuthAppId1 = Unsigned32AVP(AC_AUTH_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
    acAuthAppId1.setData(APP_ID_3GPP_Sta)
    vendorSpecificApplicationId1.addAVP(acAuthAppId1)

    message.addAVP(vendorSpecificApplicationId1)

    val vendorSpecificApplicationId2 = GroupedAVP(AC_VENDOR_SPECIFIC_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
    val vendorId2 = Unsigned32AVP(AC_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_NONE)
    vendorId2.setData(VENDOR_ID_ERICSSON)
    vendorSpecificApplicationId2.addAVP(vendorId2)
    val acAuthAppId2 = Unsigned32AVP(AC_AUTH_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
    acAuthAppId2.setData(APP_ID_3GPP_S6t)
    vendorSpecificApplicationId2.addAVP(acAuthAppId2)
    message.addAVP(vendorSpecificApplicationId2)

    message.addAVPFromDictionary(AC_FIRMWARE_REVISION, VENDOR_ID_NONE, "1")

    val packet = message.encodePacket() // Ready to send
    assertEquals(232, packet.size)
    diameterClient.send(packet)

    TimeUnit.SECONDS.sleep(3L)

    diameterServer.destroy()
    diameterClient.destroy()

    assertEquals(10, message.avpList.size)
  }

  internal class SimpleServerApplication : DiameterApplication {

    companion object {
      private val LOGGER: Logger = LoggerFactory.getLogger(SimpleServerApplication::class.java)

    }

    override fun receiveMessage(stack: DiameterStack, message: DiameterMessage) {
      LOGGER.info("Message Received from Server Application")
      // Creating message to sent
      val response = DiameterMessage.createMessage(HEADER_FLAG_P, COMMAND_CER_CEA, APP_ID_3GPP_S6t)
      try {
        response.addUnsigned32AVP(AC_RESULT_CODE, AVP_FLAG_M, VENDOR_ID_NONE, RC_DIAMETER_SUCCESS)

        response.addIdentityAVP(AC_ORIGIN_HOST, AVP_FLAG_M, VENDOR_ID_NONE, "NPCDiameter")
        response.addIdentityAVP(AC_ORIGIN_REALM, AVP_FLAG_M, VENDOR_ID_NONE, "OpenSource")
        response.addAddressAVP(AC_HOST_IP_ADDRESS, AVP_FLAG_M, VENDOR_ID_NONE, "127.0.0.1")
        response.addUnsigned32AVP(AC_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_NONE, VENDOR_ID_3GPP)
        response.addIdentityAVP(AC_PRODUCT_NAME, AVP_FLAG_M, VENDOR_ID_NONE, "NpcDiameter")
        response.addUnsigned32AVP(AC_SUPPORTED_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_NONE, VENDOR_ID_3GPP)

        val vendorSpecificApplicationId = GroupedAVP(AC_VENDOR_SPECIFIC_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
        val vendorId = Unsigned32AVP(AC_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_NONE)
        vendorId.setData(VENDOR_ID_3GPP)
        vendorSpecificApplicationId.addAVP(vendorId)
        val acAuthAppId = Unsigned32AVP(AC_AUTH_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
        acAuthAppId.setData(APP_ID_3GPP_SWm)
        vendorSpecificApplicationId.addAVP(acAuthAppId)

        response.addAVP(vendorSpecificApplicationId)

        val vendorSpecificApplicationId1 = GroupedAVP(AC_VENDOR_SPECIFIC_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
        val vendorId1 = Unsigned32AVP(AC_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_NONE)
        vendorId1.setData(VENDOR_ID_3GPP)
        vendorSpecificApplicationId1.addAVP(vendorId1)
        val acAuthAppId1 =
          Unsigned32AVP(AC_AUTH_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
        acAuthAppId1.setData(APP_ID_3GPP_Sta)
        vendorSpecificApplicationId1.addAVP(acAuthAppId1)

        response.addAVP(vendorSpecificApplicationId1)

        val vendorSpecificApplicationId2 =
          GroupedAVP(AC_VENDOR_SPECIFIC_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
        val vendorId2 = Unsigned32AVP(AC_VENDOR_ID, AVP_FLAG_M, VENDOR_ID_NONE)
        vendorId2.setData(VENDOR_ID_ERICSSON)
        vendorSpecificApplicationId2.addAVP(vendorId2)
        val acAuthAppId2 =
          Unsigned32AVP(AC_AUTH_APPLICATION_ID, AVP_FLAG_M, VENDOR_ID_NONE)
        acAuthAppId2.setData(APP_ID_3GPP_S6t)
        vendorSpecificApplicationId2.addAVP(acAuthAppId2)
        response.addAVP(vendorSpecificApplicationId2)

        response.addAVPFromDictionary(AC_FIRMWARE_REVISION, VENDOR_ID_NONE, "1")
      } catch (e: DiameterDictionaryException) {
        LOGGER.error(e.message)
      }
      stack.sendMessage(response.encodePacket())
    }

    override fun onConnectionSuccess(
      localAddress: String,
      localPort: Int,
      remoteAddress: String,
      remotePort: Int
    ) {
      LOGGER.info("onConnectionSuccess")
    }

    override fun onConnectionFail(hostAddress: String, remotePort: Int) {
      LOGGER.info("onConnectionFail")
    }

    override fun onSendMessage() {
      LOGGER.info("onSendMessage")
    }

    override fun onDisconnect(result: Int) {
      LOGGER.info("onDisconnect")
    }
  }

  internal class SimpleClientApplication : DiameterApplication {

    companion object {
      private val LOGGER: Logger = LoggerFactory.getLogger(SimpleClientApplication::class.java)
    }

    override fun receiveMessage(stack: DiameterStack, message: DiameterMessage) {
      LOGGER.info("Message Received from client Application")
    }

    override fun onConnectionSuccess(
      localAddress: String,
      localPort: Int,
      remoteAddress: String,
      remotePort: Int
    ) {
      LOGGER.info("onConnectionSuccess")
    }

    override fun onConnectionFail(hostAddress: String, remotePort: Int) {
      LOGGER.info("onConnectionFail")
    }

    override fun onSendMessage() {
      LOGGER.info("onSendMessage")
    }

    override fun onDisconnect(result: Int) {
      LOGGER.info("onDisconnect")
    }
  }
}
