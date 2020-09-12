package preprocessing.models.json

case class Review(review_id: String,
                  user_id: String,
                  business_id: String,
                  stars: Option[String],
                  useful: Option[String],
                  // funny: Option[String],
                  // cool: Option[String],
                  text: Option[String],
                  date: Option[String]) extends RedisModel with MemcachedModel {

  override def toMap: Map[String, String] = Map(
    "user_id" -> Some(user_id),
    "business_id" -> Some(business_id),
    "stars" -> stars,
    "date" -> date,
    "text" -> text,
    "useful" -> useful
  ).filter(_._2.isDefined).mapValues(_.get)

  override def allToMap: Map[String, String] = Map(
    "user_id" -> Some(user_id),
    "business_id" -> Some(business_id),
    "stars" -> stars,
    "date" -> date,
    "text" -> text,
    "useful" -> useful
  ).filter(_._2.isDefined).mapValues(_.get)
}
