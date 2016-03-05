package redis

import java.net.URI
import org.apache.commons.pool.impl.GenericObjectPool.Config
import redis.clients.jedis.{Protocol, JedisPool, Jedis}

/**
  * Created by Phyrex on 2016/3/5.
  */
object RedisService {
  private val redisURI : URI = new URI(System.getenv("REDIS_URL"))
  private val redisPassword: Option[String] = Option(redisURI.getUserInfo).map(_.split(":").apply(1))
  //lazy val redisPool: JedisPool = createRedisPool()
  def connection : Jedis = {
    val jedis : Jedis = new Jedis(redisURI.getHost, redisURI.getPort)
    for (p <- redisPassword) {
      jedis.auth(p)
    }
    jedis
  }
  /*def createRedisPool(): JedisPool = {
    val config = new Config()
    config.testOnBorrow = true
    redisPassword match {
      case Some(password) => new JedisPool(config, redisURI.getHost, redisURI.getPort, Protocol.DEFAULT_TIMEOUT, password)
      case None => new JedisPool(config, redisURI.getHost, redisURI.getPort, Protocol.DEFAULT_TIMEOUT, null)
    }
  }*/
}