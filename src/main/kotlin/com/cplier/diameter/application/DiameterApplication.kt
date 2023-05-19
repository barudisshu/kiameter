package com.cplier.diameter.application

import com.cplier.diameter.packet.message.DiameterMessage

interface DiameterApplication {

  fun receiveMessage(stack: DiameterStack, message: DiameterMessage)

  fun onConnectionSuccess(localAddress: String, localPort: Int, remoteAddress: String, remotePort: Int)

  fun onConnectionFail(hostAddress: String, remotePort: Int)

  fun onSendMessage()

  fun onDisconnect(result: Int)
}
