import org.apache.spark.{SparkConf, SparkContext}



object SparkWordCount {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("local").set("com.spark.executor", "")

    val sc = new SparkContext(conf)

    val movierate = sc.textFile("u.data")

    //map transformation -- split the file contents with tab separation
    val spliting = movierate.map{t =>
      val p = t.split("\t")
      (p(0),1)
    }


    //reduceByKey transformation -- reduce the distinct userid
    val op3= spliting.reduceByKey((x,y) => x+y)
    //op3.saveAsTextFile("test2")

    //sortBy transformation -- to sort the RDD based on value
    val sorting = op3.sortBy(_._2, false)

    //SaveAsTextFile action -- to save the sorted usreid
    sorting.saveAsTextFile("Sorted_userid")
//val hey = sorting.take(25)
    //hey.foreach(println(_))

  val hey = sorting.filter(_._2 > 25)
    hey.saveAsTextFile("test3")
     // var first25 = sorting.map((_, 1L))
      //.reduceByKey(_ + _)
      //.map{ case ((k, v)) => (k, (v >=25)) }
      //.groupByKey
    //first25.saveAsTextFile("test3")

    //print the first element from RDD



    //Stop the SparkContext
    //sc.stop()


  }

}
