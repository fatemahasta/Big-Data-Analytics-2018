import org.apache.log4j.{Logger, Level}
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors

object kMeans {

  def main(args: Array[String]): Unit = {



    val sparkConf = new SparkConf().setAppName("KMeans").setMaster("local[*]")

    val sc=new SparkContext(sparkConf)

    // Turn off Info Logger for Consolexxx
    Logger.getLogger("org").setLevel(Level.OFF);
    Logger.getLogger("akka").setLevel(Level.OFF);
    // Load and parse the data
    val data = sc.textFile("input")
    val parsedData = data.map(s => Vectors.dense(s.split(',').map(_.toDouble))).cache()

    //Look at how training data is!
    parsedData.foreach(f=>println(f))

    // Cluster the data into four classes using KMeans
    val numClusters = 4
    val numIterations = 50
    val clusters = KMeans.train(parsedData, numClusters, numIterations)

    // Evaluate clustering by computing Within Set Sum of Squared Errors
    val WSSSE = clusters.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSSE)

    //Look at how the clusters are in training data by making predictions
    println("Clustering on training data: ")
    clusters.predict(parsedData).zip(parsedData).foreach(f=>println(f._2,f._1))

    clusters.save(sc, "data/KMeansModel")
    val sameModel = KMeansModel.load(sc, "data/KMeansModel")

    val numClusters1 = 3
    val numIterations1 = 50
    val clusters1 = KMeans.train(parsedData, numClusters1, numIterations1)

    // Evaluate clustering by computing Within Set Sum of Squared Errors
    val WSSSE1 = clusters1.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSSE1)

    //Look at how the clusters are in training data by making predictions
    println("Clustering on training data: ")
    clusters1.predict(parsedData).zip(parsedData).foreach(f=>println(f._2,f._1))

    // Save and load model
    clusters1.save(sc, "data/KMeansModel")
    val sameModel1 = KMeansModel.load(sc, "data/KMeansModel")


  }


}