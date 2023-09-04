//package C03
//
//import org.apache.spark.sql.SparkSession
//
//object testds_df_rdd {
//  def main(args: Array[String]): Unit = {
//    val spark = SparkSession
//      .builder()//创建spark会话
//      .appName("Spark SQL basic example")//设置会话名称
//      .master("local") //设置本地模式
//      .getOrCreate()//创建会话变量
//    import spark.implicits._
//    val df = spark.read.json("./src/C03/people.json")
//    val rdd = spark.sparkContext.parallelize(Array(1,2,3,4))
//    case class Person(name:String,age:Long)
//    val rdd1 = df.rdd//df->rdd
//    val ds = df.as[Person]//df->ds
//    val df1 = ds.toDF()//ds->df
//    val rdd2 = ds.rdd//df->rdd
//    val df2 = rdd.toDF("id")//rdd->df
//    val ds2 = rdd.map(x=>Person(x.toString,x)).toDS()//rdd->ds
//  }
//}
