# akkaBehavior

## Introduction

A behavior is an actor that represent a task or a simple action.  
There are several types of behaviors:  
* OneShotBehavior (behavior that is used only once)
* TimerBehavior (behavior that represent an task that is executed after a certain ammount of time)
* TickerBehavior (behavior that is executed repeatedly)
* ParallelBehavior (behavior that execute several behaviors asynchronously)

## simple Usage
You shall use those behavior by two ways:
* Using the class constructor or by derivated classes
* Throught the companion object (*apply* method)

## Complex Usage
It is possible to create user-defined behaviors.  
To do that you must go to ComplexRunning state.  
Example:  

```scala
class MyComplexBehavior extends AbstractBehavior(() => {}){
 
  
  override final protected def init = {}
  
  def complexRun = {
     // TODO: userde
   }
  
  when(ComplexRunning)
  {
    //this handler must exist
    case Event(ComplexRun, _) => complexRun
                                  stay

     //Once the complex run is finished you must go to state Ended	
    case Event(FinishedRun, _) => self ! Poke
                                  goto(Ended) 

  }
}


```
