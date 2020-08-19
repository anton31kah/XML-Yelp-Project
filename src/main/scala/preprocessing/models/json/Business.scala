package preprocessing.models.json

case class Hours(monday: Option[String],
                 tuesday: Option[String],
                 wednesday: Option[String],
                 thursday: Option[String],
                 friday: Option[String],
                 saturday: Option[String],
                 sunday: Option[String]) {
  def toMap: Map[String, String] = Map(
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
                    postal_code: Option[String],
                    latitude: Option[String],
                    longitude: Option[String],
                    stars: Option[String],
                    review_count: Option[String],
                    is_open: Option[String],
//                    attributes: Option[Map[String, Any]],
                    categories: Option[String],
                    hours: Option[Hours])
