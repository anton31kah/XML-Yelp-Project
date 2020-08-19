package preprocessing.models.preprocessed

case class Hours(monday: String,
                 tuesday: String,
                 wednesday: String,
                 thursday: String,
                 friday: String,
                 saturday: String,
                 sunday: String) {
  def toMap: Map[String, String] = Map(
    "monday" -> monday,
    "tuesday" -> tuesday,
    "wednesday" -> wednesday,
    "thursday" -> thursday,
    "friday" -> friday,
    "saturday" -> saturday,
    "sunday" -> sunday,
  )
}

case class Business(businessId: String,
                    name: String,
                    address: String,
                    city: String,
                    state: String,
                    postalCode: String,
                    latitude: Double,
                    longitude: Double,
                    stars: Double,
                    reviewCount: Double,
                    isOpen: Double,
//                    attributes: Map[String, String],
                    categories: List[String],
                    hours: Hours)
