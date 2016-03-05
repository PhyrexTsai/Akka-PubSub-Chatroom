package events

import java.util.Date

/**
  * Created by Phyrex on 2016/3/3.
  */
object Events {
  sealed trait Message
  case class ChatMessage(sender: String, message: String) extends Message
  case class Joined(member: String, allMembers: String) extends Message
  case class Leaved(member: String, allMembers: String) extends Message

  // 把 Events.Message 轉成字串輸出
  def parse(msg : Events.Message) : String = {
    println(msg.toString)
    val messageTypeTick = "^([A-Z]\\w+)".r
    var messageType = ""
    var sender = ""
    var message = ""
    var members = ""
    messageTypeTick findFirstIn msg.toString match {
      case Some(messageTypeTick(inside)) => messageType = inside //println(inside)
      case _ => println("nothing")
    }
    val s1 = msg.toString.substring(messageType.length)

    val senderTick = "(\\(\\w+,)".r
    var len = 0
    senderTick findFirstIn s1 match {
      case Some(senderTick(inside)) => {
        len = inside.length
        sender = inside.substring(1, inside.length - 1)
      }
      case _ => println("nothing")
    }
    val messageJson = s1.substring(len, s1.length - 1)
    val time = (new Date()).getTime()
    messageType match {
      case "Joined" => message = "{\"sender\" : \"" + sender + "\", \"message\" : \"" + sender + " 加入了\", \"messageType\" : \"Joined\", \"time\" : " + time + ", \"members\" : " + messageJson + "}"
      case "Leaved" => message = "{\"sender\" : \"" + sender + "\", \"message\" : \"" + sender + " 離開了\", \"messageType\" : \"Leaved\", \"time\" : " + time + ", \"members\" : " + messageJson + "}"
      case "ChatMessage" => message = messageJson
      case "SnapMessage" => message = messageJson
      case _ => message = "{\"sender\" : \"" + sender + "\", \"message\" : \"\", \"messageType\" : \"ChatMessage\", \"time\" : " + time + "}"
    }
    println("return : " + message)
    message
  }
}