import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Mayanka on 01-Sep-16.
  */
object SparkTransformation {
  def main(args: Array[String]): Unit = {


    //System.setProperty("hadoop.home.dir", "F:\\winutils");

    val sparkConf = new SparkConf().setAppName("SparkTransformation").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    val movies_input=sc.textFile("input")

    val wc=movies_input.flatMap(line=>{line.split(" ")})

    top_movies = new_user_recommendations_rating_title_and_count_RDD.filter(lambda r: r[2]>=25).takeOrdered(25, key=lambda x: -x[1])



    wc.foreach(println(_))

  }

}
