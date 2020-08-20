package preprocessing.models.json

case class Review(review_id: String,
                  user_id: String,
                  business_id: String,
                  stars: Option[String],
                  useful: Option[String],
                  // funny: Option[String],
                  // cool: Option[String],
                  text: Option[String],
                  date: Option[String])
