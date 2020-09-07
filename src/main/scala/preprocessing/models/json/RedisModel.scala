package preprocessing.models.json

trait RedisModel {

  def toMap: Map[String, String]
}
