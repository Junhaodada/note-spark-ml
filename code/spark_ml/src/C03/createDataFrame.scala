package C03


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.spark.sql.types._

// 创建 SparkConf 对象来配置 Spark 应用程序
val conf = new SparkConf().setAppName("MySparkApp")

// 创建 SparkContext 对象
val sc = new SparkContext(conf)

// 创建 SparkSession，使用已经存在的 SparkContext 对象 sc
val sparkSession = new org.apache.spark.sql.SparkSession(sc)

val schema =
  StructType(
    StructField("name", StringType, false) ::
      StructField("age", IntegerType, true) :: Nil)

val people =
  sc.textFile("examples/src/main/resources/people.txt").map(
    _.split(",")).map(p => Row(p(0), p(1).trim.toInt))
val dataFrame = sparkSession.createDataFrame(people, schema)
dataFrame.printSchema
//root
//|-- name: string (nullable = false)
//|-- age: integer (nullable = true)

dataFrame.createOrReplaceTempView("people")
sparkSession.sql("select name from people").collect.foreach(println)
