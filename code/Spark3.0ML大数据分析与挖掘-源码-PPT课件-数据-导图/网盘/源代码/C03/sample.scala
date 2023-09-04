package C03

import org.apache.spark.sql.SparkSession

object sample {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()//创建spark会话
      .appName("Spark SQL basic example")//设置会话名称
      .master("local") //设置本地模式
      .getOrCreate()//创建会话变量
    val df = spark.read.json("./src/C03/employees.json")
    df.sample(withReplacement = true,fraction = 0.6,seed = 10).show()
  }
}
