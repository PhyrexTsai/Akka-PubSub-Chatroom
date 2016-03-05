package redis

import java.net.URI
import redis.clients.jedis.Jedis

/**
  * Created by Phyrex on 2016/3/5.
  */
object RedisService {
  private val redisURI : URI = new URI(System.getenv("REDIS_URL"));
  private val jedis : Jedis = new Jedis(redisURI.getHost, redisURI.getPort)
  def connection : Jedis = {
    jedis
  }
}