package preprocessing.models.preprocessed

case class Hours(monday: String,
                 tuesday: String,
                 friday: String,
                 wednesday: String,
                 thursday: String,
                 sunday: String,
                 saturday: String)

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
                    attributes: Map[String, String],
                    categories: List[String],
                    hours: Hours)
