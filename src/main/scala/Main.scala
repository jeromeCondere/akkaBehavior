
class eud(f: ()=>Unit) {
  
  def mt(a: => Unit) =
  {
    
  }
    
  def rt = f()
}

object eud{
  def apply(f: => Unit) = new eud(()=>f)
}
class eud2 {
  var t: () => Unit =  _
  def this (f: =>Unit)
  {
    this()
    t = () => f
  }
  def mt(a: => Unit) =
  {
    
  }
    
  def rt = t()
}
object eud2 {
  def apply(f: => Unit) = new eud2(f)
}
object Main extends App {
  
  var a =2
  
  val b = eud{
    println("hello ")
    print("a")
    a+=3
  }
println(a)
b.rt
println(a) 
b.rt
println(a) 
}
