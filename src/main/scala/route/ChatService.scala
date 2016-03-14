package route

import akka.http.scaladsl.model.ws.{BinaryMessage, TextMessage, Message}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.scaladsl.Flow
import chat.ChatHandler
import events.Events

/**
  * Created by yjtsai on 2016/3/3.
  */
object ChatService {
  def route(chatroom: ChatHandler) : Route = path("chat") {
    parameters('name, 'topic) { (name, topic) => {
      handleWebSocketMessages(websocketChatFlow(chatroom, topic, name))
    }}
  }

  // 這邊使用到 PartialFunction, 包含 collect 與 map
  // 參考 API : http://www.scala-lang.org/api/current/index.html#scala.PartialFunction
  def websocketChatFlow(chatroom : ChatHandler, topic : String, sender: String): Flow[Message, Message, Any] =
    Flow[Message]
      .collect({
        case TextMessage.Strict(msg) => msg
      })
      .via(chatroom.chatFlow(topic, sender))
      .map({
        case msg : Events.Message => {
          TextMessage.Strict(Events parse msg)
        }
      })

}
