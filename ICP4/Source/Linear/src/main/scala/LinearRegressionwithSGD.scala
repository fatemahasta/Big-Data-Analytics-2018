import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import scala.collection.mutable.ListBuffer

object LinearRegression {

  def main(args: Array[String]): Unit ={

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc=new SparkContext(sparkConf)

    // Turn off Info Logger for Consolexxx
    Logger.getLogger("org").setLevel(Level.OFF);
    Logger.getLogger("akka").setLevel(Level.OFF);

    // Load and parse the data
    val data = sc.textFile("input")
    val parsedLabels = data.map(line=>{line.split("\t")}).map(line=>(line(0),line(1)))
   // parsedLabels.take(1).foreach(f=>println(f))


    // Lets consider binary values for man and woman where man=1.0, woman=0.0
    val predictionAndLabels1 =  parsedLabels.map(x => (if (x._1.equals("man"))
      "1".toDouble
    else "0".toDouble,

      if (x._2.equals("woman"))
        "0".toDouble
      else "1".toDouble))

    predictionAndLabels1.foreach(println(_))

    //calculate metrics
    val metrics = new MulticlassMetrics(predictionAndLabels1)

    val confusionMatrix = metrics.confusionMatrix
    val TP = confusionMatrix(0, 0)
    val FN = confusionMatrix(0, 1)
    val FP = confusionMatrix(1, 0)
    val TN = confusionMatrix(1, 1)
    val total = TP + FN + FP + TN

    //Printing the values

    println("\nConfusion Matrix:\n" + confusionMatrix + "\n")
    println("Accuracy: " + (TP + TN) / total)
    println("Misclassification Rate: " + (FN + FP) / total)
    println("Precision: " + TP / (TP + FP))
    println("True Positive Rate: " + TP / (TP + FN))
    println("False Positive Rate: " + (FP / (FP + TN)))
    println("Specificity: " +(TN / (FP + TN)))
    println("False Negative Rate: " +(FN / (TP + FN)))
    println("Prevalence: " + ((TP + TN) / total))

  }

}
