package preprocessing.jobs

import org.apache.spark.sql.SparkSession
import preprocessing.util.Paths

case class Attributes(AcceptsInsurance: String,
                      AgesAllowed: String,
                      Alcohol: String,
                      Ambience: String,
                      BYOB: String,
                      BYOBCorkage: String,
                      BestNights: String,
                      BikeParking: String,
                      BusinessAcceptsBitcoin: String,
                      BusinessAcceptsCreditCards: String,
                      BusinessParking: String,
                      ByAppointmentOnly: String,
                      Caters: String,
                      CoatCheck: String,
                      Corkage: String,
                      DietaryRestrictions: String,
                      DogsAllowed: String,
                      DriveThru: String,
                      GoodForDancing: String,
                      GoodForKids: String,
                      GoodForMeal: String,
                      HairSpecializesIn: String,
                      HappyHour: String,
                      HasTV: String,
                      Music: String,
                      NoiseLevel: String,
                      Open24Hours: String,
                      OutdoorSeating: String,
                      RestaurantsAttire: String,
                      RestaurantsCounterService: String,
                      RestaurantsDelivery: String,
                      RestaurantsGoodForGroups: String,
                      RestaurantsPriceRange2: String,
                      RestaurantsReservations: String,
                      RestaurantsTableService: String,
                      RestaurantsTakeOut: String,
                      Smoking: String,
                      WheelchairAccessible: String,
                      WiFi: String)

object BusinessAttributesAnalysis2 {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder
      .appName("Simple Application")
      // .master("spark://localhost:7077")
      .master("local[*]")
      .getOrCreate()

    import sparkSession.implicits._

    val businessDf = sparkSession.read.json(Paths.RealData.business)

    val attributesDf = businessDf.select($"attributes.*").as[Attributes]

    val columnsExplored = Map[String, Attributes => String](
      "attributes.HairSpecializesIn" -> (_.HairSpecializesIn),
      "attributes.HappyHour" -> (_.HappyHour),
      "attributes.HasTV" -> (_.HasTV),
      "attributes.Music" -> (_.Music),
      "attributes.NoiseLevel" -> (_.NoiseLevel),
      "attributes.Open24Hours" -> (_.Open24Hours),
      "attributes.OutdoorSeating" -> (_.OutdoorSeating),
      "attributes.RestaurantsAttire" -> (_.RestaurantsAttire),
      "attributes.RestaurantsCounterService" -> (_.RestaurantsCounterService),
      "attributes.RestaurantsDelivery" -> (_.RestaurantsDelivery),
      "attributes.RestaurantsGoodForGroups" -> (_.RestaurantsGoodForGroups),
      "attributes.RestaurantsPriceRange2" -> (_.RestaurantsPriceRange2),
      "attributes.RestaurantsReservations" -> (_.RestaurantsReservations),
      "attributes.RestaurantsTableService" -> (_.RestaurantsTableService),
      "attributes.RestaurantsTakeOut" -> (_.RestaurantsTakeOut),
      "attributes.Smoking" -> (_.Smoking),
      "attributes.WheelchairAccessible" -> (_.WheelchairAccessible),
      "attributes.WiFi" -> (_.WiFi),
    )

    val columnsDistinctValues = attributesDf
      .map(att => columnsExplored.mapValues(mapper => Set(mapper(att))))
      .reduce((map1, map2) => columnsExplored.keys
          .map(col => col -> (map1(col) ++ map2(col)))
          .toMap[String, Set[String]])

    println(columnsDistinctValues)
    for ((col, values) <- columnsDistinctValues) {
      println(col)
      println(values)
      println("=" * 20)
    }

    sparkSession.stop()
  }
}
