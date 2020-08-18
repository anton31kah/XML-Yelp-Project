package preprocessing.util

object Paths {
  object SampleData {
    private val parent = "/opt/spark-data/sample-data"

    val business = s"$parent/business.json"
    val checkin = s"$parent/checkin.json"
    val review = s"$parent/review.json"
    val tip = s"$parent/tip.json"
    val user = s"$parent/user.json"

    val tipOutputWriteJson = "/tip_output_WriteJson.json"
    val tipOutputToJsonWriteText = "/tip_output_ToJsonWriteText.json"
  }

  object RealData {
    private val antonDirectory = "C:/Users/anton/Downloads/yelp_dataset"
    private val awsDirectory = ""
    private val parent = antonDirectory
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
