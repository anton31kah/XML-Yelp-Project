package preprocessing.models.json

case class Hours(Monday: String,
                 Tuesday: String,
                 Friday: String,
                 Wednesday: String,
                 Thursday: String,
                 Sunday: String,
                 Saturday: String)

case class Business(business_id: String,
                    name: String,
                    address: String,
                    city: String,
                    state: String,
                    `postal code`: String,
                    latitude: Double,
                    longitude: Double,
                    stars: Double,
                    review_count: Double,
                    is_open: Double,
                    attributes: Map[String, String],
                    categories: List[String],
                    hours: Hours)
