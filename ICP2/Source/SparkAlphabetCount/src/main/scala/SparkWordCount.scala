

import org.apache.spark.{SparkContext, SparkConf}

object SparkWordCount {

  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sc=new SparkContext(sparkConf)

    val input=sc.textFile("input")

    val wc=input.flatMap(line=>{line.split(" ")})
    val wc1 = wc.groupBy(word=>word.charAt(0))
    //val output = wc1.collect()
  //val output1 = output.flatten.mkString(",")
    //output1.map{case (k, (xs, ys)) =>
      //s"""($k, ((${xs.mkString(",")}), (${ys.mkString(",")}))"""}
    //output.foreach(println(_))
    //val output = wc.collect()
    //output.map(word => (wordmkString("")))
    //val output=wc.reduceByKey(_+_)

    wc1.saveAsTextFile("output")


    //val o=output.collect()

    //var s:String="Words:Count \n"
    //o.foreach{case(word,count)=>{

      //s+=word+" : "+count+"\n"

    //}}

  }

}
