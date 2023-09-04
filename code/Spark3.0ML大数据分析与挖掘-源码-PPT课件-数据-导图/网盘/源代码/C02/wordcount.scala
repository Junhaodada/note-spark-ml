import org.apache.spark.sql.SparkSession

object wordcount {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder                //创建spark会话
      .master("local")        //设置本地模式
      .appName("wordcount")   //设置名称
      .getOrCreate()          //创建会话变量
    import spark.implicits._
    val data = spark.read.text("c://wc.txt")		//读取文件为Dataframe格式
    data.as[String].rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_).collect().foreach(println)
    //word计数

  }
}
