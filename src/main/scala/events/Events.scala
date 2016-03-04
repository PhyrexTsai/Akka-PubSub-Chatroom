package events

/**
  * Created by Phyrex on 2016/3/3.
  */
object Events {
  sealed trait Message
  case class ChatMessage(sender: String, message: String) extends Message
  case class Joined(member: String, allMembers: Seq[String]) extends Message
  case class Leaved(member: String, allMembers: Seq[String]) extends Message

  // 把 Events.Message 轉成字串輸出
  def parse(msg : Events.Message) : String = {
    println(msg.toString)
    val msgs : Array[String] = msg.toString.split("\\(")
    var event = "";
    var message = "";
    var sender = "";
    var members = "";
    if(msgs.length > 0){
      event = msgs.apply(0)
      sender = msgs.apply(1).split(",").apply(0)
      message = msgs.apply(1).split(",").apply(1)
    }
    if(msgs.length > 2){
      members = ", \"member\" : [\"" + msgs.apply(2).replace(")", "").replace(", ", "\",\"") + "\"]"
    }
    event match {
      case "Joined" => message = s"$sender 加入了"
      case "Leaved" => message = s"$sender 離開了"
      case "ChatMessage" => message = message.substring(0, message.length - 1)//.replace("\n", "<br/>")
    }
    "{\"sender\" : \"" + sender + "\", \"message\" : \"" + message + "\", \"type\" : \"" + event + "\"" + members + "}"
  }
}