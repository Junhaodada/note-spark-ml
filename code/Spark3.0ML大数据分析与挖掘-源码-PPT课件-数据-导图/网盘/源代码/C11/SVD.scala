package C11

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.sql.SparkSession


object SVD {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder      //创建spark会话
      .master("local")  //设置本地模式
      .appName("SVD")  //设置名称
      .getOrCreate()   //创建会话变量

    // 加载向量
    val data = Array(
      Vectors.sparse(5, Seq((1, 1.0), (3, 7.0))),
      Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
      Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0)
    )

    //转换成RowMatrix的输入格式
    val data1 = spark.sparkContext.parallelize(data)

    // 建立模型
    val rm = new RowMatrix(data1)           		 //读入行矩阵
    val SVD = rm.computeSVD(2, computeU = true)	 //进行SVD计算
    println(SVD)							 //打印SVD结果矩阵

  }
}
