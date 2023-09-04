class Outer1{
  //Scala 中，如果没有指定任何的修饰符，则默认为 public。这样的成员在任何地方都可以被访问。
  class Inner1 {
    def f() { println("f") }
    class InnerMost {
      f() // 正确
    }
  }
  (new Inner1).f() // 正确因为 f() 是 public

}