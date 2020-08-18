package preprocessing.models.json

case class Review(review_id: String,
                  user_id: String,
                  business_id: String,
                  stars: Double,
                  useful: Double,
                  funny: Double,
                  cool: Double,
                  text: String,
                  date: String)
