package preprocessing.models.json

import scala.util.Try

case class User(user_id: String,
                name: Option[String],
                review_count: Option[String],
                yelping_since: Option[String],
                // useful: Double,
                // funny: Double,
                // cool: Double,
                // elite: String,
                friends: Option[String],
                // fans: Double,
                average_stars: Option[String],
                compliment_hot: Option[String],
                compliment_more: Option[String],
                compliment_profile: Option[String],
                compliment_cute: Option[String],
                compliment_list: Option[String],
                compliment_note: Option[String],
                compliment_plain: Option[String],
                compliment_cool: Option[String],
                compliment_funny: Option[String],
                compliment_writer: Option[String],
                compliment_photos: Option[String]) {
  def parseDouble(s: String): Double = Try { s.toDouble }.toOption.getOrElse(0)

  val compliments: Double = Vector(
    compliment_hot,
    compliment_more,
    compliment_profile,
    compliment_cute,
    compliment_list,
    compliment_note,
    compliment_plain,
    compliment_cool,
    compliment_funny,
    compliment_writer,
    compliment_photos,
  ).filter(_.isDefined).map(c => parseDouble(c.get)).sum
}
