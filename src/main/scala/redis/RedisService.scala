package redis

import java.net.URI
import redis.clients.jedis.Jedis

/**
  * reference : https://github.com/sclasen/play-extras/blob/master/src/main/scala/com/heroku/play/api/libs/redis/RedisService.scala
  * Created by Phyrex on 2016/3/5.
  */
object RedisService {
  private val redisURI : URI = new URI(System.getenv("REDIS_URL"))
  private val redisPassword: Option[String] = Option(redisURI.getUserInfo).map(_.split(":").apply(1))
  private val jedis : Jedis = new Jedis(redisURI.getHost, redisURI.getPort)
  def connection : Jedis = {
    for (p <- redisPassword) {
      jedis.auth(p)
    }
    jedis
  }
}