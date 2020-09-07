package preprocessing.models.json

case class Hours(monday: Option[String],
                 tuesday: Option[String],
                 wednesday: Option[String],
                 thursday: Option[String],
                 friday: Option[String],
                 saturday: Option[String],
                 sunday: Option[String]) extends RedisModel {
  override def toMap: Map[String, String] = Map(
    "monday" -> monday,
    "tuesday" -> tuesday,
    "wednesday" -> wednesday,
    "thursday" -> thursday,
    "friday" -> friday,
    "saturday" -> saturday,
    "sunday" -> sunday,
  ).filter(_._2.isDefined).mapValues(_.get)
}

case class Business(business_id: String,
                    name: Option[String],
                    address: Option[String],
                    city: Option[String],
                    state: Option[String],
                    // postal_code
                    latitude: Option[String],
                    longitude: Option[String],
                    stars: Option[String],
                    review_count: Option[String],
                    is_open: Option[String],
                    // attributes:
                    categories: Option[String],
                    hours: Option[Hours]) extends RedisModel {
  override def toMap: Map[String, String] = Map(
    "name" -> name,
    "address" -> address,
    "city" -> city,
    "state" -> state,
    "latitude" -> latitude,
    "longitude" -> longitude,
    "stars" -> stars,
    "review_count" -> review_count,
    "is_open" -> is_open
  ).filter(_._2.isDefined).mapValues(_.get)
}
