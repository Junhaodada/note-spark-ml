import scala.io.Source
//文件 I/O
object Test36 {
  def main(args: Array[String]) {
    println("文件内容为:" )

    Source.fromFile("test.txt" ).foreach{
      print
    }
  }
}