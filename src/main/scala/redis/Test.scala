package redis

/**
  * Created by Phyrex on 2016/3/6.
  */
object Test {
  def main(args : Array[String]) = {
    RedisService.connection.set("hi", "....")
    println(RedisService.connection.get("hi"))
  }
}
