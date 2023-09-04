import Array._
//集合
object Test19 {
  def main(args: Array[String]) {
    // 定义整型 List

    val x = List(1, 2, 3, 4)
    print(" " + x)
    // 定义 Set
    val x1 = Set(1, 3, 5, 7)
    print(" " + x1)
    // 定义 Map
    val x2 = Map("one" -> 1, "two" -> 2, "three" -> 3)
    print(" " + x2)
    // 创建两个不同类型元素的元组
    val x3 = (10, "Runoob")
    print(" " + x3)
    // 定义 Option
    val x4: Option[Int] = Some(5)
    print(" " + x4)

  }
}