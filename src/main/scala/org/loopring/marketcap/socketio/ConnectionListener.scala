package org.loopring.marketcap.socketio

import com.corundumstudio.socketio.SocketIOClient
import org.slf4j.LoggerFactory

class ConnectionListener extends com.corundumstudio.socketio.listener.ConnectListener {

  lazy val logger = LoggerFactory.getLogger(getClass)

  override def onConnect(client: SocketIOClient): Unit = {
    logger.info(s"SocketIO: client ${client.getRemoteAddress.toString} connected")
  }

}

class DisconnectionListener extends com.corundumstudio.socketio.listener.DisconnectListener {

  lazy val logger = LoggerFactory.getLogger(getClass)

  override def onDisconnect(client: SocketIOClient): Unit = {
    logger.info(s"SocketIO: client ${client.getRemoteAddress.toString} disconnected")
  }
}
