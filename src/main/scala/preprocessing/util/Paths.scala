package preprocessing.util

object Paths {
//  val properties = "src/main/resources/app.properties"
  val properties = "/opt/spark-data/app.properties"

  object SampleData {
    private val parent = "/opt/spark-data/sample-data"
//    private val parent = "spark-cluster/spark-data/sample-data"

    val business = s"$parent/business.json"
    val checkin = s"$parent/checkin.json"
    val review = s"$parent/review.json"
    val tip = s"$parent/tip.json"
    val user = s"$parent/user.json"

    val businessIds = s"$parent/businessIds.txt"
    val checkinIds = s"$parent/checkinIds.txt"
    val reviewIds = s"$parent/reviewIds.txt"
    val tipIds = s"$parent/tipIds.txt"
    val userIds = s"$parent/userIds.txt"
  }

  object RealData {
    private val antonDirectory = "C:/Users/anton/Downloads/yelp_dataset"
    private val awsDirectory = "/opt/spark-data/real-data"
    private val parent = awsDirectory
    private val prefix = "yelp_academic_dataset"

    val business = s"$parent/${prefix}_business.json"
    val checkin = s"$parent/${prefix}_checkin.json"
    val review = s"$parent/${prefix}_review.json"
    val tip = s"$parent/${prefix}_tip.json"
    val user = s"$parent/${prefix}_user.json"


    val businessAttributes = s"$parent/${prefix}_business_attributes.json"
    val tips2019 = s"$parent/${prefix}_tip_2019.json"
  }
}
